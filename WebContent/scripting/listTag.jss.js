$SW.parseParams();

//
println("itemId:"+param.itemId);
var rsItemService=$SF("rsItemService"); //考虑在整个js中或自己增加一个beancache 提高效率？ //以后通过反射或其他方法提供一个 bean 信息帮助？
//业务逻辑 调用spring
//importClass(Packages.java.util.Arrays); //一个类

//var tagLis=Arrays.asList(typeof tags === "string"&&tags.length>0?tags.split(","): java.lang.reflect.Array.newInstance(java.lang.String,0)); //TODO 选择一些通用的 js 类型工具函数 还有数组生成的函数
var tagList=rsItemService["listTag"](param.itemId);
var tags="";
for ( var idx = 0; idx < tagList.size(); idx++) {
	println("str:"+tagList.get(idx));
	tags+=tagList.get(idx)+",";
}
delete idx;
tags=tags.substring(0,tags.length-1);

//
var result={success:true,'tags':tags,msg:"add ok. "};

//println(JSON.stringify(result));
//
$SW.outJSON(result);

