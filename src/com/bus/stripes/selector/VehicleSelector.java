package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class VehicleSelector implements BMSSelector{

	
	public Date getExpire1() {
		return expire1;
	}

	public void setExpire1(Date expire1) {
		this.expire1 = expire1;
	}

	public Date getExpire2() {
		return expire2;
	}

	public void setExpire2(Date expire2) {
		this.expire2 = expire2;
	}

	private String vid;
	private Integer throwed;
	private Date expire1;
	private Date expire2;
	private String teamId;
	
	private String selfid;
	private String laneId;
	private String subcompany;
	private Integer order;
	
	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM VehicleProfile q WHERE ";
		if(vid != null){
			query += "vid LIKE '%"+vid+"%'";
			set++;
		}
		if(selfid != null){
			if(set>0)
				query += " AND";
			query += " selfid LIKE '%"+ selfid+"%'";
			set++;
		}
		if(teamId != null){
			if(set>0)
				query += " AND";
			query += " q.vehicleteam.team.id="+teamId;
			set++;
		}
		if(laneId != null){
			if(set>0)
				query += " AND";
			query += " q.vehiclelane.lane.id="+laneId;
			set++;
		}
		if(subcompany!=null){
			if(set>0)
				query += " AND";
			query += " subcompany='"+subcompany+"'";
			set++;
		}
		if(throwed == null || throwed == 0){
			if(set >0)
				query += " AND";
			query += " status = 'A'";
			set++;
		}else if(throwed != null && throwed == 1){
//			if(set >0)
//				query += " AND";
//			query += " status = 'E'";
//			set++;
		}
		if(expire1 != null || expire2 != null){
			if(set>0)
				query += " AND";
			if(expire1 != null && expire2 != null){
				query += " throwDate <='"+HRUtil.parseDateToString(expire2)+"' AND throwDate >='"+HRUtil.parseDateToString(expire1)+"'";
			}else if(expire2 != null){
				query += " throwDate <='"+HRUtil.parseDateToString(expire2)+"'";
			}else if(expire1 != null){
				query += " throwDate >='"+HRUtil.parseDateToString(expire1)+"'";
			}
			set++;
		}
		if(set==0){
			query = query.substring(0,query.indexOf("WHERE"));
		}
		if(order == null)
			query += " ORDER BY selfid";
		else{
			if (order == 2){
				query += " ORDER BY joinDate ASC";
			}else if(order == 3){
				query += " ORDER BY forcethrowdate ASC";
			}else
				query += " ORDER BY selfid";
		}
		return query;
	}

	public String getTeamSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM VehicleProfile q WHERE ";
		if(vid != null){
			query += "vid LIKE '%"+vid+"%'";
			set++;
		}
		if(teamId == null){
			if(set>0)
				query += " AND";
			query += " NOT EXISTS (SELECT m FROM VehicleTeamMember m WHERE q.id=m.vehicle.id)";
		}else{
			if(set>0)
				query += " AND";
			query += " q.vehicleteam.team.id="+teamId;
		}
		query += " ORDER BY q.selfid";
		return query;
	}

	
	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Integer getThrowed() {
		return throwed;
	}

	public void setThrowed(Integer throwed) {
		this.throwed = throwed;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getSelfid() {
		return selfid;
	}

	public void setSelfid(String selfid) {
		this.selfid = selfid;
	}

	public String getLaneId() {
		return laneId;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public String getSubcompany() {
		return subcompany;
	}

	public void setSubcompany(String subcompany) {
		this.subcompany = subcompany;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	
}
