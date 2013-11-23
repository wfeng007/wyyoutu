<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%!
//
// 依赖WebResult的常量。WebResult可以转发到本result-traffic。
// 需要参考webresult定义跳转数据规范。
// 
%>
<%
//
String navPage="TEMPLATE";
pageContext.setAttribute("navPage",navPage); //设置到page范围的attrabute
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="boards" />   
</jsp:include>
<jsp:include page="/neck.jsp" flush="true">
	<jsp:param name="nav_page"  value="${navPage}"/>
</jsp:include>
boards-body...
<jsp:include page="/footer.jsp" flush="true"/>