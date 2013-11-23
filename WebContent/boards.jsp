<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%!
//
%>
<%
//
String navPage="BOARDS";
pageContext.setAttribute("navPage",navPage); //设置到page范围的attrabute
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="boards" />   
</jsp:include>
<link rel="stylesheet" href="./jquery/masonry/style.masonry.css" />
<script src="./jquery/masonry/jquery.masonry.min.js"></script>
<jsp:include page="/neck.jsp" flush="true">
	<jsp:param name="nav_page"  value="${navPage}"/>
</jsp:include>
boards building...
<style type="text/css">
#container .box {
	height:220px
}

.board a {
	display:block;
	border: 1px solid #fff;
	width : 100%;
	height : 100%
	
}
.board a:hover {
	border: 1px /* solid */dashed  #eee;
}

</style>
<div id="container" class="container" >
	<div class="box col25 board"><a href="./index.jsp"><img alt="add" src="./res/image/folder_add.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
</div>

<jsp:include page="/footer.jsp" flush="true"/>