<%@page import="com.baidu.api.store.BaiduCookieStore"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="summ.framework.util.*"%>
<%@page import="com.sun.jersey.api.representation.Form"%>
<%@page import="javax.ws.rs.core.*"%>
<%@page import="com.baidu.api.store.BaiduStore"%>
<%@page import="com.baidu.api.Baidu"%>
<%@page import="com.baidu.api.domain.User"%>
<%
BaiduStore store=new BaiduCookieStore("AfTCo9kptgqGGBjuYMMvmrSM",request,response);
Baidu baidu=null;
try{
	baidu=new Baidu("AfTCo9kptgqGGBjuYMMvmrSM",
			"HX9sxvto2DsOuvOYzHrl79lyOnwCG2Ev","http%3A%2F%2Fwww.51youtu.com%2F51youtu%2Fbaidu_redirect.jsp",store,request);
	String state=baidu.getState();
	Map<String,String> params=new HashMap<String,String>();
	params.put("state",state);
	String authorizeUrl=baidu.getBaiduOAuth2Service().getAuthorizeUrl(params);
	
	response.sendRedirect(authorizeUrl);
}finally{
	
}
%>


