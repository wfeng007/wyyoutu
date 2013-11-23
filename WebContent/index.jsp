<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="wyyoutu.web.AccountInfo" %>
<%!
//public String wf="wf";
%>
<%
String path = request.getContextPath(); //应用所在contextpath， 不包括 前面协议IP域名端口部分。结尾没有斜杠。
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; //应用所在contextpath， 包括 前面协议IP域名端口部分。
System.out.println(basePath);
String contextPath=path+"/";
String myPath=path+request.getServletPath(); //当前除去querypara的url
System.out.println(myPath);

//TODO 后期应该抛开session 从后台获取accountinfo来判断是否有session 后期不一定使用httpsession作为session判断
AccountInfo accountInfo=AccountInfo.lookupAccountInfo(request);
if(accountInfo==null){
	//out.println("need to login"); 这里不能再加载css之前输出否则会影响一些样式，甚至直接导致ie无法正常展示页面，原因不明。 
}

//
//querypara
//
//特定用户参数
String owner=request.getParameter("owner");

// 获取url中的查询参数，比如 pageNum=" + num+ "&numPerPage=10 这样类似参数
// 当前页实际页数：
String pns=request.getParameter("pageNum");
int pn=1;
if(pns==null || "".equals(pns)||!StringUtils.isNumeric(pns)){
	pn=1;
}else{
	pn=Math.abs(NumberUtils.stringToInt(pns));
}
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="51youtu-index" />   
</jsp:include>

<!-- <link rel="stylesheet" href="res/common_util.css"> -->
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
<!-- jquery -->
<!-- bootstrap-js in include!-->
<!-- masonry -->
<script src="./jquery/masonry/jquery.masonry.min.js"></script>
<script src="./jquery/masonry/jquery.masonry.corner.js"></script>
<!-- infinitescroll -->
<script src="./jquery/infinitescroll/jquery.infinitescroll.js"></script>
<!-- lightbox -->
<script src="./jquery/lightbox/lightbox.js"></script>
<!-- drop file uploader 暂时用fineuploader
<script type="text/javascript" src="jquery/uploader/js/jquery.filedrop.js"></script>
-->

<!-- jPaginator all -->
<!-- for jPaginator jquery.ui. -->
<script src="./jquery/jPaginator/slider/jquery.ui.core.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.widget.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.mouse.min.js"></script>
<script src="./jquery/jPaginator/slider/jquery.ui.slider.min.js"></script>
<!-- jPaginator -->
<script src="./jquery/jPaginator/jPaginator.js"></script>

<script src="./jquery/bootstrap/js/jquery.bootpag.js"></script>

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
		
		//

		
		//检测登录信息并追加瀑布布局中的corner-stamp 注意这个其实要在 $('#container').masonry()第一次初始化后执行，否则界面会因为初始化设置与后续设置相反二出问题。
		if(typeof(accountInfo)!='undefined'&&accountInfo!=null){
			
			//user profile html
			var userProfile="<div class='corner-stamp' >"
						+"<div class='rel'>" 
				+"<div class='marg'/>"
				+"<div  class='prof'>"
				+"<div id='userInfo' class='profinfo'>"
				+"<p class='text-info'><span class='label label-info'>用户：</span><strong>"+accountInfo.userId+"</strong></p>"
				+"<p class='text-success'>"+accountInfo.userName+"<span class='label label-success'>你好！</span></p>"
				+"</div>"
				+"</div>"
				+"<div class='proffun'>"
				+"<img class='portrait' alt='头像' src='./res/image/portrait_def.jpg' >"
				+"</div>"
				
				+"</div>"
				+"</div>";
			//	
		
			//$("#topnav #userId").html(accountInfo.userId);
			$('#container').prepend(userProfile);
			$('#container').masonry({cornerStampSelector: ".corner-stamp" });//显示 瀑布角落内容
			
			//$('#container').masonry( 'reload' );
		}
		//
		
		//返回状态按钮内容
		var _publishType=function(status){
			if(status==1){
				return '<i class="icon-eye-open"></i>公';
			}else{
				return '<i class="icon-eye-close"></i>私';
			}
		};
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
							+'<a href="./item.jsp?itemId='+item.seqId+'" target="_blank" title="'+item.name+'">'
								+'<img alt="test" src="'+item.thumbnailUrl+'" >'
							+'</a>'
							+'<span class="price">有图有真相</span>'
							+'<div class="btns" >'
								+'<div class="img_album_panel_left">'
									+'<a href="'+item.thumbnailUrl+'"  class="btn btn-small img_album_btn" rel="lightbox" title="'+item.name+'">展<i class="icon-fullscreen"></i></a>'
								+'</div>'
								+'<div class="img_album_panel_right">'
									+'<a href="'+item.url+'" class="btn btn-small img_album_btn" target="_blank" title="下载"><i class="icon-download-alt"></i>下</a>'
								+'</div>'
							+'</div>'
						+'</div>'
						+'<h5 class="" style="word-break:break-all;">'+item.name+'</h5>'
						+'<div class="title"  style="word-break:break-all;">'
						+'<p>'+item.text+'</p>'
						+((typeof(accountInfo)==='object' && (typeof(accountInfo.userId))==='string' && item.ownerId==accountInfo.userId)?'<a class="showEditor" href="javascript:void(0)" title="编辑">编辑</a> ':"")
						+'<form itemId="'+item.seqId+'" class="editor" style="display:none">'
						+'<textarea class="editor" rows="4" style="overflow-y:visible;resize:none;"></textarea>'
						+'<input class="btn btn-mini btn-primary save" type="button" onclick="" value="保存"/> <input class="btn btn-mini reset" type="button" onclick="" value="放弃"/>'
						+'</form>'
						+'</div>'
					+'</div>'
					+'<!-- bottom 常显菜单或内容 -->'
					+'<div class="item_b clearfix">'
						+'<div class="items_likes fl">'
							+'<!-- <a href="#" class="like_btn"></a><em class="bold">916</em> -->'
						+'</div>'
						+'<!-- <div class="items_comment fr"><a href="#">底部备用按钮</a><em class="bold">(0)</em></div>-->'
						+((typeof(accountInfo)==='object' && (typeof(accountInfo.userId))==='string' && item.ownerId==accountInfo.userId)?('<a itemId="'+item.seqId+'" status="'+item.status+'" href="javascript:void(0);" class="btn btn-small btn_left doPublishToggle" title="切换">'+_publishType(item.status)+'</a>'):"")
						+((typeof(accountInfo)==='object' && (typeof(accountInfo.userId))==='string' && item.ownerId==accountInfo.userId)?('<a itemId="'+item.seqId+'" class="btn btn-small btn-danger btn_right doDelete" href="javascript:void(0);" title="删除">删<i class="icon-trash icon-white"></i></a>'):"")
					+'</div>'
				+'</div>'
				+'<!--item box end-->'
				//TODO 以上的某些控制面板中功能在后台也需要做验证，使用不同权限控制不同的功能。当前暂时使用界面的session信息控制展示。
				//TODO 之后页面需要提供单独进行权限分析的区域达到逻辑单独地方处理的效果。
				
				
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
			if(typeof(accountInfo)==='object' && (typeof(accountInfo.userId))==='string'){ //TODO 临时的权限控制,当已经刚登时才考虑设置事件 
				
				$tt.find("a.doDelete").click(function(event){
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
				//注册发布按钮功能
				$tt.find("a.doPublishToggle").click(function(event){
						//var oevent = event.originalEvent;
						//alert(oevent.target || oevent.srcElement);
						//alert("attr:"+$(oevent.target).attr("itemId"));
						//alert("attr:"+$(event.target).attr("itemId"));、
					 	//alert("attr:"+$(this).attr("itemId")); //都可以
					 	//outerHTML()获取整个html
					 	var item=$(this);
						var itemId=item.attr("itemId");
					 	var status=item.attr("status");
						//alert("a");
						$.post("./item!publishToggle.act", {action:"publish",itemId:itemId, itemIid: "?" ,status:status}, //默认使用post,json对象其实会转变为form格式数据向后传
							function (data, textStatus){
								//data 可以是 xmlDoc, jsonObj, html, text, 等等.
								//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
								//data 当前为json对象
								if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断
									alert("ok 发布状态切换成功!"+itemId); 
									//
									// 界面效果 从masonery中删除 对应item的box
									//
									//$('#container').masonry( 'reload' ); // TODO 不应该刷新masonry而应该设置当前按钮状态
									//alert(data.data); //data中为newStatus
									item.html(_publishType(data.data));//状态1指发布
									item.attr("status",data.data);
									//
								}else{
									//处理错误情况
									alert("ok 发布状态切换失败!id:"+itemId); 
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
					$(this).next("form").children("textarea.editor").html(txtCont.html());//内容显示在
					$(this).next("form").css("display","block")
					//文本框隐藏//隐藏 a.showEditor按钮
					txtCont.hide();
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
					//获取itemid
					var itemId=formEditor.attr("itemId");
					//获取文本框中的内容
					var text = formEditor.children("textarea.editor").val();
					//alert(text);
					// 提交更新文本内容
	 				$.post("./item!modifyText.act", {action:"modify",itemId:itemId,"text": text}, //默认使用post,json对象其实会转变为form格式数据向后传
							function (data, textStatus){
								//data 可以是 xmlDoc, jsonObj, html, text, 等等.
								//this; 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
								//data 当前为json对象
								//alert(data.success);
								//
								//window.location.href='\\';过是ajax的请求可以使用 该方法让浏览器跳转到指定页面，比如登录框。似乎很多系统提供一个延迟刷新的面板？
								//
								if(data!=null&&typeof(data)==="object"&&data.success==true){ //类型判断 //需要区分ajax方式与普通跳转方法。
									//alert("ok 修改成功"); // 测试 
									
									//放在这里显示内容
									formEditor.prev().prev().html(text);//显示新内容 显示的时候转义而已 
								}else{
									alert("修改失败！"); // 测试 
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
					formEditor.css("display","none");
					
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
					formEditor.css("display","none");
					
					//显示 文本
					formEditor.prev().prev().show();
					formEditor.prev().show();
					
					
					//这里需要保证$tt在这个布局对象中
					$('#container').masonry( 'reload' );
				});
				// 
			}//权限相关按钮结束
			
			
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
			
		};//fnRenderListItem=function(data) end
		
		
		
		//后台获取项目内容
		//读出数据逐一放入 container
		
		//具体用户作为参数
		//userIdPara="";
		/*
		if(typeof(accountInfo)!='undefined'&&accountInfo!=null){
			userIdPara="owner="+accountInfo.userId;
		}
		*/
		userIdPara='<%=(owner==null)?"":"&owner="+owner%>';
		
		
		
		//页面初始化的时候做一次
		$.getJSON("./item!listItem.act"+"?"+"pageNum="+<%=(pn*3)-2%>+userIdPara, fnRenderListItem); //注册一个全局函数
		//查询并放入 
		
		//
		//  滚轴分页加载内容
		//
		$('#container').infinitescroll({
			navSelector : "#page-nav", //导航的选择器，会被隐藏 */
			nextSelector : "#page-nav a",//包含下一页链接的选择器 
			//itemSelector : ".box",//你将要取回的选项(内容块) 
			path:function(crrPageNum){ //使用path而不是 锚点
				//alert("pn:"+<%=pn%>);
				//alert("crrPageNum:"+crrPageNum);
				return "./item!listItem.act?pageNum=" + (<%=(pn*3)-3%>+crrPageNum)
				+ "&numPerPage=10" + "&"+userIdPara//使用url不用navselector}
			},
			debug : true, //启用调试信息
			//默认采用："http://www.infinite-scroll.com/loading.gif"
			animate : true, //当有新数据加载进来的时候，页面是否有动画效果，默认没有
			extraScrollPx : 10, //滚动条距离底部多少像素的时候开始加载，默认150
			bufferPx : 40,//载入信息的显示时间，时间越大，载入信息显示时间越短
			dataType:"json",
			appendCallback:false,//默认true
/* 			template: function(data) { // 如果appendCallback==true 则提供给 callback的必须是html。 template则是将返回处理为html的方法。如appendCallback==false则其callback方法就是用户需要自己处理内容append
				fnRenderListItem(data);
				return data;
			}, */
			errorCallback : function() {
				alert("error");
			},//当出错的时候，比如404页面的时候执行的函数
			localMode : true,//是否允许载入具有相同函数的页面，默认为false
			//maxPage: 2  //这个maxPage 在使用appendCallback:false时似乎会报错。
			
		}, function(data,opts) {
				var page = opts.state.currPage; //这个其实每次都是1开始的当前页数。
				//alert("page:"+page);
/* 				if(page%(3+1)==0){ //3的倍数页则停止 不再查询数据。TODO 这个判断是否应该在path中完成么？本方法是在ajax之后的处理,否则每次都多读一次？
					$(this).infinitescroll('destroy');
					return 
				} */
				if(data==null||typeof(data)!=="object"/* ||data.success==true */||data.rows.length<=0){
					$(this).infinitescroll('destroy');
					return;
				}
				fnRenderListItem(data);
				
				if(page >= 3){ //第三页已经输出则停止插件。
					$(this).infinitescroll('destroy'); //强制插件功能结束。
					return 
				}
				
				//$('#container').masonry('reload');
				//alert("ok!!");
		});
		// 滚轴分页加载内容 end
		

		// 分页组件
		// jPaginator 依赖于 jquery-ui的slider jquery-ui-1.8.13.slider.min.js
		/*
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
								+ "&numPerPage=10" + "&" + userIdPara,
								fnRenderListItem);
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
		*/
		
		//页面获取数据。
		$("#pagor2").bootpag({
			total: 10,
			page: <%=pn%>, //当前页
			maxVisible: 10,
			href: '<%=myPath%>?pageNum={{number}}&numPerPage=10<%=(owner==null)?"":"&owner="+owner%>', // TODO 获取url中的查询参数，比如 pageNum=" + num+ "&numPerPage=10 这样类似参数
			leaps: false,
			next: '>>',
			prev: '<<'
		});
	});
</script>

<%
String navPage="HOME";
if(owner!=null&&accountInfo!=null&&owner.equals(accountInfo.getUserId())){ //当前用户浏览器自己的列表时
	navPage="MYYOUTU";
}
pageContext.setAttribute("navPage",navPage); //pageContext可以用于el表达式 ，由于jsp中的变量一般为方法内部变量所以这里无法直接传递给el表达式需要用pageContext
%>
<jsp:include page="/neck.jsp" flush="true">
	<jsp:param value="${navPage}" name="nav_page"/>
</jsp:include>


<style type="text/css">
/* 页面主题样式  */
/**** Content  也是基础考虑放到整体css中 ****/
#content {
  margin:0 auto; 
  width: 100%;
  /* background: #eee; */
 /*  background: #fff; */
}
.container,
.navbar-static-top .container,
.navbar-fixed-top .container,
.navbar-fixed-bottom .container{
	width: 1194px;/* 1180(1184) 15个span 默认940 为12个span */
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

/*
用户profile的内部样式
一个相对定位框 内部可以放绝对定位框*/
div.rel{
	position: relative;
	/* border: 1px solid #eeeeee; */
}
/* 用户profile-wedget样式 */
img.portrait{
	height: 96px;
	width: 96px;
	border: 5px solid #eeeeee;
}
.corner-stamp .marg{
	height: 50px;
	margin:0 auto;
	/* border: 1px solid #eeeeee; */
}
.corner-stamp .prof{
	min-height:100px;
	border: 5px solid #eeeeee;
	/* 圆角 */
	-webkit-border-radius: 5px;
    	-moz-border-radius: 5px;
          	border-radius: 5px;
  	-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
     	-moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
          	box-shadow: 0 1px 2px rgba(0,0,0,.05);
}
.corner-stamp .prof .profinfo{
	width: 275px;
	height: 130px;
	/* border: 1px solid #eeeeee; */
	padding: 19px 29px 29px;
}
.corner-stamp .proffun{
	top: 5px;
	right: 20px;
	position: absolute;
	min-height: 150px;
	/* border: 1px solid #eeeeee; */
	/* position: absolute; */
}
/**/

.pagor2{
	margin: 0 auto;
	text-align: center;
}
</style>




<section id="content">


		<div id="container" class="container" >
		
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

			<div style="display: none;" id="page-nav"><a href="http://www.17sucai.com/preview/1/2013-05-05/masonry/pages/2.html"></a></div>
			
			<div id="dropEditor" class="box col25 userpanel">
<!-- 暂时不用dropbox 但后续还是以扩展这个dropbox为好 暂时只用 fine-uploader ，但dropbox可以扩展出需要的界面效果。fine-uploader扩展效果比较麻烦。
			  <div id="dropbox">
				<span class="message">...建设中...</span>
			  </div> -->
			  <div id="jquery-wrapped-fine-uploader">上传</div>
			</div>
			
		</div>
		<!-- #container -->
				
	<link href="./res/paginator.css" rel="stylesheet" />
  <!-- paginator 
  <div id="pagor" class="clearfix" >
      <a id="pagor_m_left"></a><div id="pagor_o_left"></div>
      <div class='paginator_p_wrap'>
        <div class='paginator_p_bloc'>
        </div>
      </div>
      <div id="pagor_o_right"></div><a id="pagor_m_right"></a>
      <div class='paginator_slider' class='ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all'>
        <a class='ui-slider-handle ui-state-default ui-corner-all' href='#'></a>
      </div>
  </div>
	 -->	
	<!-- paginator end -->
	
	
	<p id="pagor2" class="pagor2"></p>

</section>
<!-- #content -->

<jsp:include page="/footer.jsp" flush="true"/>