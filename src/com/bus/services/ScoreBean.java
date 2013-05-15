package com.bus.services;

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
import com.bus.dto.Employee;
import com.bus.dto.logger.ScoreLog;
import com.bus.dto.score.ScoreMemberRank;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoresheetmapper;
import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoresummary;
import com.bus.dto.score.Scoretype;
import com.bus.util.LoggerAction;


public class ScoreBean {

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

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
			list = em.createQuery("SELECT q FROM Scoretype q").getResultList();
		else
			list = em.createQuery("SELECT q FROM Scoretype q")
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
		for(Scoresheetmapper mapper:sheet.getScoremapper()){
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
			pagenum =1;lotsize = 20;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String countstatement = "SELECT count(q) " + statement.substring(statement.indexOf("FROM"));
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
	public void assignScoreTypeToScoreMember(Account user, String emp,String scorer, Scoretype st, Date scoredate, Integer score) throws Exception{
		Scoremember scorersm = getScoreMemberByWorkerid(scorer);
		Scoremember empsm = getScoreMemberByWorkerid(emp);
		Date today = Calendar.getInstance().getTime();
		Scorerecord record = new Scorerecord();
		st = getScoreTypeById(st.getId()+"");
		if(score == null || score == 0){
			score = st.getScore();
		}
		record.setCreator(user);
		record.setCreatedate(today);
		record.setReceiver(scorersm);
		record.setSender(empsm);
		record.setScoretype(st);
		record.setScoredate(scoredate);
		record.setScore(score);
		em.persist(record);
		em.flush();
		//Update history scores
		scorersm.setHistorytotal((scorersm.getHistorytotal()+(long)score));
		em.merge(scorersm);
		//Update montyly scores
		Scoresummary summary=this.getScoreSummary(scorersm, scoredate);
//		System.out.println("Score summary address :"+summary);s
		if(summary == null){
			//Not exist
			summary = new Scoresummary();
			summary.setEmployee(scorersm.getEmployee());
			summary.setDate(scoredate);
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(new Long(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(new Long(score));
			em.persist(summary);
			em.flush();
			em.persist(LoggerAction.createNewScoreSummary(user,summary));
		}else{
			//update
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(summary.getFixscore() + new Long(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(summary.getScore() + new Long(score));
			em.merge(summary);
			em.persist(LoggerAction.updateScoreSummary(user,summary));
		}
		em.persist(LoggerAction.assignScoretype(user, record.getId(), st, scorersm.getEmployee()));
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
		Scoremember member = record.getReceiver();
		Integer score = record.getScore();
		Scoresummary summary  =  this.getScoreSummary(record.getReceiver(), record.getScoredate());
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_FIX)
			summary.setFixscore(summary.getFixscore() - new Long(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP)
			summary.setScore(summary.getScore() - new Long(score));
		em.merge(summary);
		em.persist(LoggerAction.updateScoreSummary(user,summary));
		member.setHistorytotal(member.getHistorytotal() - new Long(score));
		em.merge(member);
		em.persist(LoggerAction.removeScoreRecord(user,record));
		em.remove(record);
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
		member.setHistorytotal(0L);
		member.setMonthlyremain(0);
		member.setMonthlyscore(0);
		member.setMonthlytotal(0);
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
				" receiverid=? AND EXTRACT(month FROM scoredate)=?").setParameter(1, member.getEmployee().getWorkerid())
				.setParameter(2, c.get(Calendar.MONTH)+1).getResultList();
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
//			e.printStackTrace();
			return null;
		}
	}
	
	public Map getRecordsByStatement(int pagenum, int lotsize, String statement) {
		return null;
		
	}

	public Map getSummaryByStatement(int pagenum, int lotsize, String statement) throws Exception{
		Map ret = new HashMap<String,Object>();
		List<ScoreMemberRank> list = null;
		if(pagenum == -1 || lotsize == -1){
			list = em.createNativeQuery(statement,ScoreMemberRank.class).getResultList();
		}else{
			list = em.createNativeQuery(statement,ScoreMemberRank.class)
				.setFirstResult(pagenum*lotsize -lotsize).setMaxResults(lotsize).getResultList();
		}
		ret.put("list", list);
		ret.put("count", list.get(0).getCount());
		return ret;
	}
}
