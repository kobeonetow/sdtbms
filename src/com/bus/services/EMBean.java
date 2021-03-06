package com.bus.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EMBean {
	
	public static boolean D = true;

	@PersistenceContext(unitName="shundebuspersistenceunit")
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	/**
	 * 运行Select命令,返回结果List<Object[]>
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public Object runQuery(String query) throws Exception{
		if(D)
			System.out.println("Running query###:"+query);
		return em.createNativeQuery(query).getResultList();
	}
}
