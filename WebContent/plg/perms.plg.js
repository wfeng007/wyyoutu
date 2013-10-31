//utf-8
//perms插件
//
/**
 * require & import
 */
var strings = require("ringo/utils/strings");
//var summjs=require("summ");

//日志器
var log = require("ringo/logging").getLogger(module.id);
/**
 * export 的模块函数，同时也是插件的执行句柄。
 */
export("vPosNeck"); //导出插件handle

//
log.info("插件初始化时运行，初始化插件"+module.id);

//
//exports.app = function(request){
//var app = function(request){
//
var vPosNeck=function(req,resp){
	var out=resp.getWriter();
//	out.print("hi 我是插件， hello World!以下是hello dolly的歌词!<br/>");
//	out.print(getLyricsLine()+"<br/>");
	log.info("vPosNeck!!");
	return;
};


//如果不是模块
if (require.main === module) {
    print("not support cmdmain!!");
}

