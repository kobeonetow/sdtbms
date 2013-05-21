package com.bus.stripes.selector;

import java.util.Calendar;
import java.util.Date;

import com.bus.util.HRUtil;

public class ScoreViewPublicSelector implements BMSSelector{
	
	private final static int TYPE_BY_HISTORY = 0;
	private final static int TYPE_BY_YEAR = 1;
	private final static int TYPE_BY_MONTH= 2;
	
	private final static int TYPE_BY_FIX_SCORE = 1;
	private final static int TYPE_BY_TEMP_SCORE = 0;
	private final static int TYPE_BY_TOTAL_SCORE = 2;
	
	private Date recordDate;
	
	private Date recordStartDate;
	private Date recordEndDate;
	private Integer selecttype;
	private Integer scoretype;
	private Integer order;
	
	private Integer department;
	private String position;
	
	@Override
	public String getSelectorStatement() {
		if(recordDate == null){
			recordDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(recordDate);
		String scoretypeselection = getScoreTypeQuery();
		String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score) AS tempscore, SUM(fixscore+score) AS totalscore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
				" employee.fullname AS name, employee.firstworktime AS firstworktime," +
				" position.name AS positionname " +
				" FROM scoresummary JOIN employee ON scoresummary.workerid=employee.workerid" +
				" JOIN position ON employee.positionid = position.id" +
				" WHERE EXTRACT(month FROM date)="+(c.get(Calendar.MONTH)+1) +
						" GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		return query;
	}

	private String getOrderString() {
		if(order == null)
			order = 0;
		if(order == 1){
			return "ASC";
		}else{
			order = 0;
			return "DESC";
		}
	}

	private String getScoreTypeQuery() {
		String scoretypeselection = "";
		if(scoretype == null)
			scoretype = 0;
		if(scoretype == TYPE_BY_FIX_SCORE){
			scoretypeselection = "ORDER BY SUM(fixscore) "+getOrderString();
		}else if(scoretype == TYPE_BY_TEMP_SCORE){
			scoretypeselection = "ORDER BY SUM(score) "+getOrderString();
		}else if(scoretype == TYPE_BY_TOTAL_SCORE){
			scoretypeselection = "ORDER BY SUM(fixscore+score) " +getOrderString();
		}else{
			scoretypeselection = "ORDER BY SUM(score) "+getOrderString();
		}
		return scoretypeselection;
	}

	public String getNormalStatement(){
		if(recordDate == null){
			recordDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(recordDate);
		String scoretypeselection = getScoreTypeQuery();
		String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score) AS tempscore, SUM(fixscore+score) AS totalscore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
				" employee.fullname AS name, employee.firstworktime AS firstworktime," +
				" position.name AS positionname " +
				" FROM scoresummary JOIN employee ON scoresummary.workerid=employee.workerid"+
				" JOIN position ON employee.positionid = position.id";
		if(selecttype == null)
			selecttype = 2;
		if(selecttype == 0){
			//order in history
		}else if(selecttype == 1){
			//order in year
			query += "  WHERE EXTRACT(year FROM date)="+c.get(Calendar.YEAR); 
		}else{
			//order in month
			query += "  WHERE EXTRACT(month FROM date)="+(c.get(Calendar.MONTH)+1);
		}
		if(department != null){
			query += " AND employee.departmentid="+department;
		}
		if(position != null){
			query += " AND position.name LIKE '%"+position+"%'";
		}
		query += " GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		return query;
	}
	
	public String getInTimeRangeStatement(){
		if(recordStartDate == null || recordEndDate == null){
			return getNormalStatement();
		}else{
			Calendar start = Calendar.getInstance();
			start.setTime(recordStartDate);
			Calendar end = Calendar.getInstance();
			end.setTime(recordEndDate);
			String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score) AS tempscore, SUM(fixscore+score) AS totalscore, RANK() OVER ("+getScoreTypeQuery()+") AS rank, COUNT(scoresummary.workerid) AS count," +
					" employee.fullname AS name, employee.firstworktime AS firstworktime," +
					" position.name AS positionname " +
					" FROM scoresummary JOIN employee ON scoresummary.workerid=employee.workerid" +
					" JOIN position ON employee.positionid = position.id" +
					" WHERE EXTRACT(month FROM date)>="+(start.get(Calendar.MONTH)+1)+
					" AND EXTRACT(month FROM date)<="+(end.get(Calendar.MONTH)+1)+
					" AND EXTRACT(year FROM date)>="+start.get(Calendar.YEAR)+
					" AND EXTRACT(year FROM date)<="+end.get(Calendar.YEAR);
			if(department != null){
				query += " AND employee.departmentid="+department;
			}
			if(position != null){
				query += " AND position.name LIKE '%"+position+"%'";
			}
			query += " GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
			return query;
		}
	}
	
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Date getRecordStartDate() {
		return recordStartDate;
	}

	public void setRecordStartDate(Date recordStartDate) {
		this.recordStartDate = recordStartDate;
	}

	public Date getRecordEndDate() {
		return recordEndDate;
	}

	public void setRecordEndDate(Date recordEndDate) {
		this.recordEndDate = recordEndDate;
	}

	public Integer getSelecttype() {
		return selecttype;
	}

	public void setSelecttype(Integer selecttype) {
		this.selecttype = selecttype;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public static int getTypeByHistory() {
		return TYPE_BY_HISTORY;
	}

	public static int getTypeByYear() {
		return TYPE_BY_YEAR;
	}

	public static int getTypeByMonth() {
		return TYPE_BY_MONTH;
	}

	public static int getTypeByFixScore() {return TYPE_BY_FIX_SCORE;}
	public static int getTypeByTempScore() {return TYPE_BY_TEMP_SCORE;}
	public static int getTypeByTotalScore() {return TYPE_BY_TOTAL_SCORE;}
	

	public Integer getScoretype() {
		return scoretype;
	}

	public void setScoretype(Integer scoretype) {
		this.scoretype = scoretype;
	}
}