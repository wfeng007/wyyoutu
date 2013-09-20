//utf-8
//main.js
//jsgi应用入口。其中定义应用入口函数（本文件中为app）。ringo的入口函数名称对应到web.xml的function配置。
//jsgi相关概念请见：http://wiki.commonjs.org/wiki/JSGI/Level0/A/Draft2
//
/**
 * require & import
 */
var strings = require("ringo/utils/strings");
var summjs=require("summ");

//日志器
var log = require("ringo/logging").getLogger(module.id);
/**
 * export
 */
export("doService"); //ringo中导入的另一种写法。

log.info("to init app&module:"+module.id);
//
//exports.app = function(request){
//var app = function(request){
function doService(request){
//	var a = new Application();
	log.info("info");
	log.debug("debug");
	print("do requset...");
	summjs.foo();
    return {
        status: 200,
        headers: {"Content-Type": "text/plain"},
        body: ["Hello World"]
    };
};

////异步模式处理
//app = function(request){
//	  ...
//    return doSomethingAsynchronous().then(function(result){
//        return {
//            status: 200,
//            headers: {},
//            body: ["Result ", result]
//        };
//    });
//};

//如果不是模块
if (require.main === module) {
    print("not support cmdmain!!");
}

