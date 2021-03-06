package com.bus.stripes.actionbean.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.dto.vehicleprofile.VehicleTeamLeader;
import com.bus.dto.vehicleprofile.VehicleTeamMember;
import com.bus.services.CommonBean;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.stripes.selector.VehicleSelector;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/VehicleTeam.action")
public class VehicleTeamActionBean extends CustomActionBean{

	private VehicleSelector selector;
	private List<String> selectedVehicles;
	private VehicleBean vBean;
	private CommonBean cBean;
	private VehicleTeam team;
	private List<Employee> leaders;
	private List<VehicleTeam> teamsNow;
	private List<VehicleProfile> members;
	private Long totalcount;
	private VehicleTeam newTeam;
	private String deleteId;
	private Employee leader;
	private String deleteLeaderId;
	
	@DefaultHandler
	@Secure(roles = Roles.VEHICLE_TEAM_VIEW)
	public Resolution defaultAction(){
		String teamId = context.getRequest().getParameter("teamId");
		if(teamId == null)
			teamId = context.getRequest().getParameter("selector.teamId");
		try{
			setTeamsNow(vBean.getAllVehicleTeams());
			Map map = null;
			if(selector == null)
				map = vBean.getVehicleProfileByTeamId(teamId);
			else{
				map = vBean.getVehicleProfileByTeamStatement(selector.getTeamSelectorStatement());
			}
			members = (List<VehicleProfile>) map.get("list");
			totalcount = (Long) map.get("count");
			if(members.size() > 0){
				if(members.get(0).getVehicleteam() != null)
					setTeam(members.get(0).getVehicleteam().getTeam());
			}
			if(team != null){
				Set<VehicleTeamLeader> ls = team.getLeaders();
				leaders = new ArrayList();
				for(VehicleTeamLeader l :ls){
					leaders.add(l.getLeader());
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			members = new ArrayList<VehicleProfile>();
			setTotalcount(0L);
		}
		return new ForwardResolution("/vehicle/team.jsp");
	}

	@HandlesEvent(value="newTeamAction")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT_TEAM)
	public Resolution newTeamAction(){
		try{
			if(newTeam != null && !newTeam.getName().trim().equals(""))
				vBean.saveTeam(newTeam);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="deleteTeamAction")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT_TEAM)
	public Resolution deleteTeamAction(){
		try{
			if(deleteId != null){
				vBean.removeTeam(deleteId);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="addTeamLeaderAction")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT_TEAM)
	public Resolution addTeamLeaderAction(){
		try{
			if(deleteId != null && leader != null && leader.getWorkerid() != null){
				Employee e = cBean.getEmployeeByWorkerId(leader.getWorkerid());
				if(e == null)
					e= cBean.getEmployeeByName(leader.getFullname(), true).get(0);
				if(e != null){
					VehicleTeam teamTemp = vBean.getVehicleTeamByTeamId(deleteId);
					VehicleTeamLeader l = new VehicleTeamLeader();
					l.setTeam(teamTemp);
					l.setLeader(e);
					vBean.saveTeamLeader(l);
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="deleteTeamLeaderAction")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT_TEAM)
	public Resolution deleteTeamLeaderAction(){
		try{
			if(deleteId != null && deleteLeaderId != null){
				Employee e = cBean.getEmployeeById(deleteLeaderId);
				VehicleTeam t = vBean.getVehicleTeamByTeamId(deleteId);
				if(e != null && t != null){
					vBean.removeTeamLeader(t,e);
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="quitTeam")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT)
	public Resolution quitTeam(){
		try{
			String teamId = context.getRequest().getParameter("teamId");
			if(selectedVehicles != null && teamId != null){
				vBean.quitTeam(teamId,selectedVehicles);
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="joinTeam")
	@Secure(roles = Roles.VEHICLE_TEAM_EDIT)
	public Resolution joinTeam(){
		try{
			String teamId = context.getRequest().getParameter("teamId");
			if(selectedVehicles != null && teamId != null){
				vBean.JoinTeam(teamId,selectedVehicles);
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public VehicleSelector getSelector() {
		return selector;
	}

	public void setSelector(VehicleSelector selector) {
		this.selector = selector;
	}

	public List<String> getSelectedVehicles() {
		return selectedVehicles;
	}

	public void setSelectedVehicles(List<String> selectedVehicles) {
		this.selectedVehicles = selectedVehicles;
	}

	public List<VehicleProfile> getMembers() {
		return members;
	}

	public void setMembers(List<VehicleProfile> members) {
		this.members = members;
	}

	public VehicleBean getvBean() {
		return vBean;
	}
	@SpringBean
	public void setvBean(VehicleBean vBean) {
		this.vBean = vBean;
	}

	public VehicleTeam getTeam() {
		return team;
	}

	public void setTeam(VehicleTeam team) {
		this.team = team;
	}

	public List<VehicleTeam> getTeamsNow() {
		return teamsNow;
	}

	public void setTeamsNow(List<VehicleTeam> teamsNow) {
		this.teamsNow = teamsNow;
	}

	public Long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}

	public List<Employee> getLeaders() {
		return leaders;
	}

	public void setLeaders(List<Employee> leaders) {
		this.leaders = leaders;
	}

	public VehicleTeam getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(VehicleTeam newTeam) {
		this.newTeam = newTeam;
	}

	public String getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}

	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public CommonBean getcBean() {
		return cBean;
	}
	@SpringBean
	public void setcBean(CommonBean cBean) {
		this.cBean = cBean;
	}

	public String getDeleteLeaderId() {
		return deleteLeaderId;
	}

	public void setDeleteLeaderId(String deleteLeaderId) {
		this.deleteLeaderId = deleteLeaderId;
	}
}
