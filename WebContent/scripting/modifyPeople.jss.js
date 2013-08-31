/**
 * 已经导入框架以及需要的环境。
 * 参考framework.jss.js以及summ.framework.web.SummFilter类中createScriptContext方法。
 * 
 * java环境条件：BASIC,WEB
 * js环境：framwork.jss.js
 * 
 */
$SW.parseParams();
println("userName:"+param.userName);
println("userId:"+param.userId);
var accountService=$SF("loginService");

importPackage(Packages.wyyoutu.model);
var people=new CoPeople();
people["id"]=param.userId; //FIXME 这里如果param.userId没有定义会给对象设置"undefine怎样的字符串。需要提供方便的转换工具。"
people["name"]=param.userName;
var b=accountService["modifyPeopleBaisc"](people);

delete people;

var result={success:b,msg:"add ok. "};
//如果用json直接返回则用$SW.outJSON返回。
//println(JSON.stringify(result));
//$SW.outJSON(result);

var jsonObj=null;
//如果是使用java的JSONObject对象则处理为：
//importPackage(Packages.net.sf.json);
//var jsonObj=JSONObject.fromObject(JSON.stringify(result));
//System.out.println(jsonObj);

//直接用框架装换
jsonObj=$S.toJavJson(result);

//这里实现跳转功能，而非json返回。
importPackage(Packages.wyyoutu.web);
var re=new WebResult(request,response);
re.setJSON(jsonObj).setRedirectUrl("/account.jsp", -1).setMsg("修改完成！").sendToTraffic();

