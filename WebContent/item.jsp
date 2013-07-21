<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
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

//
if(itemId==null){
	itemId="";
}

//还是要获取一次本item的一些数据数据
RsItemService rsis=(RsItemService)SpringContextHolder.getApplicationContext().getBean("rsItemService");
RsItem rsItem=rsis.getItemById(Integer.valueOf(itemId));//需要校验？
//TODO 增加无法查询到指定id的item时的处理

%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="51youtu-login" />   
</jsp:include>

<!-- TODO删除？
<link rel="stylesheet" href="res/common_util.css"/> -->

<!-- 区域编辑 -->
<script src="./jquery/jeditable/jquery.autogrow.js"></script>
<script src="./jquery/jeditable/jquery.jeditable_summ.js"></script>
<script src="./jquery/jeditable/jquery.jeditable.autogrow.js"></script>

<jsp:include page="/neck.jsp" flush="true"/>

<style type="text/css">
/* #page_container {
	width: 1194px;
	margin: 0 auto;
	display:block;
} */
/* min-height: 800px; */
/* #item {
	float: left;
	
	width: 850px;
	border: 1px solid #eeeeee;
}
#side_bar_right {
	float: right;
	min-height: 600px;
	width: 330px;
	border: 1px solid #eeeeee;
} */
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

/* .clear {
 	clear: both;
} */
img#itemMedia {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 30px;
	margin-bottom: 30px;
	max-width: 800px;
}
#img_area{
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 0px;
	margin-bottom: 20px;
	max-width: 800px;
	background-image:url(res/image/img_bg_thick_40x40.png); 
	border: 1px solid #eeeeee;
}
/*#itemText*/
.edit_area {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 20px;
	margin-bottom: 20px;
	max-width: 800px;
	border: 1px solid #eeeeee;
}
#itemComments {
	display:block;
	/* margin: 0 auto; */
	margin-left: auto; margin-right: auto; /* 左右居中 */
	margin-top: 20px;
	margin-bottom: 20px;
	max-width: 800px;
	border: 1px solid #eeeeee;
}
</style>


<div class="container">

<div class="row">
	<div id="item" class="span11">
		<div id="img_area" class="">
			<img id="itemMedia" alt="<%=itemId%>" class=".clear" src="./svc/image?itemId=<%=itemId%>" />
		</div>
				
<script type="text/javascript">
$(function(){
	$('.editable').editable('./item!modifyText.act',{
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
         tooltip   : '点击区域添加内容',
         placeholder : '点击这里添加内容',
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
		<div class="edit_area">
		<h1>说说：</h1>
		<div id="<%=itemId%>" class="editable"><%=rsItem.getText()!=null?rsItem.getText():""%></div>
		</div>

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
	<div id="side_bar_right" class="span4">
		<form id="tagsForm" >
			<input name="tags" id="tags" value="" >
            <input id="tagsSubmit" type="button" value="Submit"/>
        </form>
	</div>
	<!--<div class="clear"></div>  保证同级的left在上级目录中被包括。 -->
</div><!-- row -->
</div>

<jsp:include page="/footer.jsp" flush="true"/>
