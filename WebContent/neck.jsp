<%@page import="wyyoutu.web.WebPlugin"%>
<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%@ page session="false"%>
<%!%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contextPath=path+"/";
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
String navPage=request.getParameter("nav_page");
if(navPage==null)navPage="HOME";
%>

<style type="text/css">
/** 这样做会让body的内容到navbar下面 ，包括自己在背景上写的内容。 **/
  body { 
    padding-top: 60px;
    padding-bottom: 40px;
  }
</style>

</head>
<body>

<!--navbar区域，定义了图层以及一般样式布局位置等  顶部导航条 navbar-fixed-top表示窗口顶部？ -->
<div class="navbar navbar-inverse navbar-fixed-top">
   <div class="navbar-inner">
     <div class="container">
         <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
         </button>
         <a class="brand" href="<%=contextPath%>./">我要优图</a>
         <div class="nav-collapse collapse">
           <ul class="nav">
             <li <%=("HOME".equals(navPage))?"class='active' ":""%>><a href="<%=contextPath%>./"><i class="icon-globe"></i>主页</a></li>
             <li <%=("LOGIN".equals(navPage))?"class='active' ":""%>><a href="<%=contextPath%>./login.jsp"><i class="icon-user"></i>登录</a></li>
<%if(accountInfo!=null){%>
             <li <%=("SETTING".equals(navPage))?"class='active' ":""%>><a href="<%=contextPath%>./account.jsp"><i class="icon-wrench"></i>设置</a></li>
             <li <%=("MYYOUTU".equals(navPage))?"class='active' ":""%>><a href="<%=contextPath%>./index.jsp?owner=<%=accountInfo.getUserId()%>"><i class="icon-home"></i>我的优图</a></li>
<%}%>
             <li <%=("ABOUT".equals(navPage))?"class='active' ":""%>><a href="#about"><i class="icon-book"></i>关于</a></li>
             <li <%=("CALL".equals(navPage))?"class='active' ":""%>><a href="#contact"><i class="icon-envelope"></i>联系</a></li>
             <%WebPlugin.doHandle("vNavbarItem",request,response); %>
           </ul>
           
<%//登录信息等
if(accountInfo==null){%>           
           <form class="navbar-form pull-right" action="./session!login.act" method="POST">
              <input id="userId" name="userId" class="span2" type="text" placeholder="邮箱 或 昵称">
              <input id="password" name="password" class="span2"  type="password" placeholder="密码">
              <button id="signin_submit" type="submit" class="btn btn-warning">登录</button>
           </form>
           
<%}else{ %> 
<ul class="nav pull-right">
    <li class="dropdown">
    	<a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=accountInfo.getUserId()%><b class="caret"></b></a>
    	<ul class="dropdown-menu">
    	<li><a href="<%=contextPath%>./index.jsp?owner=<%=accountInfo.getUserId()%>"><i class="icon-home"></i>我的优图</a></li>
    	<li><a href="<%=contextPath%>./account.jsp"><i class="icon-wrench"></i>设置</a></li>
    	<li class="divider"></li>
    	<li><a href="<%=contextPath%>./session!logout.act">
      	<i class="icon-off"></i>退出
    </a></li>
    	</ul>
    </li>
    <li class="divider-vertical"></li>
    <li><a href="<%=contextPath%>./session!logout.act">
      		<i class="icon-off"></i>退出
    </a></li>
</ul>
		<div class="pull-right">
			<mytext style='line-height: 20px;padding: 10px 15px;display: block;color:#ffffff'><%=accountInfo.getUserName()%> 您好！</mytext> 
		</div>

<%}%>
       <%WebPlugin.doHandle("vNavbar",request,response); %>
         </div><!--/.nav-collapse -->
    </div>	 
  </div>
</div>

<%WebPlugin.doHandle("vPosNeck",request,response); %>