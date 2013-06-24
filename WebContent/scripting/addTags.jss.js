load(servletContext.getRealPath("/")+"/scripting/json2.js");

var param = new Object();
var paramValues = new Object();

function initParams() { //参考这个提供一个 java-map/list 到 js-json 以及 js-json到java-map/list的拷贝
    var paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) { // 注意queryparam 会覆盖 headerparam
        var name = paramNames.nextElement();
        param[name] = String(request.getParameter(name));
        paramValues[name] = new Array();
        var values = request.getParameterValues(name);
        for (var i = 0; i < values.length; i++)
            paramValues[name][i] = String(values[i]);
    }
}

initParams();

//application request session
//针对所有带有getAttribute //setAttribute的操作
function getBean(scope, id) {
    return eval(scope).getAttribute(id);
}
function setBean(scope, id, bean) {
    if (!bean)
        bean = eval(id);
    return eval(scope).setAttribute(id, bean);
}

println("action:"+param.action);
println("itemId:"+param.itemId);
println("tags:"+param.tags);

//
//importPackage(Packages.summ);
//importPackage(Packages.summ.framework);
//importPackage(Packages.summ.framework.web);

//var rsItemService=SpringContextHolder.getApplicationContext().getBean("rsItemService");
var rsItemService=$SF("rsItemService"); //考虑在整个js中或自己增加一个beancache 提高效率？ //以后通过反射或其他方法提供一个 bean 信息帮助？

// 业务逻辑 调用spring
//println(rsItemService.getItemById(param.itemId));
//println(rsItemService["getItemById"](param.itemId)); //非常好用
//println($SF("rsItemService")["getItemById"](param.itemId)); //方法路径完全动态了
var tags=param.tags;
importClass(Packages.java.util.Arrays); //一个类
//var tags =null;
var tagList=Arrays.asList(typeof tags === "string"&&tags.length>0?tags.split(","): java.lang.reflect.Array.newInstance(java.lang.String,0)); //TODO 选择一些通用的 js 类型工具函数 还有数组生成的函数
var retC=$SF("rsItemService")["replaceItemTags"](tagList,param.itemId);

//以下就是通过beaninfo方式获取类信息的 这个可以附加到所有从$SF 得到的bean 后续提供一个方便方法
//println($SF("rsItemService")["getInfo"]()["getMethodDescriptors"]()[1]["getMethod"]()); //比较复杂需要单独整理
//
var result=retC>0?{success:true,msg:"add ok. added tags count:"+retC}:{success:true,msg:"empty"};
delete retC;

//println(JSON.stringify(result));
// 以下可以封装一下
response.setContentType("application/json");// 设置http头部为json
out.print(JSON.stringify(result));
out.flush();
//

