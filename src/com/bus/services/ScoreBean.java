package com.bus.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Position;
import com.bus.dto.logger.ScoreLog;
import com.bus.dto.score.DepartmentScore;
import com.bus.dto.score.Positiongroup;
import com.bus.dto.score.ScoreDivGroup;
//import com.bus.dto.score.ScoreDivGroup;
import com.bus.dto.score.ScoreExceptionList;
import com.bus.dto.score.ScoreMemberRank;
import com.bus.dto.score.Scoreapprover;
import com.bus.dto.score.Scoregroup;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoresheetmapper;
import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoresummary;
import com.bus.dto.score.Scoretype;
import com.bus.dto.score.Voucherlist;
import com.bus.util.EmpDepartments;
import com.bus.util.HRUtil;
import com.bus.util.LoggerAction;
import com.bus.util.ScoreExcelFileProcessor;


public class ScoreBean  extends EMBean{

	/**
	 * Get the score types from giving page, if page or lotsize = -1, it will select all records
	 * <br/>
	 * returns a map with
	 * ('list',List<Scoretype>)
	 * <br/>
	 * ('count',Long)
	 * @param pagenum
	 * @param lotsize
	 * @return
	 * @throws Exception
	 */
	public Map getAllScoreTypes(int pagenum, int lotsize) throws Exception{
		List<Scoretype> list=null;
		if(pagenum == -1 || lotsize == -1)
			list = em.createQuery("SELECT q FROM Scoretype q ORDER BY reference").getResultList();
		else
			list = em.createQuery("SELECT q FROM Scoretype q ORDER BY reference")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery("SELECT count(q) FROM Scoretype q").getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Return the scoretypes inside given scoresheet
	 * @param itemlist
	 * @return
	 * @throws Exception
	 */
	public List<Scoretype> getScoretypesFromSheet(String itemlist) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, Integer.parseInt(itemlist));
		List<Scoretype> stlist = new ArrayList<Scoretype>();
		List<Scoresheetmapper> mappers = null;
		if(sheet.getParent() != null){
			mappers = em.createQuery("SELECT q FROM Scoresheetmapper q WHERE (q.sheet=:sh OR q.sheet=:sh2) ORDER BY q.type.reference")
					.setParameter("sh", sheet).setParameter("sh2", sheet.getParent()).getResultList();
		}else{
			mappers = em.createQuery("SELECT q FROM Scoresheetmapper q WHERE q.sheet=:sh ORDER BY q.type.reference").setParameter("sh", sheet).getResultList();
		}
		for(Scoresheetmapper mapper:mappers){
			stlist.add(mapper.getType());
		}
		return stlist;
	}

	/**
	 * Get scoretypes from statement with page and lotsize, if page or lotsize = -1,
	 * <br/>default will be set pagenum=1, lotsize=20;
	 * <br/>returns a map with
	 * ('list',List<Scoretype>)
	 * <br/>
	 * ('count',Long)
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	public Map getScoretypeByStatement(int pagenum, int lotsize,String statement) throws Exception{
		List<Scoretype> list=null;
		if(pagenum == -1 || lotsize == -1)
			pagenum =1;lotsize = 60;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String countstatement = "SELECT count(q) " + statement.substring(statement.indexOf("FROM"),statement.indexOf("ORDER BY"));
		Long count = (Long) em.createQuery(countstatement).getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create a new scoretype and log to score logger
	 * @param user
	 * @param scoretype
	 * @throws Exception
	 */
	@Transactional
	public void saveScoretype(Account user, Scoretype scoretype) throws Exception{
		scoretype.setCreatedate(Calendar.getInstance().getTime());
		scoretype.setAccount(user);
		em.persist(scoretype);
		em.flush();
		em.persist(LoggerAction.createScoreType(user,scoretype.getId()+""));
	}

	/**
	 * Delete score type and make a record to log
	 * @param user
	 * @param st
	 * @throws Exception
	 */
	@Transactional
	public void removeScoreType(Account user, Scoretype st) throws Exception{
		st = em.find(Scoretype.class, st.getId());
		try{
			List<Scoresheetmapper> mappers = (List<Scoresheetmapper>) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE scoretypeid=:st")
					.setParameter("st", st).getResultList();
			for(Scoresheetmapper s:mappers)
				em.remove(s);
		}catch(Exception e){
			e.printStackTrace();
		}
		em.persist(LoggerAction.removeScoreType(user, st));
		em.remove(st);
	}

	/**
	 * Get score type by id
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public Scoretype getScoreTypeById(String targetId) throws Exception{
		return em.find(Scoretype.class, Integer.parseInt(targetId));
	}

	/**
	 * Edit update a score type with old createdate and old creator account
	 * @param user
	 * @param scoretype
	 * @throws Exception
	 */
	@Transactional
	public void updateScoreType(Account user, Scoretype scoretype) throws Exception{
		Scoretype st = getScoreTypeById(scoretype.getId()+"");
		scoretype.setCreatedate(st.getCreatedate());
		scoretype.setAccount(st.getAccount());
		em.merge(scoretype);
		em.persist(LoggerAction.editScoreType(user, scoretype));
	}

	/**
	 * Get newest 100 logs of given day
	 * @param logdate
	 * @return
	 * @throws Exception
	 */
	public List<ScoreLog> getScoreLogs(Date logdate) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(logdate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date prev = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, +2);
		Date now = c.getTime();
		return (List<ScoreLog>)em.createQuery("SELECT q FROM ScoreLog q WHERE createtime > ? AND createtime < ? ORDER BY createtime DESC")
		.setParameter(1, prev).setParameter(2, now).setMaxResults(100).getResultList();
				
	}

	/**
	 * Check whether scoremember is exist in the table, mapped by workerid
	 * @param workerid
	 * @return
	 * @throws Exception
	 */
	public boolean isScoreMemberExist(String workerid) throws Exception{
		try{
			Scoremember member = (Scoremember) em.createQuery("SELECT q FROM Scoremember q WHERE workerid=?")
					.setParameter(1, workerid).getSingleResult();
			if(member != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Get scoremember by score id, return null if no one found
	 * @param workerid
	 * @return
	 */
	public Scoremember getScoreMemberByWorkerid(String workerid) {
		try{
			Scoremember sm = (Scoremember) em.createQuery("SELECT q FROM Scoremember q WHERE workerid=?")
					.setParameter(1, workerid).getSingleResult();
			return sm;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Assign score type to scorer from emp,
	 * record entry by user
	 * @param user
	 * @param emp
	 * @param scorer
	 * @param st
	 * @throws Exception
	 */
	@Transactional
	public void assignScoreTypeToScoreMember(Account user, String emp,String scorer, Scoretype st, Date scoredate, Float score) throws Exception{
		Scoremember scorersm = getScoreMemberByWorkerid(scorer); //受分人
		Scoremember empsm = getScoreMemberByWorkerid(emp); //给分人
		Date today = Calendar.getInstance().getTime();
		Scorerecord record = new Scorerecord();
		st = getScoreTypeById(st.getId()+"");
		if(Scoretype.SCORE_TYPE_TEMP == st.getType() && (score == null || score == 0)){ //如果分值为0，侧采用默认的条例分值
			score = st.getScore();
			if(score == 0){
				throw new Exception("项目分值不能为0，请独立对0分值的条例打分。");
			}
		}
		record.setCreator(user);
		record.setCreatedate(today);
		record.setReceiver(scorersm);
		record.setSender(empsm);
		record.setScoretype(st);
		record.setScoredate(scoredate);
		record.setScore(score);
		record.setStatus(Scorerecord.WAITING);//初次新建，自动提交到审核页面
		
		em.persist(record);
		em.flush();
		
		//扣掉积分,以及部门积分
//		addScore(record); 
		takeAwayScore(record);
		
		if(isUserApprover(user.getEmployee())){//如果新建用户为审核人，侧直接通过审核，直接相加积分
			System.out.println("设为已审核");
			record.setStatus(Scorerecord.APPROVED);
			em.merge(record);
			em.flush();
			addScore(record); 
		}
		em.persist(LoggerAction.assignScoretype(user, record.getId(), st, scorersm.getEmployee()));
	}
	
	/**
	 * 重设部门分值
	 * @param record
	 * @throws Exception
	 */
	@Transactional
	public void toResetDepartmentScores(Employee scorer, Date createdate) throws Exception{
		if(HRUtil.isDateInCurrentWeek(createdate)){
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.WEEK_OF_YEAR, 1); 
			Long countRecords = (Long) em.createQuery("SELECT count(q) FROM Scorerecord q WHERE q.receiver.employee.department.id='" + scorer.getDepartment().getId()+"' " +
					" AND q.createdate >= '"+HRUtil.parseDateToString(cal.getTime())+"' AND q.createdate < '"+ HRUtil.parseDateToString(cal2.getTime())+"'")
					.getSingleResult();
//			System.out.println(countRecords + " Exists.");
			if(countRecords > 0)
				return;
			DepartmentScore ds = getDepartmentScore(scorer);
			String query = "SELECT count(q) FROM employee q LEFT JOIN scoreexceptionlist s ON q.positionid = s.positionid WHERE (s.status IS NULL OR (s.status IS NOT NULL AND s.status<>0)) AND q.departmentid="+ ds.getDepartment().getId()+" AND "
						+ "(q.joblevel='中管' OR q.joblevel='管') AND q.status='A'";
			BigInteger count = (BigInteger) em.createNativeQuery(query).getSingleResult();
//			System.out.println(ds.getDepartment().getName() + " 部门找到 "+ count+" 个");
			ds.setAvailable(count.intValue()*ds.getBasescore());
			em.merge(ds);
			em.flush();
		}
	}
	
	/**
	 * Count department scoring employee number
	 * @param departmentid
	 * @return
	 * @throws Exception
	 */
	public Integer getScoreEmployeeCount(int departmentid) throws Exception{
		String query = "SELECT count(q) FROM employee q LEFT JOIN scoreexceptionlist s ON q.positionid = s.positionid WHERE (s.status IS NULL OR (s.status IS NOT NULL AND s.status="+ScoreExceptionList.HAS_UPPER_SCORE_LIMIT+")) AND q.departmentid="+ departmentid+" AND "
				+ "(q.joblevel='中管' OR q.joblevel='管') AND q.status='A'";
		BigInteger count = (BigInteger) em.createNativeQuery(query).getSingleResult();
		return count.intValue();
	}

	/**
	 * 
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	private DepartmentScore getDepartmentScore(Employee employee) throws Exception {
		DepartmentScore ds = null;
		try{
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.vehicleteam.id=?")
					.setParameter(1, employee.getTeam().getId()).getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?")
					.setParameter(1, employee.getDepartment().getId()).getSingleResult();
		}
		return ds;
	}

	/**
	 * 减去已经用掉的积分
	 * @param record
	 * @throws Exception
	 */
	private void takeAwayScore(Scorerecord record) throws Exception{
		Scoretype st = record.getScoretype();
		if(st.getType() == Scoretype.SCORE_TYPE_TEMP){
			try{
				ScoreExceptionList excep = null;
				try{
					excep = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id=?")
						.setParameter(1, record.getReceiver().getEmployee().getPosition().getId()).getSingleResult();
					System.out.println("excep found."+excep.getPosition().getName());
				}catch(Exception e){
					System.out.println("Exception not found for position."+e.getMessage());
					excep = null;
				}
				if(excep != null && excep.getStatus() == ScoreExceptionList.NO_UPPER_SCORE_LIMIT){
					return;
				}
				Employee e = (Employee) em.find(Employee.class,record.getReceiver().getEmployee().getId());
				DepartmentScore ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?")
						.setParameter(1, e.getDepartment().getId()).getSingleResult();
				ds.setAvailable(ds.getAvailable() - record.getScore());
				em.merge(ds);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 为员工 添加分值/减去分值
	 * @param recordId
	 * @throws Exception
	 */
	public void addScore(Scorerecord record) throws Exception{
		//更新员工总积分
		Scoremember scorer = record.getReceiver();
		scorer.setHistorytotal(scorer.getHistorytotal() + record.getScore());
		em.merge(scorer);
				
		//更新月积分，如果该月积分还没开始统计，创建一个新的该月份的积分
		Scoresummary summary=this.getScoreSummary(scorer, record.getScoredate());
		Scoretype st = record.getScoretype();
		Float score = record.getScore();
		if(summary == null){ //没找到该月积分
			summary = new Scoresummary();
			summary.setEmployee(scorer.getEmployee());
			summary.setDate(record.getScoredate());
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
				summary.setPerformancescore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PROJECT)
				summary.setProjectscore(new Float(score));
			em.persist(summary);
			em.flush();
			em.persist(LoggerAction.createNewScoreSummary(record.getCreator(),summary));
		}else{ //找到该月积分，并相加
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(summary.getFixscore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(summary.getScore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
				summary.setPerformancescore(summary.getPerformancescore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PROJECT)
				summary.setProjectscore(summary.getProjectscore() + new Float(score));
			em.merge(summary);
			em.persist(LoggerAction.updateScoreSummary(record.getCreator(),summary));
		}
	}
	
	/**
	 * 用户是否审核人，需提供工号
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	public boolean isUserApprover(String workerid) throws Exception{
		Employee ep = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1, workerid).getSingleResult();
		try{
			Long approvers = (Long) em.createQuery("SELECT count(q) FROM Scoreapprover q WHERE q.approver=:emp AND q.isapprover='"+Scoreapprover.APPROVER+"'")
					.setParameter("emp", ep).getSingleResult();
			System.out.println("approver rights size:"+approvers);
			if(approvers > 0)
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove score record given the id of record
	 * @param user
	 * @param recordId
	 * @throws Exception
	 */
	@Transactional
	public void removeScoreTypeToScoreMember(Account user, String recordId) throws Exception{
		Scorerecord record = em.find(Scorerecord.class, Integer.parseInt(recordId));
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP && 
				HRUtil.isDateInCurrentWeek(record.getCreatedate())){
			//如果是临时积分，返回积分到部门总分，因为是当周非配的分值
			addBackDepartmentScores(record);
		}
		addBackMemberScore(record,user);
		em.persist(LoggerAction.removeScoreRecord(user,record));
		em.remove(record);
	}

	/**
	 * 返回条例的分值
	 * @param record
	 * @param user
	 * @throws Exception
	 */
	private void addBackMemberScore(Scorerecord record, Account user) throws Exception{
		Scoremember member = record.getReceiver();
		Float score = record.getScore();
		
		Scoresummary summary  =  this.getScoreSummary(record.getReceiver(), record.getScoredate());
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_FIX)
			summary.setFixscore(summary.getFixscore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP)
			summary.setScore(summary.getScore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
			summary.setPerformancescore(summary.getPerformancescore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PROJECT)
			summary.setProjectscore(summary.getProjectscore() - new Float(score));
		em.merge(summary);
		em.persist(LoggerAction.updateScoreSummary(user,summary));
		member.setHistorytotal(member.getHistorytotal() - new Float(score));
		em.merge(member);
	}

	/**
	 * 返回部门总积分
	 * @param record
	 * @throws Exception
	 */
	private void addBackDepartmentScores(Scorerecord record) throws Exception{
		ScoreExceptionList excep = null;
		try{
			excep = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id=?")
				.setParameter(1, record.getReceiver().getEmployee().getPosition().getId()).getSingleResult();
		}catch(Exception e){
			System.out.println("Exception not found for position."+e.getMessage());
			excep = null;
		}
		if(excep != null && excep.getStatus() == ScoreExceptionList.NO_UPPER_SCORE_LIMIT){
			return;
		}
		DepartmentScore ds = getDepartmentScore(record.getReceiver().getEmployee());
		ds.setAvailable(ds.getAvailable()+record.getScore());
		em.merge(ds);
	}

	/**
	 * Create the scoremember from existing employee, must have an
	 * employee profile
	 * @param employee
	 * @throws Exception
	 */
	@Transactional
	public void createScoreMember(Account user, Employee employee) throws Exception {
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND workerid=?").setParameter(1, employee.getFullname())
				.setParameter(2, employee.getWorkerid()).getSingleResult();
		Scoremember member = new Scoremember();
		member.setEmployee(e);
		member.setHistorytotal(0F);
		member.setMonthlyremain(0F);
		member.setMonthlyscore(0F);
		member.setMonthlytotal(0F);
		member.setVoucherscore(0L);
		em.persist(member);
		em.flush();
		em.persist(LoggerAction.createScoreMember(user, member));
	}

	/**
	 * get records for a given member and month
	 * @param member
	 * @param recordDate
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getRecords(Scoremember member, Date recordDate) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(recordDate);
		List<Scorerecord> records  = em.createQuery("SELECT q FROM Scorerecord q WHERE" +
				" receiverid=? AND EXTRACT(month FROM scoredate)=? AND q.status=?").setParameter(1, member.getEmployee().getWorkerid())
				.setParameter(2, c.get(Calendar.MONTH)+1).setParameter(3, Scorerecord.APPROVED).getResultList();
		return records;
	}

	/**
	 * Get scoretype by reference, return null if not exsit
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	public Scoretype getScoreTypeByReference(String ref) throws Exception{
		try{
			Scoretype st = (Scoretype) em.createQuery("SELECT q FROM Scoretype q WHERE reference=?")
					.setParameter(1, ref).getSingleResult();
			return st;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Get score summary from a scoredate and workerid
	 * @param workerid
	 * @param scoredate
	 * @return
	 */
	public Scoresummary getScoreSummary(Scoremember member, Date scoredate){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(scoredate);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH)+1;
			return (Scoresummary) em.createQuery("SELECT q FROM Scoresummary q WHERE workerid='"+member.getEmployee().getWorkerid()+"' AND EXTRACT(month FROM date)=? AND EXTRACT(year FROM date)=?")
			.setParameter(1, month).setParameter(2, year)
			.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Get score rank directly, returns a virtual table ScoreMemberRank data.<br/>
	 *   List<ScoreMemberRank>
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	public Map getSummaryByStatement(int pagenum, int lotsize, String statement) throws Exception{
		Map ret = new HashMap<String,Object>();
		List<ScoreMemberRank> list = null;
//		if(pagenum == -1 || lotsize == -1){
			list = em.createNativeQuery(statement,ScoreMemberRank.class).getResultList();
//		}else{
//			list = em.createNativeQuery(statement,ScoreMemberRank.class)
//				.setFirstResult(pagenum*lotsize -lotsize).setMaxResults(lotsize).getResultList();
//		}
		ret.put("list", list);
		ret.put("count", list.get(0).getCount());
		return ret;
	}

	/**
	 * Get the selected sheet infomation by its sheet id
	 * @param selectedSheet
	 * @return
	 */
	public Scoresheets getScoreSheetById(Integer selectedSheet) {
		try{
			return em.find(Scoresheets.class, selectedSheet);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Get all score sheets available in database
	 * @return
	 */
	public List<Scoresheets> getAllScoreSheets() throws Exception{
		return em.createQuery("SELECT q FROM Scoresheets q ORDER BY q.name").getResultList();
	}

	/**
	 * Create new score sheet and log, check sheet name existence first
	 * @param user
	 * @param sheet
	 * @throws Exception
	 */
	@Transactional
	public void createScoreSheet(Account user, Scoresheets sheet) throws Exception{
		if(isScoreSheetNameExist(sheet.getName()))
			return;
		em.persist(sheet);
		em.flush();
		em.persist(LoggerAction.createScoreSheet(user, sheet));
	}
	
	/**
	 * CHeck whether a score sheet name exsit
	 * @param name
	 * @return
	 */
	public boolean isScoreSheetNameExist(String name){
		try{
			List<Scoresheets> s = (List<Scoresheets>) em.createQuery("SELECT q FROM Scoresheets q WHERE name=?")
					.setParameter(1, name).getResultList();
			if(s.size() > 0)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Delete a score sheet and log
	 * @param user
	 * @param selectedSheet
	 * @throws Exception
	 */
	@Transactional
	public void removeScoreSheet(Account user, Integer selectedSheet) throws Exception{
		Scoresheets s = em.find(Scoresheets.class, selectedSheet);
		em.persist(LoggerAction.removeScoreSheet(user,s));
		em.remove(s);
	}

	/**
	 * remove score type from sheet, given both ids
	 * @param user
	 * @param parseInt
	 * @param id
	 */
	@Transactional
	public void removeScoreTypeFromSheet(Account user, Integer sheetId, Integer scoretypeId) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, sheetId);
		Scoretype st = em.find(Scoretype.class, scoretypeId);
		Scoresheetmapper mapper = (Scoresheetmapper) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE sheetid =:sheet AND scoretypeid=:st")
				.setParameter("sheet", sheet).setParameter("st", st).getSingleResult();
		em.persist(LoggerAction.removeScoreTypeFromSheet(user,sheet,st));
		em.remove(mapper);
	}

	/**
	 * CHeck if the score type already assign to sheet provided
	 * @param scoretypeId
	 * @param sheetId
	 * @return
	 */
	public boolean isScoretypeExistForSheet(Integer scoretypeId, int sheetId){
		try{
			Scoresheets sheet = em.find(Scoresheets.class, sheetId);
			Scoretype st = em.find(Scoretype.class, scoretypeId);
			Scoresheetmapper mapper = (Scoresheetmapper) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE sheetid =:sheet AND scoretypeid=:st")
					.setParameter("sheet", sheet).setParameter("st", st).getSingleResult();
			if(mapper != null)
				return true;
			return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Assign score type to a score sheet
	 * @param user
	 * @param id
	 * @param itemlist
	 */
	@Transactional
	public void assignScoreTypeToSheet(Account user, Integer scoretypeId, int sheetId) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, sheetId);
		Scoretype st = em.find(Scoretype.class, scoretypeId);
		Scoresheetmapper mapper = new Scoresheetmapper();
		mapper.setSheet(sheet);
		mapper.setType(st);
		em.persist(mapper);
		em.flush();
		em.persist(LoggerAction.assignScoreTypeToSheet(user, st, sheet));
	}

	/**
	 * Get positions belongs to given group
	 * @return
	 */
	public List<Position> getGroupedPositions(Integer groupid) throws Exception {
		List<Positiongroup> posg = em.createQuery("SELECT q FROM Positiongroup q WHERE" +
				" scoregroupid=? ORDER BY q.position.name").setParameter(1, groupid).getResultList();
		List<Position> pos = new ArrayList<Position>();
		for(Positiongroup g:posg){
			pos.add(g.getPosition());
		}
		return pos;
	}

	/**
	 * Create score group
	 * @param group
	 * @throws Exception
	 */
	@Transactional
	public void saveGroup(Account user, Scoregroup group) throws Exception{
		em.persist(group);
		em.flush();
		em.persist(LoggerAction.createScoreGroup(user, group));
	}

	/**
	 * 
	 * @param positionid
	 * @param groupSelected
	 * @return
	 */
	public boolean isGroupedPositionExist(Integer positionid, Integer groupSelected) throws Exception {
		try{
			Positiongroup pg = (Positiongroup) em.createQuery("SELECT q FROM Positiongroup q WHERE " +
					" scoregroupid=? AND positionid=?").setParameter(1, groupSelected).setParameter(2, positionid).getSingleResult();
			if(pg != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 
	 * @param i
	 * @param groupSelected
	 */
	@Transactional
	public void assignPosToGroup(Account user, Integer posid, Integer groupSelected) throws Exception{
			Position p = em.find(Position.class, posid);
			Scoregroup sg = em.find(Scoregroup.class, groupSelected);
			Positiongroup pg = new Positiongroup();
			pg.setPosition(p);
			pg.setScoreGroup(sg);
			em.persist(pg);
			em.flush();
			em.persist(LoggerAction.assginToScoreGroup(user, pg));
	}

	/**
	 * 
	 * @param user
	 * @param posid
	 * @param groupSelected
	 * @throws Exception
	 */
	@Transactional
	public void quitPosFromGroup(Account user, Integer posid, Integer groupSelected) throws Exception{
		Position p = em.find(Position.class, posid);
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		Positiongroup pg = (Positiongroup) em.createQuery("SELECT q FROM Positiongroup q WHERE " +
				" q.position=:p AND q.scoreGroup=:sg").setParameter("p", p).setParameter("sg",sg).getSingleResult();
		em.persist(LoggerAction.quitScoreGroup(user, pg));
		em.remove(pg);
	}

	/**
	 * Get all scoring positions' group
	 * @return
	 */
	public List<Scoregroup> getAllScoreGroup() throws Exception{
		List<Scoregroup> list = em.createQuery("SELECT q FROM Scoregroup q ORDER BY q.id").getResultList();
		return list;
	}

	/**
	 * Edit the group information for created group
	 * @param user
	 * @param groupSelected
	 * @param editGroup
	 */
	@Transactional
	public void editGroup(Account user, Integer groupSelected,
			Scoregroup editGroup) throws Exception{
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		if(editGroup.getName() != null && editGroup.getName().trim() != ""){
			sg.setName(editGroup.getName());
		}
		if(editGroup.getRemark() != null && editGroup.getRemark().trim() != ""){
			sg.setRemark(editGroup.getRemark());
		}
		em.merge(sg);
		em.persist(LoggerAction.editScoreGroupDetail(user, sg));
	}

	/**
	 * Remove the score group, only if no positions are assigned
	 * @param user
	 * @param groupSelected
	 */
	@Transactional
	public void removeScoreGroup(Account user, Integer groupSelected) throws Exception{
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		em.persist(LoggerAction.removeScoreGroup(user,sg));
		em.remove(sg);
	}

	/**
	 * Get vouchers from company and make records
	 * @param user
	 * @param workerid
	 * @param vl
	 */
	@Transactional
	public void giveVouchers(Account user, Voucherlist vl) throws Exception{
		Scoremember sm = vl.getScoremember();
		sm.setVoucherscore(sm.getVoucherscore() + new Long(vl.getScore()));
		em.merge(sm);
		em.persist(vl);
		em.flush();
		em.persist(LoggerAction.giveVoucher(user,vl));
	}

	/**
	 * Get voucher records by worker's workerId
	 * @param filterworkerid
	 * @return
	 */
	public List<Voucherlist> getVouchersByWorkerId(String filterworkerid) throws Exception {
		return em.createQuery("SELECT q FROM Voucherlist q WHERE workerid=? ORDER BY date DESC")
				.setParameter(1, filterworkerid).getResultList();
	}

	/**
	 * Get voucher records by worker's name
	 * @param filtername
	 * @return
	 */
	public List<Voucherlist> getVouchersByName(String filtername) throws Exception{
		return em.createQuery("SELECT q FROM Voucherlist q WHERE q.scoremember.employee.fullname LIKE '%"+filtername+"%' ORDER BY date DESC")
				.getResultList();
	}

	/**
	 * Delete voucher record given the voucher record id
	 * @param targetId
	 * @throws Exception
	 */
	@Transactional
	public void deleteVoucherRecord(Account user, Integer targetId) throws Exception{
		Voucherlist vl = em.find(Voucherlist.class, targetId);
		Scoremember sm = vl.getScoremember();
		sm.setVoucherscore(sm.getVoucherscore() - new Long(vl.getScore()));
		em.merge(sm);
		em.persist(LoggerAction.deleteVoucherRecord(user, vl));
		em.remove(vl);
	}

	/**
	 * 添加新的审核人到部门或车队
	 * @param newApprover
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void addApprover(Scoreapprover newApprover, Account user) throws Exception{
		try{
			if(newApprover.getApprover().getWorkerid() != null){
				Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?")
						.setParameter(1, newApprover.getApprover().getWorkerid()).getSingleResult();
				newApprover.setApprover(e);
				if(!isApproverExist(newApprover)){
					em.persist(newApprover);
					em.flush();
					em.persist(LoggerAction.addApprover(user, newApprover));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查审核人有没有已经分配到该车队或部门。优先检查车队
	 * @param approver
	 * @return
	 */
	public boolean isApproverExist(Scoreapprover approver) {
		try{
			List<Scoreapprover> sas = null;
			if(approver.getVehicleteam() != null){
				sas = em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.id=? AND q.vehicleteam.id=?")
				.setParameter(1, approver.getApprover().getId()).setParameter(2, approver.getVehicleteam().getId())
				.getResultList();
			}else{
				sas = em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.id=? AND q.department.id=?")
						.setParameter(1, approver.getApprover().getId()).setParameter(2, approver.getDepartment().getId())
						.getResultList();
			}
			if(sas != null && sas.size()>0){
				return true;
			}else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取审核人的管核范围
	 * @param e
	 * @return
	 */
	public List<Scoreapprover> getApproverSections(Employee e) throws Exception{
		return em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.id="+e.getId() +" ORDER BY id ASC").getResultList();
	}

	/**
	 * 删除审核人被允许审核的部门或者车队的记录
	 * @param targetId
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void delApproverSection(String targetId,Account user) throws Exception{
		Scoreapprover sa = em.find(Scoreapprover.class,Integer.parseInt(targetId));
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.DELETE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setRecordid(sa.getId()+"");
		sl.setWho(user);
		sl.setRemark("删除了"+sa.getClass().getName());
		em.persist(sl);
		em.remove(sa);
	}

	/**
	 * 更改审核人状态，只修改是否为改部门的审核人
	 * @param targetId
	 * @param user
	 */
	@Transactional
	public void toggleApproverStatus(String targetId, Account user) {
		Scoreapprover sa = em.find(Scoreapprover.class,Integer.parseInt(targetId));
		if(sa.getIsapprover() == null){
			sa.setIsapprover("N");
		}else if(sa.getIsapprover().equals("N")){
			sa.setIsapprover("Y");
		}else if(sa.getIsapprover().equals("Y")){
			sa.setIsapprover("N");
		}
		em.merge(sa);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setRecordid(sa.getId()+"");
		sl.setWho(user);
		sl.setRemark("更改了"+sa.getClass().getName()+" ID"+sa.getId()+" 的状态到"+sa.getIsapprover());
		sl.setCreatetime(Calendar.getInstance().getTime());
		em.persist(sl);
	}

	/**
	 * 获取审核人名单，主要是看名单和管核部门的统计
	 * @return
	 */
	public List<Scoreapprover> getDistinctApprovers() throws Exception{
		return em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.status='A' ORDER BY q.approver.fullname").getResultList();
	}

	/**
	 * 计数 等待审核的条例数目
	 * @param user
	 * @return
	 */
	public Long countWaittingApprove(Account user) throws Exception{
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1,user.getEmployee()).getSingleResult();
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" WHERE e.departmentid " +
				" IN (SELECT departmentid FROM scoreapprover WHERE approver = " + e.getId() +") AND re.status="+Scorerecord.WAITING).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 获取 某周期内的审核条例详细资料，仅限指定审核人的条例
	 * @param user
	 * @param selectPeriod
	 * @return
	 */
	public List<Scorerecord> getWaittingApprove(Account user,
			String selectPeriod) throws Exception{
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1,user.getEmployee()).getSingleResult();
		List<Scorerecord> departs = new ArrayList<Scorerecord>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		if(selectPeriod == null)
			selectPeriod = "w"; //Default display week records
		if(selectPeriod == null){
			departs = em.createQuery("SELECT re FROM Scorerecord re" +
					" WHERE re.receiver.employee.department.id " +
					" IN (SELECT q.department.id FROM Scoreapprover q WHERE q.approver.id = " + e.getId() +") " +
							" AND re.createdate='"+HRUtil.parseDateToString(new Date())+"' " +
							" AND re.status=1 " +
									" ORDER BY re.id DESC")
					.getResultList();
		}else if(selectPeriod.equals("w".toLowerCase())){
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.WEEK_OF_YEAR, 1);
			departs = em.createQuery("SELECT re FROM Scorerecord re" +
					" WHERE re.receiver.employee.department.id " +
					" IN (SELECT q.department.id FROM Scoreapprover q WHERE q.approver.id = " + e.getId() +") " +
							" AND re.createdate>='" + HRUtil.parseDateToString(cal.getTime())+"' AND re.createdate<'" +  HRUtil.parseDateToString(cal2.getTime()) + "' " +
							" AND re.status=1 " +
							" ORDER BY re.id DESC").getResultList();
		}else if(selectPeriod.equals("m".toLowerCase())){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.MONTH, 1);
			departs = em.createQuery("SELECT re FROM Scorerecord re" +
					" WHERE re.receiver.employee.department.id " +
					" IN (SELECT q.department.id FROM Scoreapprover q WHERE q.approver.id = " + e.getId() +") " +
							" AND re.createdate>='" + HRUtil.parseDateToString(cal.getTime())+"' AND re.createdate<'" +  HRUtil.parseDateToString(cal2.getTime()) + "' " +
							" AND re.status=1 " +
							" ORDER BY re.id DESC").getResultList();
		}else if(selectPeriod.equals("a".toLowerCase())){
			departs = em.createQuery("SELECT re FROM Scorerecord re" +
					" WHERE re.receiver.employee.department.id " +
					" IN (SELECT q.department.id FROM Scoreapprover q WHERE q.approver.id = " + e.getId() +") " +
					" AND re.status=1 " +
							" ORDER BY re.id DESC").getResultList();
		}
		return departs;
	}

	/**
	 * 审核已经提交的积分项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void approveScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setStatus(Scorerecord.APPROVED);
			em.merge(se);
			addScore(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"审核了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 拒绝不合格的积分项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void rejectScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setStatus(Scorerecord.REJECTED);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"拒绝了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 统计用户创建的有多少条项目是正在等待审批的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countWaitingApproveRecords(Account user) throws Exception {
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.WAITING).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 统计用户创建的有多少条项目是被拒绝的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countRejectedRecords(Account user) throws Exception{
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.REJECTED).getSingleResult();
		return depart.longValue();
	}
	
	/**
	 * 统计用户创建的有多少条项目是未提交审核的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countCreatedRecords(Account user) throws Exception{
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.CREATED).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 获取用户创建的正在审核的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getWaitingApproveRecords(Account user) throws Exception{
		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY createdate DESC,q.receiver.employee.fullname")
				.setParameter(1, user.getId()).setParameter(2, Scorerecord.WAITING).getResultList();
		return records;
	}

	/**
	 * 获取用户创建的拒绝了的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getRejectedRecords(Account user) throws Exception{
		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY createdate DESC,q.receiver.employee.fullname")
				.setParameter(1, user.getId()).setParameter(2, Scorerecord.REJECTED).getResultList();
		return records;
	}
	
	/**
	 * 获取用户创建的未提交的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getCreatedRecords(Account user) throws Exception{
		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY createdate DESC,q.receiver.employee.fullname")
				.setParameter(1, user.getId()).setParameter(2, Scorerecord.CREATED).getResultList();
		return records;
	}

	/**
	 * 删除审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void deleteScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			if(se.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP && 
					HRUtil.isDateInCurrentWeek(se.getCreatedate())){
				//如果是临时积分，返回积分到部门总分，因为是当周非配的分值
				addBackDepartmentScores(se);
			}
			if(se.getStatus() == Scorerecord.APPROVED)
				addBackMemberScore(se,user);
			em.remove(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.DELETE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"删除了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 撤回审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void getBackScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setStatus(Scorerecord.CREATED);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"撤回了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 重新提交审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void reSubmitScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setCreatedate(Calendar.getInstance().getTime());
			se.setStatus(Scorerecord.WAITING);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"提交了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 计算现存分值够不够分配
	 * List[0][0] = 部门名字
	 * List[0][1] = 扣除后剩余分值
	 * @param totalscore
	 * @param workerids
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getDepartmentScores(Float totalscore,
			String workerids) throws Exception{
		List<Object[]> result = em.createNativeQuery("SELECT q.name,s.available-count(e)*" + totalscore +" AS result " +
				" FROM department q LEFT JOIN departmentscores s ON q.id=s.departmentid RIGHT JOIN employee e ON q.id=e.departmentid LEFT JOIN scoreexceptionlist  p ON e.positionid=p.positionid " +
				" WHERE e.workerid IN ("+workerids+") " +
				" AND (p.status IS NULL OR (p.status IS NOT NULL AND p.status=" + ScoreExceptionList.HAS_UPPER_SCORE_LIMIT+ "))  " +
				" GROUP BY q.name,s.available;").getResultList();
		List<List<String>> res = new ArrayList<List<String>>();
		for(Object[] objs:result){
			List<String> strlist = new ArrayList<String>();
//			System.out.println(objs[0] + " scores "+ objs[1]);
			strlist.add((String)objs[0]);
			strlist.add((Double)objs[1]+"");
			res.add(strlist);
		}
		return res;
	}

	/**
	 * 创建部门基础分和初次分配分值
	 * @param ds
	 * @throws Exception
	 */
	@Transactional
	public void addDepartmentScore(DepartmentScore ds) throws Exception{
		em.persist(ds);
	}

	/**
	 * Return "" if no errors, else will be written inside the returned string.
	 * <br/> need 7 columns + 1 optional
	 * <br/>id, scoredate, reference, nameOfSender, workeridOfSender, nameOfReceiver, workeridOfReceiver, [score]
	 * @param scoreExcelFileProcessor
	 * @param hrBean
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public String saveMassScores(ScoreExcelFileProcessor saver, HRBean hrBean,
			Account user) throws Exception{
		String str = "";
		saver.hasNextLine();
		while (saver.hasNextLine()) {
			String[] cols = saver.strLine.split("\t");
			if (cols.length < 7) {
//				String num = "N.A.";
//				if (cols.length > 1)
//					num = cols[0];
//				str += "第" + saver.index + "行" + "录入失败,id " + num;
//				throw new Exception(str);
				continue;
			}
				Date scoredate = HRUtil.parseDate(cols[1], "yyyy-MM-dd");
				Scoretype st = getScoreTypeByReference(cols[2]);
				if(st ==null){
					str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".条例编号不存在."  + "\n<br/>";
					throw new Exception(str);
				}
				
				if(!isScoreMemberExist(cols[4])){
					if(hrBean.isEmployeeWorkerIdExist(cols[4])){
						Employee e = hrBean.getEmployeeByWorkerId(cols[4]);
						createScoreMember(user,e);
					}else{
						str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".工号不存在"+cols[4]+"."  + "\n<br/>";
						throw new Exception(str);
					}
				}
				if(!isScoreMemberExist(cols[6])){
					if(hrBean.isEmployeeWorkerIdExist(cols[6])){
						Employee scorer = hrBean.getEmployeeByWorkerId(cols[6]);
						createScoreMember(user,scorer);
					}else{
						str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".工号不存在"+cols[6]+"."  + "\n<br/>";
						throw new Exception(str);
					}
				}
				Float score= 0F;
				if(cols.length > 7 && !cols[7].trim().equals("")){
					System.out.println(st.getDescription() + " 分值:"+st.getScore());
					if(st.getScore() == 0){
						score = Float.parseFloat(cols[7]);
					}else{
						score = st.getScore();
					}
				}else{
					score = st.getScore();
				}
				if(score <= 0 || score > 1000){
					str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".分值不能为0 或 过大.\n<br/>";
					throw new Exception(str);
				}
				
				Employee scorer = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?")
						.setParameter(1, cols[6]).getSingleResult();
				
				//检查是否员工可以打分的,审核人可以直接打分
				Employee curUser = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q.workerid=?").setParameter(1, user.getEmployee()).getSingleResult();
				if(!isUserScoreApprover(curUser)){
					if(!checkEmployeeAllowToScore(scorer, curUser)){
						str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".用户"+ curUser.getFullname()+ "没有权限打分给用户"+ scorer.getFullname()+ "\n<br/>";
						throw new Exception(str);
					}
				}
				
				//检查是否这个部门今周的第一条奖分，是的话重设管理人员数目*部门基础分的总分值
				toResetDepartmentScores(scorer,Calendar.getInstance().getTime());
				
				//这里需要检查部门够不够分值打分，不够的话出错
				if(st.getType() == Scoretype.SCORE_TYPE_TEMP && !isDepartmentScoreEnoughForEmployee(scorer,score)){
					str += "" + "第" + saver.index + "行" + "录入失败,序号 " + cols[0]+".部门分值不够."  + "\n<br/>";
					throw new Exception(str);
				}
				assignScoreTypeToScoreMember(user, cols[4], cols[6], st, scoredate,score);
		}
		return str;
	}

	/**
	 * 
	 * @param records
	 * @param user
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveMassScoresFromList(List<Scorerecord> records, Account user,HRBean hrBean) throws Exception{
		for(Scorerecord sr:records){
			//检查打分人是否存在
			if(!isScoreMemberExist(sr.getSender().getEmployee().getWorkerid())){
				if(hrBean.isEmployeeWorkerIdExist(sr.getSender().getEmployee().getWorkerid())){
					Employee e = hrBean.getEmployeeByWorkerId(sr.getSender().getEmployee().getWorkerid());
					createScoreMember(user,e);
				}else{
					throw new Exception("工号不存在:"+sr.getSender().getEmployee().getWorkerid());
				}
			}
			//检查受分人是否存在
			if(!isScoreMemberExist(sr.getReceiver().getEmployee().getWorkerid())){
				if(hrBean.isEmployeeWorkerIdExist(sr.getReceiver().getEmployee().getWorkerid())){
					Employee scorer = hrBean.getEmployeeByWorkerId(sr.getReceiver().getEmployee().getWorkerid());
					createScoreMember(user,scorer);
				}else{
					throw new Exception("工号不存在:"+sr.getReceiver().getEmployee().getWorkerid());
				}
			}
			
			//查找条例
			Scoretype st = getScoreTypeByReference(sr.getScoretype().getReference());
			if(st == null)
				throw new Exception("条例编号不存在:"+sr.getScoretype().getReference());
			
			//设置分值
			Float score= 0F;
			if(st.getType() == Scoretype.SCORE_TYPE_TEMP){
				if(sr.getScore() != null && sr.getScore() == 0)
					continue;
			}
			if(st.getScore() == 0){
				score = sr.getScore();
			}else{
				score = st.getScore();
			}
			if(score == null)
				score = 0F;
			if(st.getType() == Scoretype.SCORE_TYPE_TEMP && score == 0){
				continue;
			}
			if(st.getType() == Scoretype.SCORE_TYPE_TEMP && (score < 0 || score > 1000)){
				throw new Exception("分值不能为0 或 过大");
			}

			Employee scorer = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?")
					.setParameter(1, sr.getReceiver().getEmployee().getWorkerid()).getSingleResult();
			
			//检查是否员工可以打分的,审核人可以直接打分
			Employee curUser = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q.workerid=?").setParameter(1, user.getEmployee()).getSingleResult();
			if(!isUserScoreApprover(curUser)){
				if(!checkEmployeeAllowToScore(scorer, curUser)){
					throw new Exception("用户"+ curUser.getFullname()+ "没有权限打分给用户"+ scorer.getFullname());
				}
			}
			
			//检查是否这个部门今周的第一条奖分，是的话重设管理人员数目*部门基础分的总分值
			toResetDepartmentScores(scorer,Calendar.getInstance().getTime());
			
			//这里需要检查部门够不够分值打分，不够的话出错
			if(st.getType() == Scoretype.SCORE_TYPE_TEMP && !isDepartmentScoreEnoughForEmployee(scorer,score)){
				throw new Exception("部门分值不够."+scorer.getDepartment().getName());
			}
			assignScoreTypeToScoreMember(user, curUser.getWorkerid(), 
					sr.getReceiver().getEmployee().getWorkerid(), st, sr.getScoredate(),score);
		}
	}
	
	/**
	 * 检查员工部门是否有足够的分值飞给该员工
	 * @param scorer
	 * @param score
	 * @return
	 * @throws Exception
	 */
	private boolean isDepartmentScoreEnoughForEmployee(Employee scorer,
			Float score) throws Exception{
		if(isScorerUnlimited(scorer))
			return true;
		DepartmentScore ds = getDepartmentScore(scorer);
		Float result = ds.getAvailable() - score;
		if(result < 0)
			return false;
		return true;
	}

	/**
	 * 检查受分人是否没上限
	 * @param scorer
	 * @return
	 */
	public boolean isScorerUnlimited(Employee scorer){
		try{
			ScoreExceptionList se = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id="+scorer.getPosition().getId()+" AND q.status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT).getSingleResult();
			if(se != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 根据时间获取该用户提交的并且已经通过审核的条例。
	 * @param user
	 * 用户
	 * @param selectPeriod
	 * null 为今天
	 * <br/>w为周
	 * <br/>m为月
	 * <br/>a为自选时间段
	 * @param startDate
	 * 当选择为a时才使用，开始日期
	 * @param endDate
	 * 当选择为a时才使用，结束日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Scorerecord> getApprovedListByTime(Account user,String selectPeriod, Date startDate, Date endDate) throws Exception {
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Employee e = (Employee) em.createQuery("SELECT distinct q FROM Employee q WHERE q.workerid=?").setParameter(1, user.getEmployee()).getSingleResult();
		
		if(selectPeriod == null || selectPeriod.equals(""))
			selectPeriod = "w"; // 设置默认获取本周的记录
		
		if(selectPeriod == null || selectPeriod.equals("")){
			return em.createQuery("SELECT distinct q FROM Scorerecord q, Scoreapprover d WHERE ((q.receiver.employee.department.id=d.department.id AND d.approver.id=?) OR q.creator.id=?) AND q.status=? AND q.createdate='"+ HRUtil.parseDateToString(cal.getTime())+"'")
					.setParameter(1, e.getId()).setParameter(2, user.getId()).setParameter(3, Scorerecord.APPROVED).getResultList();
		}else{
			if(selectPeriod.equals("w")){
				cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
				cal2.setTime(cal.getTime());
				cal2.add(Calendar.WEEK_OF_YEAR, 1);
			}else if(selectPeriod.equals("m")){
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal2.setTime(cal.getTime());
				cal2.add(Calendar.MONTH, 1);
			}else if(selectPeriod.equals("a")){
				if(startDate != null)
					cal.setTime(startDate);
				if(endDate != null)
					cal2.setTime(endDate);
			}
			return em.createQuery("SELECT distinct re FROM Scorerecord re , Scoreapprover d" +
						" WHERE ((re.receiver.employee.department.id=d.department.id AND d.approver.id=?) OR re.creator.id=? )"+
								" AND re.createdate>='" + HRUtil.parseDateToString(cal.getTime())+"' AND re.createdate<'" +  HRUtil.parseDateToString(cal2.getTime()) + "' " +
								" AND re.status=? " +
								" ORDER BY re.id DESC DESC")
								.setParameter(1, e.getId()).setParameter(3, Scorerecord.APPROVED).setParameter(2, user.getId()).getResultList();
		}
	}

	/**
	 * 获取当月月供得分详情
	 * @param user
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public List<Scoresummary> getDepartmentScoreSummary(Account user,Integer month,Integer departmentId) throws Exception{
		List<Scoresummary> summary  = em.createQuery("SELECT q FROM Scoresummary q WHERE q.employee.department.id=? AND" +
				" EXTRACT(month FROM q.date)=? ORDER BY q.score DESC").setParameter(1, departmentId).setParameter(2, month).getResultList();
		return summary;
	}

	/**
	 * 用用户账号或者工号来获取部门得分
	 * @param user
	 * @param workerid
	 * @return
	 */
	public DepartmentScore getDepartmentScoresByDepartmetnIdORWorkerId(Integer dsId, String workerid) throws Exception{
		DepartmentScore ds = null;
		if(dsId != null){
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?").setParameter(1, dsId).getSingleResult();
		}else{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q.workerid=?").setParameter(1, workerid).getSingleResult();
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?").setParameter(1, e.getDepartment().getId()).getSingleResult();
		}
		return ds;
	}

	/**
	 * 获取部门时间段内的详细审核了的项目
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public List<Scorerecord> getApprovedListByTimeForDepartment(Integer id, Date time, Date time2) throws Exception{
		List<Scorerecord> records = em.createNativeQuery("SELECT q.* FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" WHERE e.departmentid="+id+ " AND q.status="+Scorerecord.APPROVED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' " +
				" ORDER BY q.receiverid", Scorerecord.class).getResultList();
		return records;
	}

	/**
	 * 添加职位过滤,积分制里的有些职位不用参加积分制，有些没有上限
	 * @param exception
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void addPositionException(ScoreExceptionList exception, Account user) throws Exception{
		em.persist(exception);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.CREATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setRecordid(exception.getId()+"");
		sl.setWho(user);
		sl.setRemark("添加了一个职位过滤");
		em.persist(sl);
	}

	/**
	 * 获取所有过滤的职位信息
	 * @return
	 * @throws Exception
	 */
	public List<ScoreExceptionList> getAllPositionExceptions() throws Exception{
		return em.createQuery("SELECT q FROM ScoreExceptionList q ORDER BY q.position.name").getResultList();
	}

	/**
	 * 检查员工是否可以打分给指定员工
	 * @param e
	 * @param curUser
	 * @return
	 */
	public boolean checkEmployeeAllowToScore(Employee e, Employee curUser) {
		Long count =  (Long) em.createQuery("SELECT count(q) FROM Scoreapprover q WHERE q.department.id=? AND q.approver.id=?")
				.setParameter(1, e.getDepartment().getId()).setParameter(2, curUser.getId()).getSingleResult();
		if(count >= 1)
			return true;
		else 
			return false;
	}

	/**
	 * 检查用户是否打分的审核人
	 * @param curUser
	 * @return
	 * @throws Exception
	 */
	public boolean isUserScoreApprover(Employee curUser) throws Exception{
		String query = "SELECT count(q) FROM Scoreapprover q WHERE q.approver.id="+curUser.getId()+" AND q.isapprover='"+Scoreapprover.APPROVER+"'";
		Long count  = (Long) em.createQuery(query).getSingleResult();
//		System.out.println("is user score approver count size:"+count + " Query:"+query);
		if(count >=1)
			return true;
		else
			return false;
	}

	/**
	 * 获取用户所在 和所管核的部门
	 * @param user
	 * @return
	 */
	public List<Department> getAllManageDeparmentsByUser(Account user) throws Exception{
		String query = "select distinct d.* from employee e,department d, scoreapprover s where e.id=s.approver AND e.workerid='"+ user.getEmployee()
					+"' AND (d.id=s.departmentid OR d.id=e.departmentid);";
		return em.createNativeQuery(query, Department.class).getResultList();
	}

	public List<DepartmentScore> getAllDepartmentScores() throws Exception{
		return em.createQuery("SELECT q FROM DepartmentScore q ORDER BY q.id").getResultList();
	}

	/**
	 * 更新部门可用总分
	 * @param depS
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateDepartmentScores(List<DepartmentScore> depS) throws Exception{
		for(DepartmentScore ds:depS){
			DepartmentScore d = em.find(DepartmentScore.class, ds.getId());
			d.setBasescore(ds.getBasescore());
			d.setAvailable(ds.getAvailable());
			em.merge(d);
		}
	}

	/**
	 * 获取部门等待审核的积分总和
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentWaittingScores(Integer id, Date time, Date time2) throws Exception{
//		List<Integer> list = em.createNativeQuery("SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT).getResultList();
		String query = "SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" WHERE e.departmentid="+id+ " AND q.status="+Scorerecord.WAITING+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' " +
						"AND e.positionid NOT IN (SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT+")"; 
		Float bint = (Float) em.createNativeQuery(query).getSingleResult();
		return bint;
	}

	/**
	 * 获取部门被拒绝的条例总分
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentRejectedScores(Integer id, Date time, Date time2) throws Exception{
		Float bint = (Float) em.createNativeQuery("SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" WHERE e.departmentid="+id+ " AND q.status="+Scorerecord.REJECTED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' ").getSingleResult();
		return bint;
	}
	
	/**
	 * 获取部门未提交的条例总分
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentNotSubmitScores(Integer id, Date time, Date time2) throws Exception{
		Float bint = (Float) em.createNativeQuery("SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" WHERE e.departmentid="+id+ " AND q.status="+Scorerecord.CREATED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' "+
				" AND e.positionid NOT IN (SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT+")"
				).getSingleResult();
		return bint;
	}

	/**
	 * 获取积分组的最开始父类组
	 * @return
	 */
	public List<ScoreDivGroup> getParentScoreGroup() throws Exception{
		return em.createNativeQuery("SELECT * FROM score_group WHERE pid is NULL ORDER BY name DESC",ScoreDivGroup.class).getResultList();
	}

	/**
	 * 添加积分组
	 * @param scoreNewGroup
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addScoreDivGroup(ScoreDivGroup scoreNewGroup,Account user) throws Exception{
		if(scoreNewGroup.getName() == null)
			throw new Exception("没有提供组名字");
		scoreNewGroup.setLastUpdateDate(new Date());
		em.persist(scoreNewGroup);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setWho(user);
		sl.setRecordid(scoreNewGroup.getId()+"");
		sl.setAction(ScoreLog.CREATE);
		sl.setRemark("添加新的积分组");
		sl.setCreatetime(Calendar.getInstance().getTime());
		em.persist(sl);
	}

	/**
	 * 给一个积分组，获取它的子组合子组员工
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public EmpDepartments getAllChildrenAndEmployeeForScoreGroup(
			EmpDepartments e) throws Exception{
		String query = "select sg.id as sgid,sg.name as sdname ,e.id as eid,e.fullname as efullname from score_group sg left join score_group_mapper sgm on sg.id=sgm.scoregroupid left join employee e on sgm.empid=e.id " +
				" WHERE sg.pid=" + e.getDeptId() + " order by sg.id";
		List<Object[]> results = em.createNativeQuery(query).getResultList();
		List<EmpDepartments> extras = new ArrayList<EmpDepartments>();
		List<Employee> empList = new ArrayList<Employee>();
		EmpDepartments group = new EmpDepartments();
		for(Object[] obj:results){
			Integer id = (Integer) obj[0];
			if(id.toString().equals(group.getDeptId())){
				if(obj[2] != null){
					Employee emp = new Employee();
					emp.setId((Integer)obj[2]);
					emp.setFullname((String) obj[3]);
					empList.add(emp);
				}
			}else{
				if(group != null && group.getDeptId()!=null && !group.getDeptId().trim().equals("")){
					if(empList.size()>0)
						group.setEmps(empList);
					extras.add(group);
				}
				group = new EmpDepartments();
				group.setDeptId(id.toString());
				group.setDept((String) obj[1]);
				empList = new ArrayList<Employee>();
				if(obj[2] != null){
					Employee emp = new Employee();
					emp.setId((Integer)obj[2]);
					emp.setFullname((String) obj[3]);
					empList.add(emp);
				}
			}
		}
		//添加最后一行记录
		if(group != null && group.getDeptId()!=null && !group.getDeptId().trim().equals("")){
			if(empList.size()>0)
				group.setEmps(empList);
			extras.add(group);
		}
		if(extras.size()>0)
			e.setExtras(extras);
		return e;
	}

	/**
	 * 删除一个积分组和组成员
	 * @param scoreNewGroup
	 * @param user
	 */
	@Transactional
	public void delScoreDivGroup(String gid, Account user) throws Exception {
		Integer id = Integer.parseInt(gid);
		ScoreDivGroup sdg = em.find(ScoreDivGroup.class, id);
		//创建删除记录
		ScoreLog sl = new ScoreLog();
		sl.setWho(user);
		sl.setAction(ScoreLog.CREATE);
		sl.setRemark("删除了组:"+sdg.getName());
		sl.setCreatetime(Calendar.getInstance().getTime());
		
		em.createNativeQuery("DELETE FROM score_group_mapper WHERE scoregroupid="+id).executeUpdate();
		em.createNativeQuery("DELETE FROM score_group WHERE pid="+id).executeUpdate();
		em.createNativeQuery("DELETE FROM score_group WHERE id="+id).executeUpdate();
		
		em.persist(sl);
	}
}
