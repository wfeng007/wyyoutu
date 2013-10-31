/*
 * Generate time : 2013-10-31 19:06:37
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
import wyyoutu.model.RsItemExten;

/**
 * RsItemExtenDao
 * for table/view RS_ITEM_EXTEN 
 */
public class RsItemExtenDao {
	
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
	
	public int insert(RsItemExten obj) {
		return sessionTemplate.insert("wyyoutu.dao.RsItemExtenDao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("wyyoutu.dao.RsItemExtenDao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<RsItemExten> query(RsItemExten obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<RsItemExten>)sessionTemplate.selectList("wyyoutu.dao.RsItemExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsItemExten> query(RsItemExten obj,Paging paging) {
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
		return (List<RsItemExten>)sessionTemplate.selectList("wyyoutu.dao.RsItemExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<RsItemExten> query(Map<String,Object> mp) {
		return (List<RsItemExten>)sessionTemplate.selectList("wyyoutu.dao.RsItemExtenDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsItemExten> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<RsItemExten>)sessionTemplate.selectList("wyyoutu.dao.RsItemExtenDao.query",mp);
	}
	
	public int count(RsItemExten obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemExtenDao.count",mp);
	}
	
	public int count(RsItemExten obj,Paging paging) {
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
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemExtenDao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemExtenDao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsItemExtenDao.count",mp);
	}
	
	
	
	public RsItemExten getByPk(
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
		
		return (RsItemExten)sessionTemplate.selectOne("wyyoutu.dao.RsItemExtenDao.query",kpParaMap);
	}
	

	public int update(RsItemExten obj) {
		return sessionTemplate.update("wyyoutu.dao.RsItemExtenDao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("wyyoutu.dao.RsItemExtenDao.update", mp);
	}
	
	public int delete(RsItemExten obj) {
		return sessionTemplate.delete("wyyoutu.dao.RsItemExtenDao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("wyyoutu.dao.RsItemExtenDao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	
	
}