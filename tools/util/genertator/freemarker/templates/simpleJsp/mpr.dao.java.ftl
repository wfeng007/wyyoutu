<#ftl />
<#--
  - mpr相关dao模板模板。
  - author:	王丰
  - version: 0.2 补齐mp以及obj参数
  - version: 0.3 增加queryByPk
  - version: 0.4 query(${mapping.className} obj) 内部修改为生成Map
  - version: 0.5 增加count功能
  - version: 0.6 增加mysql库时默认的分页
  -->
/*
 * Generate time : ${time}
 * Version : ${version}
 */
package ${daoPackage};

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.apache.commons.beanutils.PropertyUtils; // for beanutils
import summ.framework.Paging; //for paging
import ${mapping.packageName}.${mapping.className};

/**
 * ${mapping.className}Dao
 * for table/view ${mapping.tableName} 
 */
public class ${mapping.className}Dao {
	
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
	
	public int insert(${mapping.className} obj) {
		return sessionTemplate.insert("${daoPackage}.${mapping.className}Dao.insert", obj);
	}
	
	public int insert(Map<String,Object> mp) {
		return sessionTemplate.insert("${daoPackage}.${mapping.className}Dao.insert", mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，不带分页。
 	 * 
 	 * @param obj 参数条件
 	 */
	public List<${mapping.className}> query(${mapping.className} obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (List<${mapping.className}>)sessionTemplate.selectList("${daoPackage}.${mapping.className}Dao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能，带分页。
 	 * 
 	 * @param obj 参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<${mapping.className}> query(${mapping.className} obj,Paging paging) {
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
		return (List<${mapping.className}>)sessionTemplate.selectList("${daoPackage}.${mapping.className}Dao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * 
 	 *
 	 */
	public List<${mapping.className}> query(Map<String,Object> mp) {
		return (List<${mapping.className}>)sessionTemplate.selectList("${daoPackage}.${mapping.className}Dao.query",mp);
	}
	
	/**
 	 * 
 	 * 基本查询或列表功能
 	 * 
 	 * @param mp Map方式参数条件
 	 * @param paging 分页条件 paging==null时则为不分页
 	 *
 	 */
	public List<${mapping.className}> query(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (List<${mapping.className}>)sessionTemplate.selectList("${daoPackage}.${mapping.className}Dao.query",mp);
	}
	
	public int count(${mapping.className} obj) {
		// for beanutils
		Map mp=null;
		try {
			mp=PropertyUtils.describe(obj);
		} catch (Exception e) {
			new RuntimeException("describe failed!!",e);
		}
		return (Integer)sessionTemplate.selectOne("${daoPackage}.${mapping.className}Dao.count",mp);
	}
	
	public int count(${mapping.className} obj,Paging paging) {
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
		return (Integer)sessionTemplate.selectOne("${daoPackage}.${mapping.className}Dao.count",mp);
	}
	
	public int count(Map<String,Object> mp) {
		return (Integer)sessionTemplate.selectOne("${daoPackage}.${mapping.className}Dao.count",mp);
	}
	
	public int count(Map<String,Object> mp,Paging paging) {
		if(paging!=null){
			mp.put("paging",paging);
		}
		return (Integer)sessionTemplate.selectOne("${daoPackage}.${mapping.className}Dao.count",mp);
	}
	
	
<#-- 增加pk查询
	public ${mapping.className} queryByPk(
		<#list pk as pk> 
		${pk.shortType} ${pk.columnName}<#if pk_has_next>, </#if>
		</#list>
		) {
		return (${mapping.className})sessionTemplate.selectOne("$daoPackage}.${mapping.className}Dao.query",mp);
	}
-->
	
	public ${mapping.className} getByPk(
			<#list pk as pk> 
			${pk.shortType} ${pk.propertyName}<#if pk_has_next>, </#if>
			</#list>
			) {
		// validate pk-para
		if(
		<#list pk as pk> 	
			${pk.propertyName}==null <#if pk_has_next>|| </#if>
		</#list>
		){
			throw new java.lang.IllegalArgumentException("pk must not be NULL!!");
		}
		
		//
		Map<String,Object> kpParaMap=new HashMap<String,Object>();
		<#list pk as pk>
		kpParaMap.put("${pk.propertyName}",${pk.propertyName});
		</#list>
		
		return (${mapping.className})sessionTemplate.selectOne("${daoPackage}.${mapping.className}Dao.query",kpParaMap);
	}
	

	public int update(${mapping.className} obj) {
		return sessionTemplate.update("${daoPackage}.${mapping.className}Dao.update", obj);
	}
	
	public int update(Map<String,Object> mp) {
		return sessionTemplate.update("${daoPackage}.${mapping.className}Dao.update", mp);
	}
	
	public int delete(${mapping.className} obj) {
		return sessionTemplate.delete("${daoPackage}.${mapping.className}Dao.delete", obj);
	}
	
	public int delete(Map<String,Object> mp) {
		return sessionTemplate.delete("${daoPackage}.${mapping.className}Dao.delete", mp);
	}
	//gen end
	
	
	//
	// custom 
	//
	
	
}