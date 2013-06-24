<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>51youtu-login</title>
<link rel="stylesheet" href="res/basic.css"/>
</head>
<body>	
登录主页面
<style type="text/css">
#page_container {
	width: 1194px;
	margin: 0 auto;
	display:block;
	max-height: 800px;
}
#other {
	float: left;
	/* min-height: 800px; */
	width: 500px;
	height: 600px;
	border: 1px solid #eeeeee;
}
#local {
	float: right;
	width: 500px;
	height: 600px;
	border: 1px solid #eeeeee;
	/*TEXT-ALIGN: center; 内部水平居中*/
}
.clear {
 	clear: both;/* 保证同行两边不能有其他div 一般用于居中*/
}
#f{
display:block;
margin-top: 200px;
margin-left: auto; margin-right: auto; /* 左右居中 注意缩小width体现效果*/
clear: both;
max-width: 150px; /* 强制100px*/
border: 1px solid #eeeeee;
/*
margin-top: 200px;
MARGIN-RIGHT: auto;
MARGIN-LEFT: auto; */
}
</style>
<div id="page_container">
<div id="other">
other“当前放置图片”
</div>
<div id="local">
local1
<form id="f"  action="./session!login.act"  method="post" >
账号：<input id="userId" name="userId" value="" title="username" tabindex="4" type="text" placeholder="邮箱 或 昵称"/>
密码：<input id="password" name="password" value="" title="password" tabindex="5" type="password"/>
<input value="登录"  type="submit"/> <input value="重置" type="reset"/>
</form>
</div>
</div>
</body>
</html>