<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>

<%
//TODO 后期应该抛开session 从后台获取accountinfo来判断是否有session 后期不一定使用httpsession作为session判断
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);

if(accountInfo==null){
	out.println("need to login");
}

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>51youtu</title>
<link rel="stylesheet" href="res/basic.css">
<link rel="stylesheet" href="res/common_util.css">
<link rel="stylesheet" href="res/header2.css">
<link rel="stylesheet" href="res/signin.css" />
<link rel="stylesheet" href="res/footer.css">
<link rel="stylesheet" href="./jquery/masonry/style.masonry.css" />

<link href="./jquery/lightbox/lightbox.css" rel="stylesheet" />

<!-- 上传box框样式    暂时用fineuploader
<link href="./jquery/uploader/css/styles2.css" rel="stylesheet" />
-->
<!-- 上传控件    -->
<link href="./jquery/fineuploader/fineuploader2.css" rel="stylesheet">

<link href="./jquery/jPaginator/jPaginator.css" rel="stylesheet" />


<style type="text/css"></style>

<!-- 对瀑布布局中 内容box 的item 内部样式定义 -->
<link href="./res/item.css" rel="stylesheet" />
<!-- item 内部样式定义  结束-->

<!-- TODO 一下内容为检测屏幕大小来调整 页面宽度的参考  -->
<script type="text/javascript">
//var isWidescreen=screen.width>=1280; 
//if(isWidescreen){document.write("<style type='text/css'>.demo{width:1194px;}</style>");} //1194 or 990
</script><style type="text/css">.demo{width:1194px;}</style>

<!-- js -->
<script src="./jquery/jquery-1.7.2.min.js"></script>
<script src="./jquery/masonry/jquery.masonry.min.js"></script>
<script src="./jquery/masonry/jquery.masonry.corner.js"></script>
<script src="./jquery/lightbox/lightbox.js"></script>

<!-- drop file uploader 暂时用fineuploader
<script type="text/javascript" src="jquery/uploader/js/jquery.filedrop.js"></script>
-->

<!-- jPaginator all -->
<!-- for jPaginator -->
<script src="./jquery/jPaginator/slider/jquery.ui.core.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.widget.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.mouse.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.slider.min.js"></script>
<!-- jPaginator -->
<script src="./jquery/jPaginator/jPaginator.js"></script>

<!-- fineuploader (uploader2) -->
<script src="./jquery/fineuploader/header.js"></script>
<script src="./jquery/fineuploader/util.js"></script>
<script src="./jquery/fineuploader/button.js"></script>
<script src="./jquery/fineuploader/handler.base.js"></script>
<script src="./jquery/fineuploader/handler.form.js"></script>
<script src="./jquery/fineuploader/handler.xhr.js"></script>
<script src="./jquery/fineuploader/uploader.basic.js"></script>
<script src="./jquery/fineuploader/dnd.js"></script>
<script src="./jquery/fineuploader/uploader.js"></script>
<script src="./jquery/fineuploader/jquery-plugin.js"></script>

<!-- 一小部分可共用的工具程序  -->
<script src="./res/util.js"></script>
<!-- 早前用来上传的filedrop的ui扩展 暂时用fineuploader 
<script src="./res/dropbox.js"></script> -->

<script type="text/javascript">
//这个其实是一个 界面效果 功能 相对独立 
//瀑布的每个box中的一个css动态效果需要在每个选择器生成后 让其生效
$(function(){
	
	// 上传2
	$('#jquery-wrapped-fine-uploader').fineUploader({
		request: {
			endpoint: './svc/upload?retType=json'
		},
		text:{
			uploadButton:'上传图片',
			dragZone:'拖动这里'
		},
		validation: {
			 allowedExtensions: ['jpeg', 'jpg', 'png','bmp'],
			 sizeLimit: 8*1024*1024 //
		},
//		uploaderType:"basic",
		//showMessage:function(message) {
			//alert(message);
			// Using Bootstrap's classes
//			$('#restricted-fine-uploader').append('<div class="alert alert-error">' + message + '</div>');
//		},
		debug: true
	}).on('complete', function(event, id, fileName,responseJSON) {
		$('.qq-upload-list').hide() //不显示 提示列表
		
		//
		//TODO 想办法显示新的图片内容等。 如果扩展dropbox 应该直接可以用界面img数据，但是用fineuploader 怎么办？读后台？
		//TODO 从后台取得话其实在上传成功后无法返回itemid 因为itemid是自增长的seqid 需要考虑其他ID同样能够读到图片
		//
//		alert(id);
//		alert(fileName);
//		alert(responseJSON.success);
/* 		if (responseJSON.success) {
			$(this).append('<img src="img/success.jpg" alt="' + fileName + '">');
		} */
		//
		$('#container').masonry( 'reload' );
    }).on('progress', function(event, id, fileName) {
		$('#container').masonry( 'reload' );
    }).on('error', function(event, id, fileName) {
		$('#container').masonry( 'reload' );
    });
	
	/*  设置 item的特效 
	鼠标进入item 蒙版操作按钮显示 整个item阴影显示 	*/
	function item_callback(){ 
		$('.item').mouseover(function(){
//			$(this).css('box-shadow', '0 1px 5px rgba(35,25,25,0.5)');
			$('.btns',this).show();
//			$('.btns',this).fadeIn();
		})
		$('.item').mouseout(function(){
//			$(this).css('box-shadow', '0 1px 3px rgba(34,25,25,0.2)');
			$('.btns',this).hide();		 
//			$('.btns',this).fadeOut();	
		});
		//item_masonry();	
	}
	item_callback();  

//	$('.item').fadeIn();

});

//on load document main
	$(function() {	
		
	
		//session check
		// TODO 修改为后台验证显示
		// 获取session信息
		// 并根据session信息
		// 界面变化
		//
		//jQuery.post(url,data,success(data, textStatus, jqXHR),dataType)
/* 		$.post("./session!session.act", { Action: "post", Name: "lulu" }, //默认使用post的json对象其实会转变为form格式数据向后传
				function (data, textStatus){
				//data 可以是 xmlDoc, jsonObj, html, text, 等等.
				//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
				//alert(JSON.stringify(data)); // jquery直接将数据转化为对象
				if(data!=null&&typeof(data)==="object"&&typeof(data.userId)==="string"){ //类型判断
					$("#topnav #userId").html(data.userId);
					
					// 显示用户登录后的面板 需要其他js配置
					// 显示用户信息框 ( stampcorner)
					//
					// TODO 单独形成模板
					//
					$('#container').prepend("<div class='corner-stamp' >"
							+"<div class='rel'>"
					+"<div id='userInfo'></div>"
					+"<img class='portrait' alt='头像' src='./res/image/portrait_def.jpg' >"
					+"</div>"
					+"</div>");
					
					$('#container').masonry({cornerStampSelector: ".corner-stamp" });//显示 瀑布角落内容
					$('#container #userInfo').html("用户："+data.userId+"您好！");
					//$('#container').masonry( 'reload' );
					//
				}
		}, "json"); */
		//session check end
		
		<% //使用后台技术传递到前台。使用全局js变量
		if (accountInfo!=null) {%>
		accountInfo={};
		accountInfo.userId='<%=accountInfo.getUserId()%>';
		accountInfo.userName='<%=accountInfo.getUserName()%>';
		//alert(accountInfo.userId);
		<%} %>

		
		
		//登录框 设置  以后考虑作为 jquery的插件
	    $(".signin").click(function(e) {          
			e.preventDefault();
               $("fieldset#signin_menu").toggle();
			$(".signin").toggleClass("menu-open");
           });
		
		$("fieldset#signin_menu").mouseup(function() {
			return false
		});
		$(document).mouseup(function(e) {
			if($(e.target).parent("a.signin").length==0) {
				$(".signin").removeClass("menu-open");
				$("fieldset#signin_menu").hide();
			}
		});
		//登录框结束
		
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
		
		//
		//检测登录信息并追加瀑布布局中的corner-stamp 注意这个其实要在 $('#container').masonry()第一次初始化后执行，否则界面会因为初始化设置与后续设置相反二出问题。
		if(typeof(accountInfo)!='undefined'&&accountInfo!=null){
			//$("#topnav #userId").html(accountInfo.userId);
			$('#container').prepend("<div class='corner-stamp' >"
					+"<div class='rel'>"
			+"<div id='userInfo'></div>"
			+"<img class='portrait' alt='头像' src='./res/image/portrait_def.jpg' >"
			+"</div>"
			+"</div>");
			
			$('#container').masonry({cornerStampSelector: ".corner-stamp" });//显示 瀑布角落内容
			$('#container #userInfo').html("用户："+accountInfo.userId+"您好！");
			//$('#container').masonry( 'reload' );
		}
		//
		
		
		//查询数据将一组数据放入瀑布布局 可能要反复使用 并刷新
		fnRenderListItem=function(data){
			var items = "";
			//得到数据并逐个绘制到瀑布布局中
			$.each(data.rows,function(idx,item){	
				//masonry可以一次加载多个box
				items+='<!--item box start-->'
					+'<div class="box col25 item" itemId="'+item.seqId+'">'
					+'<!-- top 显示图片 -->'
					+'<div class="item_t">'
						+'<div class="img">'
							+'<a href="'+item.thumbnailUrl+'" rel="lightbox" title="'+item.name+'">'
								+'<img alt="test" src="'+item.thumbnailUrl+'" >'
							+'</a>'
							+'<span class="price">有图有真相</span>'
							+'<div class="btns" >'
								+'<a href="'+item.url+'" class="img_album_btn_left" target="_blank" title="下载"/>'
								+'<a href="./item.jsp?itemId='+item.seqId+'" class="img_album_btn" target="_blank" title="加入专辑"/>'
							+'</div>'
						+'</div>'
						+'<div class="tilte"  style="word-break:break-all;">'
						+'<span>'+item.name+':<br/>'+item.text+'</span> '
						+'<a class="showEditor" href="javascript:void(0)" title="编辑">编辑</a> '
						+'<form itemId="'+item.seqId+'" class="editor" style="display:none">'
						+'<textarea class="editor" rows="4" style="width:95%;overflow-y:visible;resize:none;"></textarea>'
						+'<input class="save" type="button" onclick="" value="保存"/> <input class="reset" type="button" onclick="" value="放弃"/>'
						+'</form>'
						+'</div>'
					+'</div>'
					+'<!-- bottom 常显菜单或内容 -->'
					+'<div class="item_b clearfix">'
						+'<div class="items_likes fl">'
							+'<!-- <a href="#" class="like_btn"></a><em class="bold">916</em> -->'
						+'</div>'
						+'<!-- <div class="items_comment fr"><a href="#">底部备用按钮</a><em class="bold">(0)</em></div>-->'
						+'<a id="delete" itemId="'+item.seqId+'" class="box_btn_bright delete" href="javascript:void(0);" title="删除"></a>'
					+'</div>'
				+'</div>'
				+'<!--item box end-->'
				
				//以下是一个代码说明没有业务意义 忽略了第一条数据
				if(idx==0){
					return true;//同countinue，返回false同break
				}
			}); //foreach end
			
			var $tt=$(items);
			
			// 设置效果 应该只是给新增的部分增加这个效果即可 //find只遍历子元素   filter遍历同级元素
			$tt.filter(".item").mouseover(function() {
			//$('.item', $tt).mouseover(function() { //$('.item', $tt) 等同于 $tt.find()
				//$(this).css('box-shadow', '0 1px 5px rgba(35,25,25,0.5)');
				$('.btns', this).show();
				//$('.btns',this).fadeIn();
			})
			$tt.filter(".item").mouseout(function() {
			//$('.item', $tt).mouseover(function() {
				//$(this).css('box-shadow', '0 1px 3px rgba(34,25,25,0.2)');
				$('.btns', this).hide();
				//$('.btns',this).fadeOut();	
			}); //
			
			//
			//注册按钮的功能
			
			//注册删除按钮功能
			//alert($tt.find("a.delete").length);
			$tt.find("a.delete").click(function(event){
					//var oevent = event.originalEvent;
					//alert(oevent.target || oevent.srcElement);
					//alert("attr:"+$(oevent.target).attr("itemId"));
					//alert("attr:"+$(event.target).attr("itemId"));、
				 	//alert("attr:"+$(this).attr("itemId")); //都可以
				 	//outerHTML()获取整个html
				 	
					var itemId=$(this).attr("itemId");
					//alert("a");
					$.post("./item!removeItem.act", {action:"remove",itemId:itemId, itemIid: "?" }, //默认使用post,json对象其实会转变为form格式数据向后传
						function (data, textStatus){
							//data 可以是 xmlDoc, jsonObj, html, text, 等等.
							//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
							//data 当前为json对象
							if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
								alert("ok 删除成功效果"+itemId); // 删除成功效果。
								
								//
								// 界面效果 从masonery中删除 对应item的box
								//
								$('#container').children('div[itemId="'+itemId+'"]').remove();
								$('#container').masonry( 'reload' );
								//
								//
								
							}
						}, 
					"json"); //要求返回值为json
				}
			);
			
		
			//
			//TODO 以下编辑框、显示、保存、放弃功能 修改为 插件 jeditable分装  http://www.appelsiini.net/projects/jeditable
			//
			//注册显示编辑框的按钮
			//
			$tt.find("a.showEditor").click(function(){
				//
				//隐藏 文本内容 显示Textarea并将文本内容显示在Textarea中。
				//
				var txtCont =$(this).prev();
				$(this).next("form").children("textarea.editor").html(txtCont.html());
				$(this).next("form").css("display","block")
				//文本框隐藏
				txtCont.hide();
				//隐藏 展开a.showEditor
				$(this).hide();
				
				//这里需要保证$tt在这个布局对象中
				$('#container').masonry( 'reload' );
			});
			
			// 注册编辑框resize动作的影响
			//$tt.find("textarea.editor").resize(function(){});
			//
			// 注册 editor保存 按钮功能
			//
			$tt.find("form.editor input.save").click(function(){
				
				// 整个form
				var formEditor =$(this).parent();
			
				//
				var itemId=formEditor.attr("itemId");
				
				//获取文本框中的内容
				var text = formEditor.children("textarea.editor").val();
				
				
				// 提交更新文本内容
 				$.post("./item!modifyText.act", {action:"modify",itemId:itemId,"text": text}, //默认使用post,json对象其实会转变为form格式数据向后传
						function (data, textStatus){
							//data 可以是 xmlDoc, jsonObj, html, text, 等等.
							//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
							//data 当前为json对象
							if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
								//alert("ok 修改成功"); // 测试 
								
								//放在这里显示内容
								formEditor.prev().prev().html(text);//显示新内容 显示的时候转义而已 
								
								
								
							}else{
								alert("修改失败！"); // 测试 
								//
								// 
								//
							}
							
							//内容变更后也要执行重新布局
							$('#container').masonry( 'reload' );
						}, 
				"json"); //要求返回值为json
				
				
				//
				//隐藏 表单
				//
				formEditor.children("textarea.editor").val(text); //
				formEditor.css("display","none")
				
				// 显示 文本 
				//formEditor.prev().prev().html(text);//显示新内容
				formEditor.prev().prev().show();
				// 
				formEditor.prev().show();
				
				
				//这里需要保证$tt在这个布局对象中
				$('#container').masonry( 'reload' );
			});
			//
			// 注册 editor放弃 按钮功能
			//
			$tt.find("form.editor input.reset").click(function(){
				//
				//隐藏 表单
				//
				var formEditor =$(this).parent();
				formEditor.children("textarea.editor").val(formEditor.prev().prev().html());
				formEditor.css("display","none")
				
				//显示 文本
				formEditor.prev().prev().show();
				formEditor.prev().show();
				
				
				//这里需要保证$tt在这个布局对象中
				$('#container').masonry( 'reload' );
			});
			// 
			
			////
			//注册其他按钮功能
			////
			//...
			////
			
			//添加到masonry
			$('#container').append( $tt ).masonry( 'appended', $tt ,false);
			
			//
			// 图片加载完成后 重新render
			// 
			// masonry内置了imagesLoaded jquery插件 会根图片$images.length 判断是马上展示还是等待img显示
			//!!!在 masonry容器中的image都已经载入然后重新“render“,否则 如果image还么有载入容易出现，render 出的box图像重叠。（因为box还没有固定尺寸）
			//!!! ie8 图片读取会出现
/* 			$('#container').imagesLoaded(function(){
				//$(this).masonry({itemSelector : '.box'});  
				$(this).masonry( 'reload' );
			});  */
			// 用 imagesLoaded 似乎ie8会出现 connect reset问题 应该是 
 			$('#container img').load(function(){ //子层选择器
 				//考虑 使用一个预置图片，然后在这里替换。
				$("#container").masonry( 'reload' );
				//$("#container").fadeIn();
			});  
			// 等待图片加载
			//
			
		}
		//后台获取项目内容
		//读出数据逐一放入 container
		userIdPara="";
		if(typeof(accountInfo)!='undefined'&&accountInfo!=null){
			userIdPara="peopleId="+accountInfo.userId;
		}
		
		$.getJSON("./item!listItem.act"+"?"+userIdPara, fnRenderListItem); //注册一个全局函数
		
		//查询并放入 

		// 分页组件
		// jPaginator 依赖于 jquery-ui的slider jquery-ui-1.8.13.slider.min.js
		jPaginatorB = {
			selectedPage : 1,
			nbPages : 12,
			nbVisible : 10,
			overBtnLeft : '#pagor_o_left',
			overBtnRight : '#pagor_o_right',
			maxBtnLeft : '#pagor_m_left',
			maxBtnRight : '#pagor_m_right',
			withSlider : true,
			withAcceleration : true,
			speed : 1,
			coeffAcceleration : 2,
			minSlidesForSlider : 3,
			onPageClicked : function(a, num) {

				//换页时去除所有之前的item内容
				$('#container .item').remove(); //只删除 item 类
				$('#container').masonry('reload');//重新布局 防止出现  ”空位“
				//

				//这里是因为fnRenderListItem内部用了reload 所以重新render
				//TODO 还是用页面跳转吧 location.reXX,AJAX这个paging插件支持的不好
				//况且 对于互联网应用 可能需要改成原始的分页组件 （自己简单画的即可）+ 滚轴风格的分页
				//
				$.getJSON("./item!listItem.act?pageNum=" + num
						+ "&numPerPage=10"+"&"+userIdPara, fnRenderListItem);
				//如自定义该paging插件，可能要保留现有风格以及增加一个ajax 缓存状态的风格？ 因为如果是简单分页功能 不需要保存页面状态
				//如果想用ajax 可能要考虑缓存 以及缓存对象反复操作后的情况。 jquery会缓存每个新生成的jquery中新增的属性么？ 应该会吧 如果不会则需要插件中缓存。
				//这个分页插件无法在点击按钮后修改其本身的属性？
				//
				//只能用如下恶心方法 
				//jPaginatorB.nbPages=20;
				//jPaginatorB.selectedPage=num; 
				//a.jPaginator(jPaginatorB);
				//
				//  
			}
		}
		$("#pagor").jPaginator(jPaginatorB);

	});
</script>

<div id="header2">
	<!-- -->
	<div id="brand">
		<div class="section-wrap">
			<div class="logo">v:0.4(α)
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

<!-- 				<li class="split"></li>
				<li class="multi-nav">

					<div class="nav-trigger">
						<a href="javascript:void(0);">大家看</a> <span class="icon"></span>
					</div>
					<div class="more-nav">
					分类的菜单
						<ul class="topic clearfix">
							<li><a
								href="#">最热</a><span class="hot">这里是图片</span>
							</li>
						</ul>
						<ul class="category clearfix">
							<li><a href="#">服饰</a></li>
							<li><a href="#">家居</a></li>
							<li><a href="#">创意DIY</a></li>
						</ul>
						 
					</div>
					</li> -->
<!-- 				<li class="split"></li>

				<li><a
					href="#">豆豆图库</a>
				</li>

				<li class="split"></li>

				<li><a
					href="#">其他</a>
				</li> -->
			</ul>
			<div class="search-bar">
<!-- 		搜索框之后实现
			<form id="pix-search-form" class="search clearfix"
					action="#" method="get">
					<input id="pix-search-input" name="q" placeholder="搜图片 / 图集 / 找人"
						maxlength="30" type="text">
					<button type="submit"></button>
				</form> -->
			</div>
		</div>
	</div>
</div>
</head>


<body>
<style type="text/css">
/*一个相对定位框 内部可以放绝对定位框*/
div.rel{
	position: relative;
}
img.portrait{
	top: 5px;
	right: 5px;
	height: 100px;
	width: 100px;
	position: absolute;
}
</style>

<style type="text/css">
/*上传按钮 2*/

</style>
	<section id="content">
		<div id="container" class="clearfix" >
		
<!-- cornerstamp 使用jquery写入 不是直接显示的首先
	<div class="corner-stamp" >
	<div class="rel">
		<div id="userInfo"></div>
		<img class="portrait" alt="头像" src="./res/image/portrait_def.jpg" >
		</div>
	</div>
	  -->
	
<style type="text/css">
/* .userpanel{
	display:none;
} */
</style>	

			<div id="dropEditor" class="box col25 userpanel">
<!-- 暂时不用dropbox 但后续还是以扩展这个dropbox为好 暂时只用 fine-uploader ，但dropbox可以扩展出需要的界面效果。fine-uploader扩展效果比较麻烦。
			  <div id="dropbox">
				<span class="message">...建设中...</span>
			  </div> -->
			  <div id="jquery-wrapped-fine-uploader">上传</div>
			</div>
			
			<!--item start
			<div class="box col25 item">
				<div class="item_t">
					<div class="img">
						<a href="http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg">
							<img alt="test" src="http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg" >
						</a>
						<span class="price">￥195.00</span>
						<div class="btns">
							<a href="http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg" class="img_album_btn">加入专辑</a>
							<a href="http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg" class="img_album_btn_left">左上角</a>
						</div>
					</div>
					<div class="title"><span>图片标题 或基本内容</span></div>
				</div>
				
				<div class="item_b clearfix">
					<div class="items_likes fl">
						<a href="#" class="like_btn"></a><em class="bold">916</em>
					</div>
					<div class="items_comment fr"><a href="#">评论底部按钮</a><em class="bold">(0)</em></div>
				</div>
			</div>
			-->
			<!--item end-->

			
		</div>
		<!-- #container -->
				

<link href="./res/paginator.css" rel="stylesheet" />

  <!-- paginator  -->	
  <div id="pagor" class="clearfix" >
      <!-- optional left control buttons-->
      <a id="pagor_m_left"></a><div id="pagor_o_left"></div>
      <div class='paginator_p_wrap'>
        <div class='paginator_p_bloc'>
          <!--<a class='paginator_p'></a> // page number : dynamically added -->
        </div>
      </div>
      <!-- optional right control buttons-->
      <div id="pagor_o_right"></div><a id="pagor_m_right"></a>
      <!-- slider -->
      <div class='paginator_slider' class='ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all'>
        <a class='ui-slider-handle ui-state-default ui-corner-all' href='#'></a>
      </div>
  </div>
	
	<!-- paginator end -->

</section>
	<!-- #content -->

</body>

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

</html>