<#ftl />
<#--
- mybatis模板
- author:王丰
- version: 0.4 query count修改为全部参数
- version: 0.5 namespace内容更换为${daoPackage}.${mapping.className}Dao
- version: 0.6 增加mysql库时默认的分页 select 字段变为sql片断 修订联合主件的orderby问题 去掉部分不需要生成的内容
-->
<?xml version="1.0" encoding="UTF-8" ?> 
<!DOCTYPE mapper 
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<!--      
		table information
		Generate time : ${time}
   		Version :  ${version}
		<#if mapping.comment?trim != "">
		table comment : ${mapping.comment}
		</#if> 
		tableName :${mapping.tableName} 
		<#list mapping.propertyMappingList as property>
		  ${property.columnName}  ${property.columnType}<#if property.notNull>   NOT NULL</#if><#if property.primaryKey>   primarykey</#if><#if property_has_next>, </#if>		<#if property.comment?trim != "">-REMARKS-:${property.comment?trim}</#if>
		</#list>
	-->
<#--
<mapper namespace="${nameSpace}">
-->
<mapper namespace="${daoPackage}.${mapping.className}Dao">
	<!-- table selected columns  -->
	<sql id="selectedColumns">
	<![CDATA[
		<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		  `${property.columnName}` as "${property.propertyName}"<#if property_has_next>, </#if>
		</#if>
		</#list>
	]]>
	</sql>
	
	<#--  test="@summ.framework.util.Ognl@isNotEmpty(${property.propertyName})" 或  test="${property.propertyName} != null" -->
	<!-- querying conditions -->
	<sql id="queryingConditions">
	<#-- 直接所有字段条件 FIXME 当前框架包没有动态给 -->
		<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		<if test="@summ.framework.util.Ognl@isNotEmpty(${property.propertyName})"> 
	      <![CDATA[	
	        AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		  ]]>
	 	</if>
		</#if>
		</#list>
	</sql>
	
<#--
	todo: paging部分只适用于mysql, oracle及db2需要重新实现
-->
	<select id="query" resultType="${mapping.packageName}.${mapping.className}">
	<#-- 直接查询出的字段
	<![CDATA[
		select 
		<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		  `${property.columnName}` as "${property.propertyName}"<#if property_has_next>, </#if>
		</#if>
		</#list>
		from  ${mapping.tableName} 
	]]>
	-->
 	<#-- 使用include查询出的字段 -->
 	<![CDATA[
 		select 
 	]]>
 		  <!-- 
 		  "selectedColumns" for same-file include.  
 		  "${mapping.packageName}.${mapping.className}Dao.selectedColumns" is for other-file 
 		  and this sql-segment must be load first...
 		   -->
 		  <include refid="selectedColumns"/>
 	<![CDATA[
 		from  ${mapping.tableName} 
 	]]>
	    <where>
	    <#-- 条件选择
		<#list mapping.propertyMappingList as property>
		<#if (property.generate && property.conditional)>
		  <if test="${property.propertyName} != null">
		  <![CDATA[	
		    AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		  ]]>
		  </if>
		</#if>
		</#list>
		-->
		<#--  test="@Ognl@isNotEmpty(${property.propertyName})" 或  test="${property.propertyName} != null" -->
		<#-- 直接所有字段条件 建议分片
		<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		  <if test="${property.propertyName} != null"> 
	      <![CDATA[	
	        AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		  ]]>
	 	</if>
		</#if>
		</#list>
		-->
		<#-- 使用include 直接所有字段条件  -->
		  <include refid="queryingConditions"/>
	    </where>
	    <!-- order by need test -->	    
		<if test="orderBy != null">
		<![CDATA[
		ORDER BY ${r"$"}{orderBy}
		]]>
		</if>
		<if test="orderBy == null">
		<![CDATA[
		ORDER BY <#list pk as pk>`${pk.columnName}`<#if pk_has_next>, </#if> </#list> ASC 
		]]>
		</if>
		<#if DBType=="MySQL">
		<if test="paging != null">
		<![CDATA[
			LIMIT  ${r"#"}{paging.currentPositionWithZeroStart}, ${r"#"}{paging.countPerPage}
		]]>
		</if>
		</#if>
    </select>
    
    <select id="count" resultType="int">
      <![CDATA[
	  SELECT COUNT(*) FROM ${mapping.tableName} 
	  ]]>
	  <where>
	  <#-- pk条件
		<#list mapping.propertyMappingList as property>
		<#if (property.generate && property.conditional)>
		<if test="${property.propertyName} != null">
	    <![CDATA[	
	      AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		]]>
	 	</if>
		</#if>
		</#list>
	  -->
	  	<#--  test="@Ognl@isNotEmpty(${property.propertyName})" 或  test="${property.propertyName} != null" -->
	  	<#-- 直接所有字段条件 建议分片
		<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		  <if test="${property.propertyName} != null">
		  <![CDATA[	
		    AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		  ]]>
		</if>
		</#if>
		</#list>
		-->
	  	<#-- 使用include 直接所有字段条件  -->
		<include refid="queryingConditions"/>
	  </where>
		<#if DBType=="MySQL">
	  	<if test="paging != null">
		<![CDATA[
			LIMIT  ${r"#"}{paging.currentPositionWithZeroStart}, ${r"#"}{paging.countPerPage}
		]]>
		</if>
		</#if>
	</select>


<#-- 这部分不用生成了
	<#list mapping.propertyMappingList as property>
		<#if (property.generate)> 
		<if test="${property.propertyName} != null">
	    <![CDATA[	
	      AND `${property.columnName}` = ${r"#"}{${property.propertyName}}
		]]>
	 	</if>
		</#if>
	</#list>
-->

	<#-- 插入操作 -->
	<insert id="insert">
	<![CDATA[
		INSERT INTO ${mapping.tableName} (
		  <#list mapping.propertyMappingList as property>
		  <#if (property.generate)>
		  `${property.columnName}`<#if property_has_next>,</#if>
		  </#if>
		  </#list>
		)
		VALUES (		 
        <#if DBType=="Oracle">
		  <#list mapping.propertyMappingList as property>
		  <#if (property.generate)>
		  ${r"#"}{${property.propertyName}<#if property.columnType == "NUMBER">,jdbcType=NUMERIC<#elseif property.columnType == "VARCHAR2">,jdbcType=VARCHAR<#elseif property.columnType == "DATE">,jdbcType=DATE</#if>}<#if property_has_next>, </#if>
		  </#if>
		  </#list>
	    <#elseif DBType="DB2">
	      <#list mapping.propertyMappingList as property>
	      <#if (property.generate)>
	      ${r"#"}{${property.propertyName}<#if property.columnType == "DECIMAL">,jdbcType=NUMERIC<#elseif property.columnType == "VARCHAR">,jdbcType=VARCHAR<#elseif property.columnType == "DATE">,jdbcType=DATE</#if>}<#if property_has_next>, </#if>
	      </#if>
	      </#list>
	   	<#elseif DBType="MySQL">
	      <#list mapping.propertyMappingList as property>
	      <#if (property.generate)>
	      ${r"#"}{${property.propertyName}<#if property.columnType == "LONG">,jdbcType=INTEGER<#-- 该类型需要修正 --><#elseif property.columnType == "VARCHAR">,jdbcType=VARCHAR<#elseif property.columnType == "DATETIME">,jdbcType=TIMESTAMP<#elseif property.columnType == "DOUBLE">,jdbcType=DOUBLE</#if>}<#if property_has_next>, </#if>
	      </#if>
	      </#list>
	    <#else>
	      <#list mapping.propertyMappingList as property>
	      <#if (property.generate)>
	      ${r"#"}{${property.propertyName}}<#if property_has_next>, </#if>
	      </#if>
	      </#list>
	    </#if>
	    )
	]]>
	</insert>
	
	<#-- TODO 批量插入 -->
	
	<#-- 删除操作 默认必须有PK才能删除 -->
	<delete id="delete">
	<![CDATA[
		DELETE FROM ${mapping.tableName} WHERE 
		<#list pk as property> 
			`${property.columnName}` =  ${r"#"}{${property.propertyName}}<#if property_has_next> AND </#if>
		</#list>
	]]>
	</delete>
	
	<#-- TODO 批量删除  使用IN-->
	
	<#-- 更新操作 -->
	<update id="update">
		UPDATE ${mapping.tableName} 
		<set> 
		<#list mapping.propertyMappingList as property>	
		<#if (property.generate && !property.primaryKey)>
		<if test="${property.propertyName} != null">
		<![CDATA[
		  <#if DBType="Oracle">
		  ${property.columnName} = ${r"#"}{${property.propertyName}<#if property.columnType == "NUMBER">,jdbcType=NUMERIC<#elseif property.columnType == "VARCHAR2">,jdbcType=VARCHAR<#elseif property.columnType == "DATE">,jdbcType=DATE</#if>}<#if property_has_next>, </#if>
		  <#elseif DBType="DB2">
		  ${property.columnName} = ${r"#"}{${property.propertyName}<#if property.columnType == "DECIMAL">,jdbcType=NUMERIC<#elseif property.columnType == "VARCHAR">,jdbcType=VARCHAR<#elseif property.columnType == "DATE">,jdbcType=DATE</#if>}<#if property_has_next>, </#if>
		  <#elseif DBType="MySQL">
		  `${property.columnName}` = ${r"#"}{${property.propertyName}<#if property.columnType == "LONG">,jdbcType=INTEGER<#-- 该类型需要修正 --><#elseif property.columnType == "VARCHAR">,jdbcType=VARCHAR<#elseif property.columnType == "DATETIME">,jdbcType=TIMESTAMP<#elseif property.columnType == "DOUBLE">,jdbcType=DOUBLE</#if>}<#if property_has_next>, </#if>
		  <#else>
		  ${property.columnName} = ${r"#"}{${property.propertyName}}<#if property_has_next>, </#if>
		  </#if>
	    ]]>
	    </if>
		</#if> 
		</#list>
		</set> 
		<![CDATA[
		WHERE 	
		<#list pk as property>
		  `${property.columnName}` = ${r"#"}{${property.propertyName}}<#if property_has_next> AND </#if>
		</#list>
		]]>
	</update>
	
	<#-- TODO 批量更新  使用IN 以及自定义条件? -->
	
</mapper> 