// dropbox 需要时写回 html中。关键是 当前使用fineuploader dropbox之后需要时再用
/* */
$(function(){
		
	//alert("test");
	var dropbox = $('#dropbox'),
	message = $('.message', dropbox);
	
	dropbox.filedrop({
		// The name of the $_FILES entry:
		paramname:'pic',
		
		maxfiles: 5,
		maxfilesize: 2,
	//	url: 'post_file.php',
		url: './svc/upload?retType=json', //提交到的后台
		
		uploadFinished:function(i,file,response,timestamp){
			
			//将精度条设置为已完成
			$.data(file).addClass('done'); //显示已经完成
			//alert(i); //
			
			//$("#imgview #theImg").src="./svc/image?timestamp="+timestamp; //没有效果
			//$("#theImg").attr('src', "./svc/image?timestamp="+ timestamp); //Math.random()//改变其他部分 //刷新图片 需要设置 image的url有变化，否则浏览器会读取缓存
			//$('#dropbox').html($('#dropbox').html());
			
			//
			//TODO 在上传图片完成后直接在界面增加一个新item的box
/* 			$('#dropEditor').after('<div class="box col25"><h2>'+'title'+'</h2>'
					+'<a href="'+'http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg'+'" rel="lightbox" title="'+'title'+'">'
						+'<img alt="" src="'+'http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg'+'" />'
						+'title'+'</a><br/>'+'text'+'<br/><a href="./editor.html?itemId='+'title'+'">编辑</a>'+'</div>'); */
			//图片加载完成后重新render 瀑布布局
			//$('#dropEditor img').load(function(){$('#container').masonry( 'reload' );})
			
			
		},
		
		// 根据返回的err代码 显示错误信息
		error: function(err, file) {
			switch(err) {
				case 'BrowserNotSupported':
					showMessage('Your browser does not support HTML5 file uploads!');
					break;
				case 'TooManyFiles':
					alert('Too many files! Please select 5 at most! (configurable)');
					break;
				case 'FileTooLarge':
					alert(file.name+' is too large! Please upload files up to 2mb (configurable).');
					break;
				default:
					break;
			}
		},
		
		// 每个文件上传前
		// Called before each upload is started
		beforeEach: function(file){
			if(!file.type.match(/^image\//)){
				alert('Only images are allowed!');
				
				// Returning false will cause the
				// file to be rejected
				return false;
			}
		},
		
		//上传一个文件前触发
		uploadStarted:function(i, file, len){
			//生成preview并放入dropediter中
			createImage(file);
		},
		
		//每次更新进度时触发
		progressUpdated: function(i, file, progress) {
			//file对应的preview中得到.progress对象并更新它的进度
			$.data(file).find('.progress').width(progress);
		}
		 
	});
	
	//返回浏览一个文件的模板
	var template = '<div class="preview">'+
						'<span class="imageHolder">'+
							'<img />'+
							'<span class="uploaded"></span>'+
						'</span>'+
						'<div class="progressHolder">'+
							'<div class="progress"></div>'+
						'</div>'+
					'</div>'; 
	
	//上传前 将文件展示到浏览器				
	//创建一个预览
	function createImage(file){
	
		var preview = $(template), 
			image = $('img', preview);
			
		//html5 文件读取器
		var reader = new FileReader();
		
		image.width = 100;
		image.height = 100;
	
		// 在读取器上
		// 注册一个在读取文件后的函数
		reader.onload = function(e){
			
			// e.target.result 记录了文件内容数据 
			// 将其写入img的src属性
			// e.target.result holds the DataURL which
			// can be used as a source of the image:			
			image.attr('src',e.target.result);
		};
		
		// 读取一个文件并触发onload事件
		// Reading the file as a DataURL. When finished,
		// this will trigger the onload function above:
		reader.readAsDataURL(file);
		
		//
		message.hide();
		
		//显示预览框
		//追加
		//preview.appendTo(dropbox);
		//dropbox.append(preview);
		//替换 @FIXME 注意 $.data中的数据没有清除
		dropbox.html(preview);
		
		// 在整体数据缓存中，为file对象附加一个浏览对象。
		// Associating a preview container
		// with the file, using jQuery's $.data():
		$.data(file,preview);
		
		//dropbox变量 应该整合一下
		// 载入图片则重新reload
		$('#dropEditor img').load(function(){$('#container').masonry( 'reload' );})
		
	}
	
	//
	function showMessage(msg){
		message.html(msg);
	}
});
//dropbox end