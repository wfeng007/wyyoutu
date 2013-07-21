<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%!%>
<%%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=request.getParameter("header_title")%></title>
<!-- bootstrap.css basic -->
<link href="./jquery/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">

<!-- jquery -->
<script src="./jquery/jquery-1.7.2.min.js"></script>
<!-- bootstrap.js -->
<script src="./jquery/bootstrap/js/bootstrap.js"></script>


<!-- bootstrap修改 -->
<style type="text/css">
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
	width: 1194px;/* 1180(1184) 15个span 默认940 为12个span */ 
	/* border: 1px solid #eeeeee; */
}
.span12,
.span11,
.span10,
.span9,
.span8,
.span7,
.span6,
.span5,
.span4,
.span3,
.span2,
.span1{
	 /* border: 1px solid #eeeeee; */
}
</style>