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
%>
authCode:<%=authCode%><br/>
state:<%=state%><br/>
error:<%=error%>(access_denied则是用户取消或没有登录成功)<br/>
<br/>
开始请求Access Token<br/>
<%

/* Form form=new Form();
form.add("grant_type", "authorization_code"); 
form.add("code", authCode);//authCode
form.add("client_id", "AfTCo9kptgqGGBjuYMMvmrSM"); //API Key
form.add("client_secret", "HX9sxvto2DsOuvOYzHrl79lyOnwCG2Ev"); //Secret Key
form.add("redirect_uri", "http%3A%2F%2Fwww.51youtu.com%2F51youtu%2Fbaidu_redirect.jsp"); */
Map<String,String> queryParamMap=new HashMap<String,String>();
queryParamMap.put("grant_type", "authorization_code"); 
queryParamMap.put("code", authCode);//authCode
queryParamMap.put("client_id", "AfTCo9kptgqGGBjuYMMvmrSM"); //API Key
queryParamMap.put("client_secret", "HX9sxvto2DsOuvOYzHrl79lyOnwCG2Ev"); //Secret Key
queryParamMap.put("redirect_uri", "http%3A%2F%2Fwww.51youtu.com%2F51youtu%2Fbaidu_redirect.jsp");
//RestUtil.RestResult rr=RestUtil.doRestNoConfigNoAuth("https://openapi.baidu.com/oauth/2.0/token","POST",MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,queryParamMap, null/*  Form*/,"",new String[]{});


String text=HttpUtil.doPost("https://openapi.baidu.com/oauth/2.0/token",queryParamMap);

%>

<%-- RestResult:<%=rr%><br/>
解析access_token包
<%
String jsonStr=rr.getEntity();
%> --%>

<hr/>
<%-- <%

BaiduStore store=new BaiduCookieStore("AfTCo9kptgqGGBjuYMMvmrSM",request,response);
Baidu baidu=null;
try{
	baidu=new Baidu("AfTCo9kptgqGGBjuYMMvmrSM",
			"HX9sxvto2DsOuvOYzHrl79lyOnwCG2Ev","http://www.51youtu.com/51youtu/baidu_redirect.jsp",store,request);
	
	
	String accessToken=baidu.getAccessToken();
	%>
	<p>accessToken:<%=accessToken %></p>
	<%
	String refreshToken=baidu.getRefreshToken();
	%>
	<p>refreshToken:<%=refreshToken %></p>
	<%
	String sessionKey=baidu.getSessionKey();
	%>
	<p>sessionKey:<%=sessionKey %></p>
	<%
	String sessionSecret=baidu.getSessionSecret();
	%>
	<p>sessionSecret:<%=sessionSecret %></p>
	<%
	User loggedInUser=baidu.getLoggedInUser();
	%>
	<p>loggedInUser:<%=loggedInUser %></p>
	<%
}finally{
	
}

%>
 
<p>baidu:<%=baidu%></p> --%>
