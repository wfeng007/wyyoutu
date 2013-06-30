<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="summ.framework.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%@ page import="wyyoutu.service.RsItemService" %>
<%@ page import="wyyoutu.model.RsItem" %>

<%
//TODO 后期应该抛开session 从后台获取accountinfo来判断是否有session 后期不一定使用httpsession作为session判断
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//out.println("need to login");
}
String itemId=null;
itemId=request.getParameter("itemId");
System.out.print(itemId);

//还是要获取一次本item的一些数据数据
RsItemService rsis=(RsItemService)SpringContextHolder.getApplicationContext().getBean("rsItemService");
RsItem rsItem=rsis.getItemById(Integer.valueOf(itemId));//需要校验？
//TODO 增加无法查询到指定id的item时的处理

//
if(itemId==null){
	itemId="";
}

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>51youtu</title>
<link rel="stylesheet" href="res/basic.css"/>
<link rel="stylesheet" href="res/common_util.css"/>
<link rel="stylesheet" href="res/header2.css">
<link rel="stylesheet" href="res/signin.css" />
<link rel="stylesheet" href="res/footer.css">

<script src="./jquery/jquery-1.7.2.min.js"></script>
<script src="./jquery/jeditable/jquery.autogrow.js"></script>
<script src="./jquery/jeditable/jquery.jeditable_summ.js"></script>
<script src="./jquery/jeditable/jquery.jeditable.autogrow.js"></script>

<div id="header2">
	<!-- -->
	<div id="brand">
		<div class="section-wrap">
			<div class="logo">v:0.5(α)
				<h1>
					<a href="#" title="51youtu"></a>
				</h1>
			</div>
			<ul class="action"><!-- 功能 -->
				<div id="signin_area">			
					<!-- 登录表单框 使用signin.css -->			
					<div id="topnav" class="topnav"><div id="userId"> </div><a href="login" class="signin"><span>登录</span></a> </div>
					  <fieldset id="signin_menu">
					    <form method="post" id="signin" action="./session!login.act"> <!-- 这里直接用了form而不是ajax 这会要就页面重新刷新 -->
					      <label for="username">账号</label>
					      <input id="userId" name="userId" value="" title="username" tabindex="4" type="text" placeholder="邮箱 或 昵称">
					      </p>
					      <p>
					        <label for="password">密码</label>
					        <input id="password" name="password" value="" title="password" tabindex="5" type="password">
					      </p>
					      <p class="remember">
					        <input id="signin_submit" value="Sign in" tabindex="6" type="submit">
					        <input id="remember" name="remember_me" value="1" tabindex="7" type="checkbox">
					        <label for="remember">记住</label>
					      </p>
						  <!-- 
					      <p class="forgot"> <a href="#" id="resend_password_link">忘记密码?</a> </p>
					      <p class="forgot-username"> <A id=forgot_username_link ="If you remember your password, try logging in with your email" href="#">忘记用户名?</A> </p>
						-->
						</form>
					</fieldset>			
				</div>			
			</ul>
		</div>
	</div>
	
	<div id="nav">
		<div class="section-wrap">
			<ul class="entries clearfix">
				<li class="first"><a
					href="/">首页</a></li>
			</ul>
			<div class="search-bar">
			</div>
		</div>
	</div>
</div>
</head>

<body>
<style type="text/css">
#page_container {
	width: 1194px;
	margin: 0 auto;
	display:block;
}
#item {
	float: left;
	/* min-height: 800px; */
	width: 850px;
	border: 1px solid #eeeeee;
}
#side_bar_right {
	float: right;
	min-height: 600px;
	width: 330px;
	border: 1px solid #eeeeee;
}
.clear {
 	clear: both;
}
img#itemMedia {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 50px;
	margin-bottom: 50px;
	max-width: 800px;
}
/*#itemText*/
.edit_area {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 50px;
	margin-bottom: 50px;
	max-width: 800px;
	border: 1px solid #eeeeee;
}
#itemComments {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 50px;
	margin-bottom: 50px;
	max-width: 800px;
	border: 1px solid #eeeeee;
}
</style>
<section>
<div id="page_container">
	<div id="item">
		<img id="itemMedia" alt="<%=itemId%>" class=".clear" src="./svc/image?itemId=<%=itemId%>" />
				
<script type="text/javascript">
$(function(){
	$('.edit_area').editable('./item!modifyText.act',{
		 id   : 'itemId',//idkey
         name : 'text',//textkey
         submitdata:{action:"modify"},
		 type      : 'autogrow',
		 autogrow : {
	           lineHeight : 16,
	           minHeight  : 32
	     },
		 //rows: 5, 
         cancel    : '取消',
         submit    : '保存',
         indicator : '<img src="img/indicator.gif">',
         tooltip   : 'Click to edit...',
         method :"POST",
       	 callback : function(data, settings,submitdata) {//修改插件提供已经提交用的数据 submitdata
       	  		console.log(this); //本jquery对应的dom对象
            	console.log(data); //返回的内容
             	console.log(settings); //参数
             	if(data!=null&&typeof(data)==="object"&&data.success==true){
             		//根据源代码情况，默认内容被放在了settings.name;
             		//alert(submitdata[settings.name]);
       				$(this).html(submitdata[settings.name]);
             	}else{
					alert("修改失败！"); // 测试 
					//
				}
       			
         },
         ajaxdatatype:"json" //附加出来的ajax返回类型默认html
	});
});
</script>
		<!-- Text内容 itemText -->
		<div id="<%=itemId%>" class="edit_area"><%=rsItem.getText()%></div>
		
		<div id="itemComments" >
	<!-- Duoshuo Comment BEGIN -->
	<div class="ds-thread"></div>
	<script type="text/javascript">
	var duoshuoQuery = {short_name:"51youtu"};
	(function() {
		var ds = document.createElement('script');
		ds.type = 'text/javascript';ds.async = true;
		ds.src = 'http://static.duoshuo.com/embed.js';
		ds.charset = 'UTF-8';
		(document.getElementsByTagName('head')[0] 
		|| document.getElementsByTagName('body')[0]).appendChild(ds);
	})();
	</script>
	<!-- Duoshuo Comment END -->
			
		</div>
	</div>
	<script src="./jquery/jquery-ui-1.8.17.custom.min.js"></script>
	<script src="./jquery/autocomplete/jquery.autocomplete.js"></script>
	<link href="./jquery/tagit/jquery.tagit.css" rel="stylesheet" type="text/css"/>
    <link href="./jquery/tagit/tagit.ui-zendesk.css" rel="stylesheet" type="text/css"/>
    <script src="./jquery/tagit/tag-it.js" type="text/javascript" charset="utf-8"></script>
    <script>
    $(function(){
    	
    	//使用脚本程序
    	//$('#tags').tagit(); 
    	$.get("./scripting/listTag.jss?itemId="+<%=itemId%>, //默认使用post,json对象其实会转变为form格式数据向后传
				function (data, textStatus){
					//alert(data.tags);
					$('#tagsForm').find('#tags').attr('value',data.tags); //修改内容
					$('#tagsForm').find('#tags').tagit(); //刷新显示？
    				//$("#tags").tagit("createTag",data.tags); //改用这个操作createTag
				}, 
			"json"); 
    	//
    	
    	$('#tagsForm').find('#tagsSubmit').click(function(){
			//alert("1 TODO: ADD SERVERSID:"+$(this).parent('#tagsForm').find('#tags').attr('value'));
			var tags=$(this).parent('#tagsForm').find('#tags').attr('value');
			//alert('2 将target的功能自己+自己的交互事件，尝试自己分装成自己的jquery插件');
			var itemId=<%=itemId%>;
			$.post("./scripting/addTags.jss", {action:'addTags',itemId:itemId, tags: tags }, //默认使用post,json对象其实会转变为form格式数据向后传
					function (data, textStatus){
						//data 当前为json对象(取决于后面一个参数datatype(返回值)使用的是json),可以是 xmlDoc, jsonObj, html, text, 等等.
						//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
						if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
							//alert("ok 增加tags成功，在itemid:"+itemId+"上。"); // add..sucess
							//alert("MSG:"+data.msg);
						}
					}, 
				"json"); 
		});
    	
    });
    </script>
	<div id="side_bar_right">
		side_bar_right
		<form id="tagsForm">
			<input name="tags" id="tags" value="">
            <input id="tagsSubmit" type="button" value="Submit"/>
        </form>
	</div>
	<div class="clear"></div> <!-- 保证同级的left在上级目录中被包括。 -->
</div>
</section>

<footer>
<div id="footer"> 
<div id="footer_stripe">
</div>
<div id="copyright">
沪ICP备12041334号 Copyright © 2012 - 2013 wfeng007 <br/> 
wfeng007@163.com <br/>
</div> 
</div>
</footer>

</body>


</html>
