<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%!%>
<%%>
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
         <a class="brand" href="./">我要优图</a>
         <div class="nav-collapse collapse">
           <ul class="nav">
             <li class="active"><a href="./"><i class="icon-home icon-white"></i>主页</a></li>
             <li><a href="./login.jsp"><i class="icon-book"></i>登录</a></li>
             <li><a href="./account.jsp"><i class="icon-pencil"></i>设置</a></li>
             <li><a href="#about"><i class="icon-book"></i>关于</a></li>
             <li><a href="#contact"><i class="icon-pencil"></i>联系</a></li>
           </ul>
           <form class="navbar-form pull-right" action="./session!login.act" method="POST">
              <input id="userId" name="userId" class="span2" type="text" placeholder="邮箱 或 昵称">
              <input id="password" name="password" class="span2"  type="password" placeholder="密码">
              <button id="signin_submit" type="submit" class="btn">登录</button>
           </form>
         </div><!--/.nav-collapse -->
    </div>	 
  </div>
</div>
