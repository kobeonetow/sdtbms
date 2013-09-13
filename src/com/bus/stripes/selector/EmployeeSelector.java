package com.bus.stripes.selector;

import java.util.Date;
import java.util.List;

import com.bus.util.HRUtil;

public class EmployeeSelector implements BMSSelector{

	private String name;
	private String pob;
	private String workertype;
	private List<Integer> ps;
	private List<Integer> ds;
	private String qualification;
	private int age = 0;
	private int set = 0;
	private String sex;
	private String ethnic;
	private String politicalstatus;
	private int date=-1; //0 nothing,1 入职日期,2 按年龄排 
	private String marriage;
	private String joblevel;
	private String placebelong;
	private String domiciletype;
	private String army;
	private String status = "A";
	private String driver;
	private String workerid;
	private Date workstartdate;
	private String birthmonth;
	
	private Integer teamid;
	
	public void setWorkstartdate(Date workstartdate) {
		this.workstartdate = workstartdate;
	}
	public void setWorkenddate(Date workenddate) {
		this.workenddate = workenddate;
	}
	private Date workenddate;
	
	
	@Override
	public String getSelectorStatement() {
		String query = "SELECT q FROM Employee q WHERE";
		if(name != null){
			query += " fullname LIKE '%"+name+"%'";
			set++;
		}
		if(workerid != null){
			if(set >0)
				query += " AND";
			query += " workerid='"+workerid+"'";
			set++;
		}
		if(pob != null){
			if(set > 0)
				query += " AND";
			query += " pob LIKE '%"+pob+"%'";
			set++;
		}
		if(workertype != null){
			if(set >0)
				query += " AND";
			query += " workertype ='" +workertype+"'";
			set++;
		}
		if(ps != null && ps.size() > 0){
			if(set>0)
				query += " AND";
			if(ps.size() == 1)
				query += " q.position.id="+ps.get(0);
			else{
				query += " (";
				for(int i=0; i<ps.size();i++){
					query += "q.position.id="+ps.get(i);
					if(i < ps.size()-1)
						query += " OR ";
				}
				query += ")";
			}
			set++;
		}
		if(ds != null && ds.size() >0){
			if(set>0)
				query += " AND";
			if(ds.size() == 1)
				query += " q.department.id="+ds.get(0);
			else{
				query += " (";
				for(int i=0; i<ds.size();i++){
					query += "q.department.id="+ds.get(i);
					if(i < ds.size()-1)
						query += " OR ";
				}
				query += ")";
			}
			set++;
		}
		if(teamid != null){
			if(set>0)
				query += " AND";
			query += " q.team.id="+teamid;
			set++;
		}
		if(qualification != null){
			if(set > 0)
				query += " AND";
			query += " qualification LIKE '%"+qualification+"%'";
			set++;
		}
		if(sex != null){
			if(set > 0)
				query += " AND";
			query += " sex='"+sex+"'";
			set++;
		}
		if(ethnic != null){
			if(set > 0)
				query += " AND";
			query += " ethnic='"+ethnic +"'";
			set++;
		}
		if(birthmonth != null){
			if(set > 0)
				query += " AND";
			query += " EXTRACT(month FROM dob) = " + birthmonth;
			set++;
		}
		if(politicalstatus != null){
			if(set > 0)
				query += " AND";
			query += " politicalstatus='"+politicalstatus+"'";
			set++;
		}
		if(marriage != null){
			if(set > 0)
				query += " AND";
			query += " marriage='"+marriage+"'";
			set++;
		}
		if(joblevel != null){
			if(set > 0)
				query += " AND";
			query += " joblevel='"+joblevel+"'";
			set++;
		}
		if(domiciletype != null){
			if(set >0)
				query += " AND";
			query += " domiciletype='"+domiciletype+"'";
			set++;
		}
		if(army != null){
			if(set >0)
				query += " AND";
			query += " army LIKE '%"+army+"%'";
			set++;
		}
		if(placebelong != null){
			if(set >0)
				query += " AND";
			query += " placebelong LIKE '%"+placebelong+"%'";
			set++;
		}
		if(driver != null){
			if(set >0)
				query += " AND";
			query += " q.position.name LIKE '%"+driver +"%'";
			set++;
		}
		if(status != null){
			if(set > 0)
				query += " AND";
			String[] stats = status.split(",");
			if(stats.length >= 2)
				query += " (";
			for(int i=0; i<stats.length;i++){
				if(i != 0)
					query += " OR";
				query += " status='"+stats[i]+"'";
				set++;
			}
			if(stats.length >= 2)
				query +=")";
		}else{
			if(set > 0)
				query += " AND";
			query += " status='"+status+"'";
		}
		if(workstartdate != null || workenddate != null){
			if(set > 0)
				query += " AND";
			if(workenddate == null ){
				query += " firstworktime >= '"+HRUtil.parseDateToString(workstartdate)+"'";
			}else if(workstartdate == null){
				query += " firstworktime <= '" + HRUtil.parseDateToString(workenddate)+"'";
			}else{
				query += " firstworktime BETWEEN '"+HRUtil.parseDateToString(workstartdate)+
						"' AND '" + HRUtil.parseDateToString(workenddate)+"'";
			}
			set++;
		}
		if(date > 0){
			if(set >0){
				if(date == 1){
					query += " ORDER BY firstworktime ASC";
				}
				else if(date == 2){
					query += " ORDER BY dob ASC";
				}
			}else{
				if(date == 1){
					query = "SELECT q FROM Employee q ORDER BY firstworktime ASC";
				}else if(date == 2){
					query = "SELECT q FROM Employee q ORDER BY dob ASC";
				}
			}
		}
		return query;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkertype() {
		return workertype;
	}
	public void setWorkertype(String workertype) {
		this.workertype = workertype;
	}
	public String getPob() {
		return pob;
	}
	public void setPob(String pob) {
		this.pob = pob;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getPoliticalstatus() {
		return politicalstatus;
	}
	public void setPoliticalstatus(String politicalstatus) {
		this.politicalstatus = politicalstatus;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getJoblevel() {
		return joblevel;
	}
	public void setJoblevel(String joblevel) {
		this.joblevel = joblevel;
	}
	public String getPlacebelong() {
		return placebelong;
	}
	public void setPlacebelong(String placebelong) {
		this.placebelong = placebelong;
	}
	public String getDomiciletype() {
		return domiciletype;
	}
	public void setDomiciletype(String domiciletype) {
		this.domiciletype = domiciletype;
	}
	public String getArmy() {
		return army;
	}
	public void setArmy(String army) {
		this.army = army;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getWorkerid() {
		return workerid;
	}
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}
	public void setBirthmonth(String birthmonth){
		this.birthmonth = birthmonth;
	}
	public String getBirthmonth(){
		return this.birthmonth;
	}
	public List<Integer> getDs() {
		return ds;
	}
	public void setDs(List<Integer> ds) {
		this.ds = ds;
	}
	public List<Integer> getPs() {
		return ps;
	}
	public void setPs(List<Integer> ps) {
		this.ps = ps;
	}
	public Integer getTeamid() {
		return teamid;
	}
	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}

}
