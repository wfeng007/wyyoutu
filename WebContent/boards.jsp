<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="summ.framework.*" %>
<%@ page import="summ.framework.util.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%@ page import="wyyoutu.dao.RsBoardDao" %>
<%@ page import="wyyoutu.model.RsBoard" %>
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
	//TODO 这里不应该为null，如果为null应该跳转。
}
//
String navPage="BOARDS";
pageContext.setAttribute("navPage",navPage); //设置到page范围的attrabute

//
//获取展板列表数据
RsBoardDao rsBoardDao=(RsBoardDao)SpringContextHolder.getApplicationContext().getBean("rsBoardDao");
Map<String,Object> pm=new HashMap<String,Object>();
pm.put("ownerId",accountInfo.getUserId()); //当前登录的session。TODO其实可以考虑使用该http-request-query查看特定人员的board。
List<RsBoard> boardList=rsBoardDao.query(pm);
//
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="boards" />   
</jsp:include>
<link rel="stylesheet" href="./jquery/masonry/style.masonry.css" />
<script src="./jquery/masonry/jquery.masonry.min.js"></script>
<jsp:include page="/neck.jsp" flush="true">
	<jsp:param name="nav_page"  value="${navPage}"/>
</jsp:include>
<style type="text/css">
#container .box {
	
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
	
	//设置瀑布布局
	$('#container').masonry({
		itemSelector : '.box',			
		isAnimated : false,
			/* animationOptions: {
			    duration: 750,
			    easing: 'linear',
			    queue: false
			  },  */
			  
		/* animationOptions: {
		    duration: 400
		}, */
		//cornerStampSelector: '.corner-stamp' //左上角的一个预留空间 样式为'.corner-stamp' //使用ajax方式展示
	});
	

	//TODO 设置按钮不可继续点击 或替换为进度环。不可按下。
	/*
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
	*/
	
	if(typeof(accountInfo)==='object' && typeof(accountInfo.userId)==="string"){	
	
		$("a#addBoardBtn").click(function(event){
			
			/* 新增应该用弹出模态窗口 bootstrap的一个插件
			http://v2.bootcss.com/javascript.html#modals
			*/
			
			//TODO 设置按钮不可继续点击 或替换为进度环。不可按下。
			$.post("./rjs/addBoard", {action:"add",owner:accountInfo.userId, boardName: "test" }, //默认使用post,json对象其实会转变为form格式数据向后传
				function (data, textStatus){ //回调
					//data 可以是 xmlDoc, jsonObj, html, text, 等等.
					//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
					//data 当前为json对象
					
					//
					//TODO 修改为跳转,刷新页面等。
					if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
						alert("ok! : "+data.msg); // 成功
						//if(typeof(data.redirect)==="string"){ //如果是跳转则可以用这种方式
						if(data.reload==true){
							//alert("ok:"+data.redirect); // 成功
							window.location.reload(true);
							//window.location.href=data.redirect+"?"+window.location.search; //跳转使用这个方式
						}
					}else{
						alert("failed : "+data.msg);
					}
					//
				}, 
			"json"); //要求返回值为json
		});
	}
	
});
</script>

<div id="container" class="container" >
	<div class="box col25 board"><h4>新增test</h4><hr><a role="button" data-toggle="modal" href="#myModal"><img alt="add" src="./res/image/folder_add.png"/></a></div>
<%
for (RsBoard rB : boardList){
%>
	<!-- 布局调整 -->
	<div class="box col25 board"><h4><%=rB.getName()%></h4><hr><a><img alt="board" src="./res/image/folder.png"/></a></div>
<%
}
%>
</div>
<!-- 新增模态界面 -->
<div id="myModal" class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">新增展板</h3>
	</div>
	<div class="modal-body">
		<h4>展板名字：</h4>
		<p><input id="boardName" type="text"/><a class="tooltip-test" title="" href="#" data-original-title="Tooltip">提示</a>
		</p>
		<h4>描述：</h4>
		<p><input id="boardDesc" type="text"/><a class="tooltip-test" title="" href="#" data-original-title="Tooltip">提示</a></p>
		<hr></hr>
		<h4>
		    说明：
		</h4>
		<p>描述可选。开发中。。。</p>
		<a id="addBoardBtn" class="btn" href="javascript:void(0);">新增</a>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
		<!-- <button class="btn btn-primary">提交</button> -->
	</div>
</div>
<!-- TODO 增加分页条 -->


<jsp:include page="/footer.jsp" flush="true"/>