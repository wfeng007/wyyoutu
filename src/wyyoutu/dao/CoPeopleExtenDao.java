/*
 * Generate time : 2013-10-20 14:46:12
 * Version : 3.0.0
 */
package wyyoutu.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.apache.commons.beanutils.PropertyUtils; // for beanutils
import summ.framework.Paging; //for paging
import wyyoutu.model.CoPeopleExten;

/**
 * CoPeopleExtenDao
 * for table/view co_people_exten 
 */
public class CoPeopleExtenDao {
	
	//gen begin
	private SqlSessionTemplate sessionTemplate;
	private SqlSessionFactory sqlSessionFactory;
	/**
	 * @return the sqlSessionFactory
	 */
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	/**
	 * @param sqlSessionFactory the sqlSessionFactory to set
	 */
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.sessionTemplate=new SqlSessionTemplate(sqlSessionFactory);
	}
	
	public SqlSessionTemplate getSessionTemplate() {
		return sessionTemplate;
	}

	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	public int insert(CoPeopleExten obj) {
		return sessionTemplate.insert("wyyoutu.dao.CoPeopleExtenDao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("wyyoutu.dao.CoPeopleExtenDao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<CoPeopleExten> query(CoPeopleExten obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<CoPeopleExten>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<CoPeopleExten> query(CoPeopleExten obj,Paging paging) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
			if(paging!=null){
				mp.put("paging",paging);
			}
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<CoPeopleExten>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<CoPeopleExten> query(Map<String,Object> mp) {
		return (List<CoPeopleExten>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<CoPeopleExten> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<CoPeopleExten>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleExtenDao.query",mp);
	}
	
	public int count(CoPeopleExten obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleExtenDao.count",mp);
	}
	
	public int count(CoPeopleExten obj,Paging paging) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
			if(paging!=null){
				mp.put("paging",paging);
			}
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleExtenDao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleExtenDao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleExtenDao.count",mp);
	}
	
	
	
	public CoPeopleExten getByPk(
			Long seqId
			) {
		// validate pk-para
		if(
			seqId==null 
		){
			throw new java.lang.IllegalArgumentException("pk must not be NULL!!");
		}
		
		//
		Map<String,Object> kpParaMap=new HashMap<String,Object>();
		kpParaMap.put("seqId",seqId);
		
		return (CoPeopleExten)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleExtenDao.query",kpParaMap);
	}
	

	public int update(CoPeopleExten obj) {
		return sessionTemplate.update("wyyoutu.dao.CoPeopleExtenDao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("wyyoutu.dao.CoPeopleExtenDao.update", mp);
	}
	
	public int delete(CoPeopleExten obj) {
		return sessionTemplate.delete("wyyoutu.dao.CoPeopleExtenDao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("wyyoutu.dao.CoPeopleExtenDao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	
	
}