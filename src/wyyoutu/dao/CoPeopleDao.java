/*
 * Generate time : 2012-11-07 21:51:02
 * Version : 3.0.0
 */
package wyyoutu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import summ.framework.Paging;
import wyyoutu.model.CoPeople;

/**
 * CoPeopleDao
 * for table/view co_people 
 */
public class CoPeopleDao {
	
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
	
	public int insert(CoPeople obj) {
		return sessionTemplate.insert("wyyoutu.dao.CoPeopleDao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("wyyoutu.dao.CoPeopleDao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<CoPeople> query(CoPeople obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<CoPeople>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<CoPeople> query(CoPeople obj,Paging paging) {
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
		return (List<CoPeople>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<CoPeople> query(Map<String,Object> mp) {
		return (List<CoPeople>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<CoPeople> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<CoPeople>)sessionTemplate.selectList("wyyoutu.dao.CoPeopleDao.query",mp);
	}
	
	public int count(CoPeople obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleDao.count",mp);
	}
	
	public int count(CoPeople obj,Paging paging) {
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
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleDao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleDao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleDao.count",mp);
	}
	
	
	
	public CoPeople getByPk(
			String id
			) {
		// validate pk-para
		if(
			id==null 
		){
			throw new java.lang.IllegalArgumentException("pk must not be NULL!!");
		}
		
		//
		Map<String,Object> kpParaMap=new HashMap<String,Object>();
		kpParaMap.put("id",id);
		
		return (CoPeople)sessionTemplate.selectOne("wyyoutu.dao.CoPeopleDao.query",kpParaMap);
	}
	

	public int update(CoPeople obj) {
		return sessionTemplate.update("wyyoutu.dao.CoPeopleDao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("wyyoutu.dao.CoPeopleDao.update", mp);
	}
	
	public int delete(CoPeople obj) {
		return sessionTemplate.delete("wyyoutu.dao.CoPeopleDao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("wyyoutu.dao.CoPeopleDao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	
	
}