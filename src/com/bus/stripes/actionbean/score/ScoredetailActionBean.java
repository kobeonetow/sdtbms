package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Employee;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.stripes.selector.EmployeeSelector;

@UrlBinding(value="/actionbean/Scoredetail.action")
public class ScoredetailActionBean extends CustomActionBean{

	
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private Scoremember member;
	private EmployeeSelector selector;
	private List<Employee> founds;
	private List<Scorerecord> records;
	private Date recordDate;
	private List<Scorerecord> selectedRecord;
	
	@DefaultHandler
	public Resolution defaultAction(){
		if(!getPermission(context.getUser(), "scoredetail_view")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		if(founds == null ){
			founds = new ArrayList<Employee>();
		}
		if(records == null){
			records = new ArrayList<Scorerecord>();
		}
		return new ForwardResolution("/score/scoredetail.jsp");
	}

	@HandlesEvent(value="getMembers")
	public Resolution getMembers(){
		if(selector == null)
			return defaultAction();
		Map map = hrBean.getEmployeesBySelector(0, 0, selector.getSelectorStatement());
		founds = (List<Employee>) map.get("list");
		return defaultAction();
	}
	
	@HandlesEvent(value="memberDetail")
	public Resolution memberDetail(){
		String workerid = context.getRequest().getParameter("workerid");
		if(workerid == null)
			return defaultAction();
		try{
			if(recordDate == null)
				recordDate = new Date();
			member = scoreBean.getScoreMemberByWorkerid(workerid);
			records = scoreBean.getRecords(member,recordDate);
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("获取员工详细积分错误","请联系管理员."+e.getMessage());
		}
	}

	@HandlesEvent(value="deleteRecords")
	public Resolution deleteRecords(){
		System.out.println("In delete");
		if(!getPermission(context.getUser(), "scoredetail_remove_record")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(selectedRecord != null){
				for(Scorerecord record:selectedRecord){
					if(record != null)
						scoreBean.removeScoreTypeToScoreMember(context.getUser(), record.getId()+"");
				}
			}else{
				System.out.println("selected null");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return memberDetail();
	}
	
	public Scoremember getMember() {
		return member;
	}

	public void setMember(Scoremember member) {
		this.member = member;
	}
	public EmployeeSelector getSelector() {
		return selector;
	}
	public void setSelector(EmployeeSelector selector) {
		this.selector = selector;
	}
	public List<Employee> getFounds() {
		return founds;
	}
	public void setFounds(List<Employee> founds) {
		this.founds = founds;
	}
	public List<Scorerecord> getRecords() {
		return records;
	}
	public void setRecords(List<Scorerecord> records) {
		this.records = records;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public List<Scorerecord> getSelectedRecord() {
		return selectedRecord;
	}
	public void setSelectedRecord(List<Scorerecord> selectedRecord) {
		this.selectedRecord = selectedRecord;
	}
	
	public long getRecordsSum(){
		long sum = 0L;
		if(records == null)
			return sum;
		else{
			for(Scorerecord r:records){
				sum += (long)r.getScoretype().getScore();
			}
		}
		return sum;
	}
}