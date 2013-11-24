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
//path
String path = request.getContextPath(); //应用所在contextpath， 不包括 前面协议IP域名端口部分。结尾没有斜杠。
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; //应用所在contextpath， 包括 前面协议IP域名端口部分。
//System.out.println(basePath);
String contextPath=path+"/";
String myPath=path+request.getServletPath(); //当前除去querypara的url
//System.out.println(myPath);

//session
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//out.println("need to login"); 这里不能再加载css之前输出否则会影响一些样式，甚至直接导致ie无法正常展示页面，原因不明。 
}

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

<script type="text/javascript">
//on load document main
$(function() {	
	
	
	<% //使用后台技术传递到前台。使用全局js变量
	if (accountInfo!=null) {%>
	accountInfo={};
	accountInfo.userId='<%=accountInfo.getUserId()%>';
	accountInfo.userName='<%=accountInfo.getUserName()%>';
	//alert(accountInfo.userId);
	<%} %>
	

	//TODO 设置按钮不可继续点击 或替换为进度环。不可按下。
	$.get("./rjs/listBoard", {action:"list",owner:accountInfo.userId }, //默认使用post,json对象其实会转变为form格式数据向后传
		function (data, textStatus){ //回调
			//data 可以是 xmlDoc, jsonObj, html, text, 等等.
			//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
			//data 当前为json对象
			
			//TODO 修改为跳转,刷新页面等。
			if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
				alert("ok"); // 删除成功效果。
			}else{
				alert("failed");
			}
			//
		}, 
	"json"); //要求返回值为json
	
	
	if(typeof(accountInfo)==='object' && typeof(accountInfo.userId)==="string"){
		$("a#addBoardBtn").click(function(event){
			//TODO 设置按钮不可继续点击 或替换为进度环。不可按下。
			$.post("./rjs/addBoard", {action:"add",owner:accountInfo.userId, boardName: "test" }, //默认使用post,json对象其实会转变为form格式数据向后传
				function (data, textStatus){ //回调
					//data 可以是 xmlDoc, jsonObj, html, text, 等等.
					//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
					//data 当前为json对象
					
					//TODO 修改为跳转,刷新页面等。
					if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
						alert("ok"); // 删除成功效果。
					}else{
						alert("failed");
					}
					//
				}, 
			"json"); //要求返回值为json
		});
	}
	
});
</script>

<div id="container" class="container" >
	<div class="box col25 board"><a id="addBoardBtn" href="javascript:void(0);"><img alt="add" src="./res/image/folder_add.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
	<div class="box col25 board"><a><img alt="board" src="./res/image/folder.png"/></a></div>
</div>

<jsp:include page="/footer.jsp" flush="true"/>