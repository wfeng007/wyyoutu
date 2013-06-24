/*
 * Generate time : 2013-04-18 20:01:01
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
import wyyoutu.model.RsTagged;

/**
 * RsTaggedDao
 * for table/view RS_TAGGED 
 */
public class RsTaggedDao {
	
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
	
	public int insert(RsTagged obj) {
		return sessionTemplate.insert("wyyoutu.dao.RsTaggedDao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("wyyoutu.dao.RsTaggedDao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<RsTagged> query(RsTagged obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<RsTagged>)sessionTemplate.selectList("wyyoutu.dao.RsTaggedDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsTagged> query(RsTagged obj,Paging paging) {
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
		return (List<RsTagged>)sessionTemplate.selectList("wyyoutu.dao.RsTaggedDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<RsTagged> query(Map<String,Object> mp) {
		return (List<RsTagged>)sessionTemplate.selectList("wyyoutu.dao.RsTaggedDao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<RsTagged> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<RsTagged>)sessionTemplate.selectList("wyyoutu.dao.RsTaggedDao.query",mp);
	}
	
	public int count(RsTagged obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsTaggedDao.count",mp);
	}
	
	public int count(RsTagged obj,Paging paging) {
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
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsTaggedDao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsTaggedDao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("wyyoutu.dao.RsTaggedDao.count",mp);
	}
	
	
	
	public RsTagged getByPk(
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
		
		return (RsTagged)sessionTemplate.selectOne("wyyoutu.dao.RsTaggedDao.query",kpParaMap);
	}
	

	public int update(RsTagged obj) {
		return sessionTemplate.update("wyyoutu.dao.RsTaggedDao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("wyyoutu.dao.RsTaggedDao.update", mp);
	}
	
	public int delete(RsTagged obj) {
		return sessionTemplate.delete("wyyoutu.dao.RsTaggedDao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("wyyoutu.dao.RsTaggedDao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	public int deleteByTargetId(String targetId) {
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("targetId",targetId);
		return sessionTemplate.delete("wyyoutu.dao.RsTaggedDao.deleteByTargetId", mp);
	}
	
}