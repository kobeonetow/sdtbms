package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.score.ScoreDivGroup;
import com.bus.dto.score.Scoreapprover;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.Roles;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/ScoreApprover.action")
public class ScoreApproverActionBean  extends CustomActionBean{
	
	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private List<ScoreDivGroup> scoreGroups;
	private List<Scoreapprover> approvers;
	
	private	List<Scoreapprover> newApprovers;
	private String isApprover;
	private String workerid;
	
	protected void loadOptionList(){
		try{
			setScoreGroups(scoreBean.getAllScoreGroups());
//			vehicleteams = hrBean.getAllVehicleTeams();
			approvers = scoreBean.getDistinctApprovers();
		}catch(Exception e){
			e.printStackTrace();
			approvers = new ArrayList<Scoreapprover>();
		}
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		loadOptionList();
		return new ForwardResolution("/score/scoreapprover.jsp");
	}
	
	@Secure(roles=Roles.SCOREAPPROVER_SYSTEM)
	@HandlesEvent(value="addApprover")
	public Resolution addApprover(){
		try{
			if(newApprovers != null && isApprover != null && workerid != null){
				Employee e = new Employee();
				e.setWorkerid(workerid);
				for(Scoreapprover sa:newApprovers){
					if(sa != null && sa.getGroup() != null){
						sa.setApprover(e);
						sa.setIsapprover(isApprover);
						scoreBean.addApprover(sa, context.getUser());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolution("添加出错", "添加部门到审核人出错。时间"+new Date()+" 信息:"+e.getMessage());
		}
		return defaultAction();
	}
	
	/**
	 * 获取指定员工的已经存在的审核内容
	 * @return json String
	 */
	@Secure(roles=Roles.SCOREAPPROVER_SYSTEM)
	@HandlesEvent(value="getApprovedList")
	public Resolution getApprovedList(){
		JsonObject json = new JsonObject();
		try{
			String workerid = context.getRequest().getParameter("workerid");
			if(workerid != null){
				Employee e = hrBean.getEmployeeByWorkerId(workerid);
				approvers = scoreBean.getApproverSections(e);
				json.addProperty("result", "1");
				JsonArray jarray = new JsonArray();
				for(Scoreapprover app:approvers){
					JsonObject jo = new JsonObject();
					jo.addProperty("id", app.getId());
					jo.addProperty("name", app.getGroup().getName());
					jo.addProperty("isapprover", app.getIsapprover());
					jarray.add(jo);
				}
				json.add("list", jarray);
			}else{
				json.addProperty("result", "0");
				json.addProperty("msg","找不到工号，员工不存在");
			}
			return new StreamingResolution("text/charset=utf8;",json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg","服务器搜索出错，错误信息:"+e.getMessage());
			return new StreamingResolution("text/charset=utf8;",json.toString());
		}
	}
	
	@HandlesEvent(value="delApproverSection")
	public Resolution delApproverSection(){
		JsonObject json = new JsonObject();
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId != null){
				scoreBean.delApproverSection(targetId,context.getUser());
				json.addProperty("result", "1");
			}else{
				json.addProperty("result", "0");
				json.addProperty("msg","没有提供合适的记录ID");
			}
		}catch(Exception e){
			json.addProperty("result", "0");
			json.addProperty("msg","删除出错。"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf8;",json.toString());
	}
	
	@HandlesEvent(value="toggleSection")
	public Resolution toggleSection(){
		JsonObject json = new JsonObject();
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId != null){
				scoreBean.toggleApproverStatus(targetId,context.getUser());
				json.addProperty("result", "1");
			}else{
				json.addProperty("result", "0");
				json.addProperty("msg","没有提供合适的记录ID");
			}
		}catch(Exception e){
			json.addProperty("result", "0");
			json.addProperty("msg","修改出错。"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf8;",json.toString());
	}
	
	public List<Scoreapprover> getApprovers() {
		return approvers;
	}
	public void setApprovers(List<Scoreapprover> approvers) {
		this.approvers = approvers;
	}
	public List<ScoreDivGroup> getScoreGroups() {
		return scoreGroups;
	}
	public void setScoreGroups(List<ScoreDivGroup> scoreGroups) {
		this.scoreGroups = scoreGroups;
	}
	public List<Scoreapprover> getNewApprovers() {
		return newApprovers;
	}
	public void setNewApprovers(List<Scoreapprover> newApprovers) {
		this.newApprovers = newApprovers;
	}
	public String getIsApprover() {
		return isApprover;
	}
	public void setIsApprover(String isApprover) {
		this.isApprover = isApprover;
	}
	public String getWorkerid() {
		return workerid;
	}
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}
	
}
