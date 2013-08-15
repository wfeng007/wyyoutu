<%@page import="wyyoutu.web.AccountInfo"%>
<%@page import="com.baidu.api.BaiduOAuthToken"%>
<%@page import="com.baidu.api.BaiduApiClient"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.baidu.api.utils.HttpUtil"%>
<%@page import="com.baidu.api.store.BaiduCookieStore"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="summ.framework.util.*"%>
<%@page import="com.sun.jersey.api.representation.Form"%>
<%@page import="javax.ws.rs.core.*"%>
<%@page import="com.baidu.api.store.BaiduStore"%>
<%@page import="com.baidu.api.Baidu"%>
<%@page import="com.baidu.api.domain.User"%>

redirect-tag by jsp<br/>
<%
String authCode=request.getParameter("code");
String state=request.getParameter("state");
String error=request.getParameter("error");

//获取token
Map<String,String> queryParamMap=new HashMap<String,String>();
queryParamMap.put("grant_type", "authorization_code"); 
queryParamMap.put("code", authCode);//authCode
queryParamMap.put("client_id", "AfTCo9kptgqGGBjuYMMvmrSM"); //API Key
queryParamMap.put("client_secret", "HX9sxvto2DsOuvOYzHrl79lyOnwCG2Ev"); //Secret Key
queryParamMap.put("redirect_uri", "http://www.51youtu.com/51youtu/baidu_redirect2.jsp");//用HttpUtil则直接使用普通url写法。放入querystring时自动转换。
String tokenJsonStr=HttpUtil.doPost("https://openapi.baidu.com/oauth/2.0/token",queryParamMap);

//封装OAuthToken对象
BaiduOAuthToken bot=new BaiduOAuthToken(tokenJsonStr);
System.out.print(bot);
String accessToken=bot.getAccessToken();

// 获取本登录用户的信息。
Map<String,String> loggedPms=new HashMap<String,String>();
loggedPms.put("access_token", accessToken);
loggedPms.put("format", "json"); 
String loggedUserJsonStr=HttpUtil.doPost("https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser",loggedPms);

//封装User对象
User userinfo=new User(loggedUserJsonStr);


//设置到Session TODO之后修改为通用形式。与其他第三方一样。
HttpSession webSess=request.getSession(true);
AccountInfo ai=AccountInfo.lookupAccountInfo(request);
if(ai==null){
	ai=new AccountInfo();
	ai.setUserId("tmp_badiu_"+userinfo.getUid()+"_"+ai.getUserName());
	ai.setUserName(ai.getUserName());
	AccountInfo.setAccountInfo(request,ai);
}
ai.setOAuthBound(true);
ai.putOAuthToken(AccountInfo.OAUTH_PROVIDER_BAIDU,bot);

// TODO 获取某用户信息
Map<String,String> upms=new HashMap<String,String>();
upms.put("access_token", accessToken);
upms.put("format", "json"); 
String userStr=HttpUtil.doPost("https://openapi.baidu.com/rest/2.0/passport/users/getInfo",upms);

//
response.sendRedirect("./");
%>

