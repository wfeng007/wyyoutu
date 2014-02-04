<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="summ.framework.*" %>
<%@ page import="summ.framework.util.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%@ page import="wyyoutu.service.RsItemService" %>
<%@ page import="wyyoutu.model.RsItem" %>

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


//session
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//mock FIXME 这里好像写死了。。
	accountInfo=new AccountInfo();
	accountInfo.setUserId("admin");
	accountInfo.setUserName("admin");
	//TODO 这里不应该为null，如果为null应该跳转。
}

//
String navPage="ALBUM";
pageContext.setAttribute("navPage",navPage); //设置到page范围的attrabute
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="album editor" />   
</jsp:include>
<style type="text/css">

		#dest, #src { min-height:110px; min-width:350px; width:550px; list-style-type:none; margin:0px;  background-color: #F93 }
		#dest li, #src li { float:left;  padding:5px; width:100px; height:100px;  }
		#dest div, #src div { width:90px; height:90px; border:solid 1px black; background-color:#E0E0E0; text-align:center; /* padding-top:40px; */ }
		#dest,#src {   float:left; }
		.placeHolder div { background-color:white !important; border:dashed 1px gray !important; }
	
		
		/* .phph {
			background-color: #BFB;
			border: 1px dashed #666;
			height: 58px;
			margin: 10px;
		}
		.sortable-list {
			background-color: #F93;
			list-style: none;
			margin: 0;
			min-height: 60px;
			padding: 10px;
		}
		.sortable-item {
			background-color: #FFF;
			border: 1px solid #000;
			height: 58px;
			cursor: move;	
			display: block;
			font-weight: bold;
			margin: 10px;
			padding: 0px 0; 
			text-align: center;
		}	
		#containment {
			background-color: #FFA;
			height: 230px;
		}
		 */
		.column {
			margin-left: 1%;
			width: 49%;
			/* border:solid 1px black; */
		}
		.left {float: left;}
		
		li img.thumb {
			width:100%; 
			height:100%;
		}
</style>
<script type="text/javascript" src="./jquery/jqui/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="./jquery/dragsort/jquery.dragsort-0.5.1.js"></script>

<jsp:include page="/neck.jsp" flush="true">
	<jsp:param name="nav_page"  value="${navPage}"/>
</jsp:include>
<style type="text/css">
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
	/*  border: 1px solid #eeeeee; */
}

.span15,
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

.span13 {
  width: 1020px;
}

.span14 {
  width: 1100px;
}

.span15 {
  width: 1180px;
}


</style>

<script type="text/javascript">
//on load document main
$(function() {	
	//$("ul#dragsort").dragsort();
	
	//拖放容器设置
	$("#dest, #src").dragsort({ 
		dragSelector: "div", 
		dragBetween: true,
		dragEnd: saveOrder,
		placeHolderTemplate: "<li class='placeHolder'><div></div></li>" 
	});
	$("input[name=destSortOrder]").val("");
	function saveOrder() {
		var data = $("#dest li").map(
				function() { return $(this).children().attr("itemId"); }).get();
		$("input[name=destSortOrder]").val(data.join(","));
	};
	saveOrder();
	
/* 	$('#example-1-3 .sortable-list').sortable({
		connectWith: '#example-1-3 .sortable-list',
		placeholder: 'phph'
	}); */
	
});
</script>

<div class="container">
<!-- <div class="row">
	<div class="span15">
	<h2>基本排序拖动</h2>
	<ul id="dragsort">
		<li>bread</li>
		<li>vegetables</li>
		<li>meat</li>
		<li>milk</li>
		<li>butter</li>
		<li>ice cream</li>
	</ul>
	</div>
</div>row -->

<%

//
//获取默认图册列表数据
RsItemService rsItemService=(RsItemService)SpringContextHolder.getApplicationContext().getBean("rsItemService");
Map<String,Object> pm=new HashMap<String,Object>();
pm.put("ownerId",accountInfo.getUserId()); //当前登录的session。TODO其实可以考虑使用该http-request-query查看特定人员的board。
pm.put("orderBy", "`seq_id`  DESC ");
pm.put("otherCondition", " AND `album_aid` is NULL "); //默认图册就是album_aid为空

Paging paging=new Paging();
paging.setCountPerPage(20);
paging.setCurrentPagePosition(1-1);
List<RsItem> itemList=rsItemService.listItem(pm,paging,false);
//
%>
<div class="row">
	<div class="span15">
		<h2>编辑图册内容</h2>
		<div>
			<form class="form-horizontal" >
				<div class="control-group">
					<label class="control-label">待保存内容：</label>
					<div class="controls">
						<input type="text"  name="destSortOrder"/>
						<button class="btn btn-primary" type="submit">保存</button>
					</div>
				</div> 
			</form>
		</div>
		<div class="column left first">
			<ul id="src">
<%
for (RsItem item : itemList){
	String usageurl="";
	String thumbnailurl="";
	if(item.getUrl()==null||"#".equalsIgnoreCase(item.getUrl())||"".equalsIgnoreCase(item.getUrl())){
		usageurl= "./svc/image?itemId="+item.getSeqId();
		thumbnailurl= "./svc/image?itemId="+item.getSeqId()+"&tq=75";  //对于只有url的而没有内容
	}else{
		thumbnailurl= item.getUrl();
	}
%>
			<li><div itemId="<%=item.getSeqId()%>">
					<img class="thumb" alt="test" src="<%=thumbnailurl%>" style= "clip:rect(0px 90px 90px 0px)">
			</div></li>
<% 
}
%>
			</ul>
		</div>
		<div class="column left">
				<ul id="dest">
<!-- 		<li><div itemId="1">1</div></li>
			<li><div itemId="2">2</div></li>
			<li><div itemId="3">3</div></li>
			<li><div itemId="4">4</div></li>
			<li><div itemId="5">5</div></li>
			<li><div itemId="6">6</div></li>
			<li><div itemId="7">7</div></li>
			<li><div itemId="8">8</div></li>
			<li><div itemId="9">9</div></li> -->
		</ul>
		
		</div>
	</div>
</div><!-- row -->
<!-- <div class="row">
	<div class="span15">
	<h2>编辑展板内容3</h2>
	<div id="example-1-3">
		<div class="column left first">
		<ul  class="sortable-list">
			<li class="sortable-item"><div>1</div></li>
			<li class="sortable-item"><div>2</div></li>
			<li class="sortable-item"><div>3</div></li>
			<li class="sortable-item"><div>4</div></li>
			<li class="sortable-item"><div>5</div></li>
			<li class="sortable-item"><div>6</div></li>
			<li class="sortable-item"><div>7</div></li>
			<li class="sortable-item"><div>8</div></li>
			<li class="sortable-item"><div>9</div></li>
		</ul>
		</div>
		<div class="column left">
		<ul  class="sortable-list">
			<li class="sortable-item"><div>10</div></li>
			<li class="sortable-item"><div>11</div></li>
			<li class="sortable-item"><div>12</div></li>
			<li class="sortable-item"><div>13</div></li>
			<li class="sortable-item"><div>14</div></li>
			<li class="sortable-item"><div>15</div></li>
			<li class="sortable-item"><div>16</div></li>
			<li class="sortable-item"><div>17</div></li>
			<li class="sortable-item"><div>18</div></li>
		</ul>
		</div>
	</div>
	</div>
</div> --><!-- row -->
</div>


<jsp:include page="/footer.jsp" flush="true"/>