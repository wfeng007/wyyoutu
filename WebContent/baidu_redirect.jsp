<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="summ.framework.util.*"%>
<%@page import="com.sun.jersey.api.representation.Form"%>
<%@page import="javax.ws.rs.core.*"%>

redirect-tag by jsp<br/>
<%
String authCode=request.getParameter("code");
String state=request.getParameter("state");
String error=request.getParameter("state");
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
RestUtil.RestResult rr=RestUtil.doRestNoConfigNoAuth("https://openapi.baidu.com/oauth/2.0/token","POST",MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,queryParamMap, null/*  Form*/,"",new String[]{});
%>
RestResult:<%=rr%><br/>

解析access_token包
<%
String jsonStr=rr.getEntity();

%>

