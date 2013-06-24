/*
 * Generate time : 2012-10-28 17:16:34
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
import wyyoutu.model.RsItem;

/**
 * RsItemDao
 * for table/view RS_ITEM 
 */
public class RsItemDao {
	
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
	
	public int insert(RsItem obj) {
		return sessionTemplate.insert("wyyoutu.dao.RsItemDao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("wyyoutu.dao.RsItemDao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<RsItem> query(RsItem obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<RsItem>)sessionTemplate.selectList("wyyoutu.dao.RsItemDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsItem> query(RsItem obj,Paging paging) {
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
		return (List<RsItem>)sessionTemplate.selectList("wyyoutu.dao.RsItemDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<RsItem> query(Map<String,Object> mp) {
		return (List<RsItem>)sessionTemplate.selectList("wyyoutu.dao.RsItemDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsItem> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<RsItem>)sessionTemplate.selectList("wyyoutu.dao.RsItemDao.query",mp);
	}
	
	public int count(RsItem obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemDao.count",mp);
	}
	
	public int count(RsItem obj,Paging paging) {
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
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemDao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemDao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemDao.count",mp);
	}
	
	
	
	public RsItem getByPk(
			Integer seqId
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
		
		return (RsItem)sessionTemplate.selectOne("wyyoutu.dao.RsItemDao.query",kpParaMap);
	}
	

	public int update(RsItem obj) {
		return sessionTemplate.update("wyyoutu.dao.RsItemDao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("wyyoutu.dao.RsItemDao.update", mp);
	}
	
	public int delete(RsItem obj) {
		return sessionTemplate.delete("wyyoutu.dao.RsItemDao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("wyyoutu.dao.RsItemDao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	
	
}