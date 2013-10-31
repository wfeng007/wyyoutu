<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="wyyoutu.web.WebResult" %>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%!
//
// 依赖WebResult的常量。WebResult可以转发到本result-traffic。
// 需要参考webresult定义跳转数据规范。
// 
%>
<%
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="struts-error-page" />   
</jsp:include>
<jsp:include page="/neck.jsp" flush="true"/>

<div class="contianer">

<!-- struts错误处理 -->
<h3>
<s:property value="exception.message" />
</h3>
<pre>
<s:property value="exceptionStack" />
</pre>
</div>
<jsp:include page="/footer.jsp" flush="true"/>




