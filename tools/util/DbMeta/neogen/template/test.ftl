<#ftl />

${javaMeta4Query}

${javaMeta4Query.queryMeta}

className:${javaMeta4Query.className}
<#list javaMeta4Query.propertyMetaList as propertyMetaList>
	propertyName:${propertyMetaList.propertyName}
	javaType:${propertyMetaList.javaTypeName}
	shortJavaType:${propertyMetaList.shortJavaTypeName}
</#list>