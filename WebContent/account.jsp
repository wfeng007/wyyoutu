<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="summ.framework.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%@ page import="wyyoutu.service.RsItemService" %>
<%@ page import="wyyoutu.dao.CoPeopleDao" %>
<%@ page import="wyyoutu.model.CoPeople" %>
<%
//TODO 后期应该抛开session 从后台获取accountinfo来判断是否有session 后期不一定使用httpsession作为session判断
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//out.println("need to login"); //在jsp界面输出前不要用out输出。
}


//还是要获取一次本item的一些数据数据
CoPeopleDao peopleDao=(CoPeopleDao)SpringContextHolder.getApplicationContext().getBean("coPeopleDao");
CoPeople people=peopleDao.getByPk(accountInfo.getUserId());//需要校验？
//TODO 增加无法查询到指定id的item时的处理

%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="51youtu-account" />   
</jsp:include>

<style type="text/css">
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
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

<jsp:include page="/neck.jsp" flush="true">
	<jsp:param value="SETTING" name="nav_page"/>
</jsp:include>

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
						<span class="uneditable-input"><%=people.getId()%></span>(登录名，不可修改！)
						<input id="userId" name="userId" type="text" class="hide" value="<%=people.getId()%>"/>
					</div>
				</div> 
				<div class="control-group">
					<label class="control-label">账号名称：</label>
					<div class="controls">
						<input id="userName" name="userName" type="text" value="<%=people.getName()%>"/> 
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

<%}//if(accountInfo==null)
else{%>
<p>请登录后修改。</p>
<% }%>
</div>


<jsp:include page="/footer.jsp" flush="true"/>