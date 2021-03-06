<#ftl />
<#--
  - 实体类模板。
  - author:	王丰
  - version: 0.2
  -->
  /*
   * Generate time : ${time}
   * Version : ${version}
   */
<#if (mapping.packageName?length != 0)>
package ${mapping.packageName};

</#if>
<#if (mapping.imports?size != 0)>
	<#list mapping.imports as import>
import ${import};
	</#list>
</#if>
import java.util.HashMap;
import java.util.Map;
/**
 * ${mapping.className} 
 * <#if mapping.comment?trim != "">table comment : ${mapping.comment}</#if> 
 */
public class ${mapping.className} {
<#if (mapping.propertyMappingList?size != 0)>

	<#list mapping.propertyMappingList as property>
		<#-- 如果该属性要生成 -->
		<#if (property.generate)>
		<#-- field -->
			<#if property.shortType == "String">
	${mapping.propertyModifier} ${property.shortType} ${property} ;	<#if property.comment?trim != "">/* ${property.comment?replace("\n"," ")?j_string}*/</#if>
			<#else>
	${mapping.propertyModifier} ${property.shortType} ${property} ; <#if property.comment?trim != "">/* ${property.comment?replace("\n"," ")?j_string}*/</#if>
			</#if>
		</#if>
	</#list>

</#if>

	/**
	 * the constructor
	 */
	public ${mapping.className}() {
		//TODO
	}
	
	
<#if (mapping.propertyMappingList?size != 0)>

	/***GETTER-SETTER***/
	<#list mapping.propertyMappingList as property>
		<#-- 如果该属性要生成 -->
		<#if (property.generate)>
	<#-- getter方法 -->
	/**
	 * get the ${property} <#if property.comment?trim != "">- ${property.comment?replace("\n"," ")?j_string}</#if>
	 * @return the ${property}
	 */
	public ${property.shortType} get${property?cap_first}() {
		return this.${property};
	}
	
	<#-- setter方法 -->
	/**
	 * set the ${property} <#if property.comment?trim != "">- ${property.comment?replace("\n"," ")?j_string}</#if>
	 */
	public void set${property?cap_first}(${property.shortType} ${property}) {
		this.${property} = ${property};
	}
	
		</#if>
	</#list>
	/***GETTER-SETTER END***/
	
	
	<#--
	
	/**
	 * get the value from Map
	 */
	public void fromMap(Map map) {
	
	<#if (mapping.propertyMappingList?size != 0)>
		<#list mapping.propertyMappingList as property>
			<#if (property.generate)>
				 <#if property.shortType == "String">
		set${property?cap_first}(StringUtils.defaultIfEmpty(StringUtils.toString(map.get("${property}")), ${property}));
				 <#elseif property.shortType == "Double">
		set${property?cap_first}(NumberUtils.toDouble(StringUtils.toString(map.get("${property}")), ${property}));
				 <#elseif property.shortType == "Float">
		set${property?cap_first}(NumberUtils.toFloat(StringUtils.toString(map.get("${property}")), ${property}));
		 		<#elseif property.shortType == "BigDecimal">
		set${property?cap_first}(NumberUtils.toBigDecimal(StringUtils.toString(map.get("${property}")), ${property}));
				 <#elseif property.shortType == "Integer">
		set${property?cap_first}(NumberUtils.toInteger(StringUtils.toString(map.get("${property}")), ${property}));
				 <#elseif property.shortType == "Long">
		set${property?cap_first}(NumberUtils.toLong(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "Boolean">
		set${property?cap_first}(NumberUtils.toBoolean(StringUtils.toString(map.get("${property}")), ${property}));
				 <#elseif property.shortType == "int">
		set${property?cap_first}(NumberUtils.toint(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "double">
		set${property?cap_first}(NumberUtils.todouble(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "float">
		set${property?cap_first}(NumberUtils.tofloat(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "long">
		set${property?cap_first}(NumberUtils.tolong(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "boolean">
		set${property?cap_first}(NumberUtils.toboolean(StringUtils.toString(map.get("${property}")), ${property}));
				<#elseif property.shortType == "Date">
		set${property?cap_first}(DateUtils.toDate(StringUtils.toString(map.get("${property}"))));
				<#else>
			//TODO ${property} cannot generate automatically ,${property.shortType} dont support
				</#if>
			</#if>	
		</#list>
	</#if>
	}
	
	/**
	 * set the value to Map
	 */
	public Map toMap() {
		
		Map map = new HashMap();
		<#if (mapping.propertyMappingList?size != 0)>
			<#list mapping.propertyMappingList as property>
				<#if (property.generate)>
				<#if property.shortType == "int">
			map.put("${property}",StringUtils.toString(new Integer(${property}), eiMetadata.getMeta("${property}")));	
				<#elseif property.shortType == "double">
			map.put("${property}",StringUtils.toString(new Double(${property}), eiMetadata.getMeta("${property}")));
				<#elseif property.shortType == "float">
			map.put("${property}",StringUtils.toString(new Float(${property}), eiMetadata.getMeta("${property}")));	
				<#elseif property.shortType == "long">
			map.put("${property}",StringUtils.toString(new Long(${property}), eiMetadata.getMeta("${property}")));	
				<#elseif property.shortType == "boolean">
			map.put("${property}",StringUtils.toString(new Boolean(${property}), eiMetadata.getMeta("${property}")));	
				<#else>
			map.put("${property}",StringUtils.toString(${property}, eiMetadata.getMeta("${property}")));	
				</#if>		
				</#if>
			</#list>
		</#if>
			
		return map;
	
	}
	
	-->
	
</#if>

}