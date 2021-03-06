//utf-8
//main.js
//jsgi应用入口。其中定义应用入口函数（本文件中为app）。ringo的入口函数名称对应到web.xml的function配置。
//jsgi相关概念请见：http://wiki.commonjs.org/wiki/JSGI/Level0/A/Draft2
//
/**
 * require & import
 */
//var {Application} = require("stick");
var Application = require("stick").Application;
var Buffer = require("ringo/buffer").Buffer;
//日志器
var log = require("ringo/logging").getLogger(module.id);
var strings = require("ringo/utils/strings");

var summjs=require("summ");
var $S=summjs;
var $SW=summjs.web;
var $SF=summjs.fw;

var resp = require('ringo/jsgi/response'); //resp-util

/**
 * export
 */
export("doService"); //ringo中导出的另一种写法。
log.info("to init app&module:"+module.id);

var doService=new Application();
doService.configure(require('./middleware/doFilter')); //serlvet-Filter 在没有找到资源时执行，同时可以提供一个强制忽略jsgi对result的输出处理。不用notfound。
doService.configure("route","mount"); //按照middleware设置顺序运行,执行方式类似filter.

//
//route处理.
//
doService.get("/rjs/listTag",listTag);function listTag(request){
	
	//要求X-JSGI-Do-Filter：servlet-doFilter //也可以直接由自己doFilter.
//	return  {
//		status: 404, //无实际作用。
//		headers: {"X-JSGI-Do-Filter": "true"},
//		body: []
//	};
	//
	var param=$SW.parseParams(request.env.servletRequest).param;
	//
	print("itemId:"+param.itemId);
	var rsItemService=$SF("rsItemService"); //考虑在整个js中或自己增加一个beancache 提高效率？ //以后通过反射或其他方法提供一个 bean 信息帮助？
	//业务逻辑 调用spring
	//importClass(Packages.java.util.Arrays); //一个类
	//var tagLis=Arrays.asList(typeof tags === "string"&&tags.length>0?tags.split(","): java.lang.reflect.Array.newInstance(java.lang.String,0)); //TODO 选择一些通用的 js 类型工具函数 还有数组生成的函数
	var tagList=rsItemService["listTag"](param.itemId);
	var tags="";
	for ( var idx = 0; idx < tagList.size(); idx++) {
		print("str:"+tagList.get(idx));
		tags+=tagList.get(idx)+",";
	}
	delete idx;
	tags=tags.substring(0,tags.length-1);
	//
	var result={success:true,'tags':tags,msg:"list ok. "};
	//println(JSON.stringify(result));
	
	return resp.json(result); //返回一个json对象由上层strik-jsgi-connector完成reponse的out输出。
	
	//另一种方式使用$SW 并要求忽略response
//	$SW.outJSON(request.env.servletResponse,result);
//	//忽略返回，方便以上直接使用response进行操作。
//	return  {
//		status: 404, //无实际作用。
//		headers: {"X-JSGI-Skip-Response": "true"},
//		body: []
//	};
	//
	
}
doService.post("/rjs/replaceTags",function replaceTags(request){
	
	var param=$SW.parseParams(request.env.servletRequest).param;
	print("action:"+param.action);
	print("itemId:"+param.itemId);
	print("tags:"+param.tags);
	var rsItemService=$SF("rsItemService"); //考虑在整个js中或自己增加一个beancache 提高效率？ //以后通过反射或其他方法提供一个 bean 信息帮助？
	
	var tags=String(param.tags); //似乎param传递后又变回object类型了。还要转换一次类型。
	importClass(Packages.java.util.Arrays); //一个类
	//var tags =null;
	var tagList=Arrays.asList(typeof tags === "string"&&tags.length>0?tags.split(","): java.lang.reflect.Array.newInstance(java.lang.String,0)); //TODO 选择一些通用的 js 类型工具函数 还有数组生成的函数
	//print("size:"+tagList.size());
	var retC=$SF("rsItemService")["replaceItemTags"](tagList,param.itemId);
	
	//以下就是通过beaninfo方式获取类信息的 这个可以附加到所有从$SF 得到的bean 后续提供一个方便方法
	//println($SF("rsItemService")["getInfo"]()["getMethodDescriptors"]()[1]["getMethod"]()); //比较复杂需要单独整理
	//
	var result=retC>0?{success:true,msg:"replaceTags ok. added tags count:"+retC}:{success:true,msg:"empty"};
	print("ww:"+result.success);
	delete retC;
	
	return resp.json(result);
	
});

/**
 * 修改密码
 * @param req
 * @returns response
 */
doService.post("/rjs/modifyPassword",function modifyPassword(req){
	
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("userName:"+param.userName);
	print("userId:"+param.userId);
	print("curPwd:"+param.curPwd);
	print("newPwd:"+param.newPwd);
	print("checkPwd:"+param.checkPwd);
	
	
	var coService=$SF("coService");
	var b=false;
	importPackage(Packages.wyyoutu.model);
	var people=new CoPeople();
	var b=coService["modifyPassword"](param.userId,param.curPwd,param.newPwd); //FIXME 这里如果param.userId没有定义会给对象设置"undefine怎样的字符串。需要提供方便的转换工具。"
	delete people;

	var result={success:b,msg:"add ok. "};
//	return response.json(result);
	
	//使用reponse直接要求redirect
	var jsonObj=null;
	jsonObj=$S.toJavJson(result);
	importClass(Packages.wyyoutu.web.WebResult);
	var re=new WebResult(request,response);
	re.setJSON(jsonObj).setRedirectUrl("/index.jsp", -1).setMsg("修改完成！").sendToTraffic();
//	//忽略返回，方便以上直接使用response进行操作。
	return  {
		status: 404, //无实际作用。
		headers: {"X-JSGI-Skip-Response": "true"},
		body: []
	};
	
});


doService.post("/rjs/modifyPeople",function modifyPeople(req){
	
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("userName:"+param.userName);
	print("userId:"+param.userId);
	var coService=$SF("coService");

	importPackage(Packages.wyyoutu.model);
	var people=new CoPeople();
	people["id"]=param.userId; //FIXME 这里如果param.userId没有定义会给对象设置"undefine怎样的字符串。需要提供方便的转换工具。"
	people["name"]=param.userName;
	var b=coService["modifyPeopleBaisc"](people);
	delete people;

	var result={success:b,msg:"add ok. "};
	
	//使用reponse直接要求redirect
	var jsonObj=null;
	jsonObj=$S.toJavJson(result);
	importClass(Packages.wyyoutu.web.WebResult);
	var re=new WebResult(request,response);
	re.setJSON(jsonObj).setRedirectUrl("/index.jsp", -1).setMsg("修改完成！").sendToTraffic();
//	//忽略返回，方便以上直接使用response进行操作。
	return  {
		status: 404, //无实际作用。
		headers: {"X-JSGI-Skip-Response": "true"}, //忽略
		body: []
	};
	
	//要求X-JSGI-Do-Filter：servlet-doFilter //也可以直接由自己doFilter.
//	return  {
//		status: 404, //无实际作用。
//		headers: {"X-JSGI-Do-Filter": "true"},
//		body: []
//	};
});


/**
 * 增加board
 * @param req
 * @returns
 */
doService.post("/rjs/addBoard",function addBoard(req){
	// servlet对象
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	//
	print("pathInfo:"+req.pathInfo);
	var contextPath=request.getContextPath();
	
	//web参数
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("owner:"+param.owner);
	print("boardName:"+param.boardName);
	
	//
	//service-logic
	var rsBoardDao=$SF("rsBoardDao");
	importPackage(Packages.wyyoutu.model);
	var board=new RsBoard();
	board["name"]=param.boardName;
	board["ownerId"]=param.owner;
//	importClass(Packages.java.util.UUID);
	var UUID=Packages.java.util.UUID;
	board["bid"]=UUID.randomUUID().toString();
	board["specialType"]="BASIC";
	board["addTs"]=new Date();
	board["status"]=1;
	var reC=rsBoardDao["insert"](board);
	delete board;
	//
	//
	
	//这里使用了跳转
	//返回response
	//response对象实现redirect居然是发送303状态而不是302，javaservlet默认是302。
//	return resp.redirect(contextPath+"/boards.jsp?owner=admin");
	//如果客户端是ajax的程序，直接redirect似乎不行。
//	var location=contextPath+"/boards.jsp";
//	return {
//        status: 302,
//        headers: {Location: location},
//        body: ["Found:" + location]
//    };
	//使用ajax方式跳转
	var result=reC>0?{success:true,msg:"新增成功！",reload:true}:{success:true,msg:"新增失败！",redirect:contextPath+"/boards.jsp?",reload:false};
	return resp.json(result);
	
});


/**
 * 列出board
 * @param req
 * @returns
 */
doService.get("/rjs/listBoard",listBoard);function listBoard(req){
	// servlet对象
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	//
	
	//web参数
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("owner:"+param.owner);
	if(typeof(param.owner)==="undefined"){
		param.owner=null;
	}
	//
	//service-logic
	var rsBoardDao=$SF("rsBoardDao");
//	importPackage(Packages.java.util.HashMap);
	var HashMap=Packages.java.util.HashMap;
	var pm=new HashMap();
	pm.put("ownerId",param.owner);
	var boardList=rsBoardDao["query"](pm);
	
	//可以使用以下方式进行类型检测
	print("(boardList instanceof java.util.List):"+(boardList instanceof java.util.List) );
	//response返回 
	//TODO 将一下java对象解析为js对象功能工具化
	var JSONArray=Packages.net.sf.json.JSONArray;
	var jsonStr=JSONArray.fromObject(boardList).toString();
	var bLs=JSON.parse(jsonStr);
	var result={success:true,'boardList':bLs,msg:"list ok. "};
	//println(JSON.stringify(result));
	return resp.json(result); //返回一个json对象由上层strik-jsgi-connector完成reponse的out输出。
	//
};

//删除board


//在board中引用item(replace)

//增加album
/**
 * 增加board
 * @param req
 * @returns
 */
doService.post("/rjs/addAlbum",function addAlbum(req){
	// servlet对象
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	//
	print("pathInfo:"+req.pathInfo);
	var contextPath=request.getContextPath();
	
	//web参数
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("owner:"+param.owner);
	print("albumName:"+param.albumName);
	
	
	//
	//service-logic
	var rsAlbumDao=$SF("rsAlbumDao");
	importPackage(Packages.wyyoutu.model);
	var album=new RsAlbum();
	album["name"]=param.albumName;
	album["ownerId"]=param.owner;
//	importClass(Packages.java.util.UUID);
	var UUID=Packages.java.util.UUID;
	album["aid"]=UUID.randomUUID().toString();
	album["addTs"]=new Date();
	album["status"]=1;
	var reC=rsAlbumDao["insert"](album);
	delete album;
	//
	//
	
	//这里使用了跳转
	//返回response
	//response对象实现redirect居然是发送303状态而不是302，javaservlet默认是302。
//	return resp.redirect(contextPath+"/boards.jsp?owner=admin");
	//如果客户端是ajax的程序，直接redirect似乎不行。
//	var location=contextPath+"/boards.jsp";
//	return {
//        status: 302,
//        headers: {Location: location},
//        body: ["Found:" + location]
//    };
	//使用ajax方式跳转
	var result=reC>0?{success:true,msg:"新增成功！",reload:true}:{success:true,msg:"新增失败！",redirect:contextPath+"/album.jsp?",reload:false};
	return resp.json(result);
	
});

//删除album
/**
 * 增加board
 * @param req
 * @returns
 */
doService.post("/rjs/removeAlbum",function removeAlbum(req){
	// servlet对象
	var request =req.env.servletRequest;
	var response = req.env.servletResponse;
	//
	print("pathInfo:"+req.pathInfo);
	var contextPath=request.getContextPath();
	
	//web参数
	var param=$SW.parseParams(req.env.servletRequest).param;
	print("owner:"+param.owner);
	print("album-seqId:"+param.seqId);
	
	//
	//service-logic
	var rsAlbumDao=$SF("rsAlbumDao");
//	importPackage(Packages.wyyoutu.model);
	var HashMap=Packages.java.util.HashMap;
	var pm=new HashMap();
	pm.put("ownerId",param.owner);
	pm.put("seqId",param.seqId);
	var reC=rsAlbumDao["delete"](pm);
	//
	//
	
	//使用ajax方式跳转
	var result=reC>0?{success:true,msg:"新增成功！",reload:true}:{success:true,msg:"新增失败！",redirect:contextPath+"/album.jsp?",reload:false};
	return resp.json(result);
});



//转发到特定处理函数pathinfo不变。
//注意pathinfo以servletcontext之后为基准。
doService.get("/rjs/hello",hello);function hello(request){
	log.info("info");
	log.debug("debug");
	print("do requset...中文");
	summjs.foo();
	return {
	    status: 200,
	    headers: {"Content-Type": "text/plain"},
	    body: ["Hello World 中文"]
	};
}

//转发到特定处理函数pathinfo不变。
//注意pathinfo以servletcontext之后为基准。
doService.get("/rjs/hello",function(request){
	log.info("info");
	log.debug("debug");
	print("do requset...中文");
	summjs.foo();
  return {
      status: 200,
      headers: {"Content-Type": "text/plain"},
      body: ["Hello World 中文"]
  };
});




//
//mount
//

//
//转发(forward)到另一个标准jsgi应用函数（一个特定规则的函数即可）
doService.mount("/rjs/hi", dummyPage("hello,world!!"),true); //注意：默认最后一个参数不舍，这个会改变请求行路径被改变，类似一个redirect。比较奇怪。并且会导致notfound显示不正确。

//转发到另一个标准jsgi应用,且使用另一个模块路径,以/mounted为基准.
//注意:当前被读取的另一个jsgi-app比如export 名称为“app”的函数作为jsgi工作函数。不能是其他的。
//且pathInfo以mount设定的path(/rjs/mounted)为基准.
doService.mount("/rjs/mounted", module.resolve("mounted"),true);

//print("path:"+module.resolve("mounted"));
//暂时没有效果.
//doService.static(module.resolve("api"), "index.html"); // serve files in docs as static resources

//helper for creating simple dummy pages
function dummyPage(text) {
    return function(req) {
        log.info(text);
        log.info("dummyPage pathInfo:"+req.pathInfo);
        req.pathInfo="hi";
        return { status: 200,
                 headers: {"Content-Type": "text/html"},
                 body: new Buffer("<html><body>", text, "</body></html>") };
    }
}

//如果不是模块
if (require.main === module) {
    print("not support cmdmain!!");
}

