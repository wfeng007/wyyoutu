<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>

<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="51youtu-login" />   
</jsp:include>
<style type="text/css">
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
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

<jsp:include page="/neck.jsp" flush="true">
	<jsp:param value="LOGIN" name="nav_page"/>
</jsp:include>

<div  class="container">
	<div class="row">
		<div class="span8">
		...<br/>
		<a href="https://openapi.baidu.com/oauth/2.0/authorize?client_id=AfTCo9kptgqGGBjuYMMvmrSM&response_type=code&redirect_uri=http%3A%2F%2Fwww.51youtu.com%2F51youtu%2Fbaidu_redirect2.jsp&scope=netdisk">
			<img src="http://bcs.duapp.com/open-api/oauth%2Flogin-short.png"/>
		</a>
		</div>
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

<jsp:include page="/footer.jsp" flush="true"/>