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


/* 账号信息表单 */
.form-account {
  padding: 19px 29px 29px;
  margin: 0 auto 20px; /* 左右没有方框，并居中。 */
  background-color: #fff;
  border: 1px solid #eeeeee;
  -webkit-border-radius: 5px;
     -moz-border-radius: 5px;
          border-radius: 5px;
  -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
     -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
          box-shadow: 0 1px 2px rgba(0,0,0,.05);
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
	<div class="row">
		<div class="span3">
		</div>
		<div class="span12">
			<form class="form-horizontal form-account" action="./scripting/modifyPeople.jss" method="post">
				<legend><h2>基本信息</h2></legend>
				<div class="control-group">
					<label class="control-label">账号标识：</label>
					<div class="controls">
						<span class="uneditable-input"><%=accountInfo.getUserId()%></span>(登录名，不可修改！)
						<input id="userId" name="userId" type="text" class="hide" value="<%=accountInfo.getUserId()%>"/>
					</div>
				</div> 
				<div class="control-group">
					<label class="control-label">账号名称：</label>
					<div class="controls">
						<input id="userName" name="userName" type="text" value="<%=accountInfo.getUserName()%>"/> 
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button class="btn btn-primary" type="submit">保存</button>
						<button class="btn " type="reset">重置</button>
					</div>
				</div>
			</form>
			<form class="form-horizontal form-account" action="./scripting/modifyPassword.jss" method="post">
				<input id="userId" name="userId" type="text" class="hide" value="<%=accountInfo.getUserId()%>"/>
				<legend><h2>密码</h2></legend>
				<div class="control-group">
					<label class="control-label">现有密码：</label>
					<div class="controls">
						<input id="curPwd" name="curPwd" type="password" placeholder="现有密码确认" />
					</div>
				</div> 
				<div class="control-group">
					<label class="control-label">新的密码：</label>
					<div class="controls">
						<input id="newPwd" name="newPwd" type="password" placeholder="新的密码"/> 
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">确认新码：</label>
					<div class="controls">
						<input id="checkPwd" name="checkPwd" type="password" placeholder="再确认一次新的密码"/> 
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button class="btn btn-primary" type="submit">保存</button>
						<button class="btn " type="reset">重置</button>
					</div>
				</div>
			</form>
		</div>
	</div>

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