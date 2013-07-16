<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>51youtu-login</title>
<link href="./jquery/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
<link rel="stylesheet" href="res/footer.css">
<script src="./jquery/bootstrap/js/bootstrap.js"></script>
</head>
<body>	
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
	width: 1194px;/* 1180(1184) 15个span 默认940 为12个span */
	/* border: 1px solid #eeeeee;  */
}
.span14,
.span13,
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
	 /* border: 1px solid #eeeeee;  */
}

/*  signin */
.form-signin {
  max-width: 300px;
  padding: 19px 29px 29px;
  margin: 0 auto 20px; /* 左右没有方框，并居中。 */
  background-color: #fff;
  border: 1px solid #e5e5e5;
  -webkit-border-radius: 5px;
     -moz-border-radius: 5px;
          border-radius: 5px;
  -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
     -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
          box-shadow: 0 1px 2px rgba(0,0,0,.05);
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin input[type="text"],
.form-signin input[type="password"] {
  font-size: 16px;
  height: auto;
  margin-bottom: 15px;
  padding: 7px 9px;
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
             <li class="active"><a href="./"><i class="icon-home icon-white"></i>主页</a></li>
             <li><a href="#about"><i class="icon-book"></i>关于</a></li>
             <li><a href="#contact"><i class="icon-pencil"></i>联系</a></li>
             <li><a href="./login.jsp"><i class="icon-book"></i>登录</a></li>
           </ul>
         </div><!--/.nav-collapse -->
    </div>	 
  </div>
</div>




<div  class="container">
	<div class="row">
		<div class="span8">...</div>
		<div class="span7">
			<form class="form-signin" action="./session!login.act" method="post">
				<!-- <h2 class="form-signin-heading">请登录</h2> -->
				<legend><h2>请登录</h2></legend>
				<label>账号：</label><input id="userId" name="userId" type="text" class="input-block-level" placeholder="邮箱 或 昵称"/> 
				<label>密码：</label><input id="password" name="password" type="password" class="input-block-level" placeholder="密码"/> 
				<label class="checkbox"> <input type="checkbox"	value="remember-me">记住</label>
				<button class="btn btn-large btn-primary" type="submit">登录</button>
				<button class="btn btn-large" type="reset">重置</button>
			</form>
		</div>
	</div>
</div>




<hr/>
<footer>
<div id="footer"> 
<div id="copyright">
51youtu v0.5@ 沪ICP备12041334号 Copyright © 2012 - 2013 wfeng007 <br/> 
wfeng007@163.com <br/>
<br/>
</div> 
</div>
</footer>
</body>
</html>