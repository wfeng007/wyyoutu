<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="summ.framework.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%
//TODO 后期应该抛开session 从后台获取accountinfo来判断是否有session 后期不一定使用httpsession作为session判断
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//out.println("need to login"); //在jsp界面输出前不要用out输出。
}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>51youtu - account</title>

<!-- bootstrap -->
<link href="./jquery/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
<link rel="stylesheet" href="res/footer.css">
<style type="text/css">
/** 这样做会让body的内容到navbar下面 ，包括自己在背景上写的内容。 **/
body { 
  padding-top: 60px;
  padding-bottom: 40px;
}
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
	width: 1184px;/* 1180(1184) 15个span 默认940 为12个span */
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
<!--navbar区域，定义了图层以及一般样式布局位置等  顶部导航条 navbar-fixed-top表示窗口顶部？ -->
<div class="navbar navbar-inverse navbar-fixed-top">
   <div class="navbar-inner">
     <div class="container">
         <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
         </button>
         <a class="brand" href="./">我要优图</a>
         <div class="nav-collapse collapse">
           <ul class="nav">
             <li class="active"><a href="./">主页</a></li>
             <li><a href="#about">关于</a></li>
             <li><a href="#contact">联系</a></li>
           </ul>
         </div><!--/.nav-collapse -->
    </div>
  </div>
</div>
</head>
<body>
<div class="container">
<%if(accountInfo!=null ){%>
<h3>...已经登录...</h3>
<hr>
accountInfo.getUserId():<%=accountInfo.getUserId()%><br>
accountInfo.getUserName():<%=accountInfo.getUserName()%><br>
accountInfo:<%=accountInfo%><br>
<%}//if(accountInfo==null)%>
</div>

<hr/>
<footer>
<div id="footer"> 
<div id="copyright">
沪ICP备12041334号 Copyright &copy; 2012 - 2013 wfeng007 <br/> 
wfeng007@163.com <br/>
</div> 
</div>
</footer>

</body>
</html>