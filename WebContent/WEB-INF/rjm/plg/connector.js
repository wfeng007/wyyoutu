

//日志器
var log = require("ringo/logging").getLogger(module.id);

/**
 * 为注册模块添加返回export出来的句柄函数。
 * @param moduleId
 * @param reFuncNameLs
 */
function addPlugin(moduleId,reFuncNameLs) {
	var m = require(moduleId);
	for(var an in m){ //遍历出的就是名称。
		var a=m[an];
		if(typeof(a)==='function'){
			print("+"+an+":"+typeof(an));
			reFuncNameLs.add(an);
		}
	}
}

/**
 * 调用插件（模块）句柄函数。
 * @param moduleId
 * @param funcName
 * @param request
 * @param response
 */
function handle(moduleId,funcName,request,response){
	var m = require(moduleId);
	
	var func=m[funcName];
	log.info("do handle:"+funcName);
	func(request,response);
}