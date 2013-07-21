<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page session="false"%>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="wyyoutu.web.WebResult" %>
<%@ page isErrorPage="true" %>
<%!
//
// 依赖WebResult的常量。WebResult可以转发到本result-traffic。
// 需要参考webresult定义跳转数据规范。
// 
%>
<%
//TODO 增加404等协议状态处理。
//确定类型
String handleType=WebResult.HANDLE_TYPE_DISPLAY;
String redirectUrl=null;
if(request.getAttribute(WebResult.RESULT_DATA_KEY_REDIRECT_URL) instanceof String){
	redirectUrl = (String) request.getAttribute(WebResult.RESULT_DATA_KEY_REDIRECT_URL);
	handleType=WebResult.HANDLE_TYPE_REDIRECT;
}
Integer autoDelay=null;
if(request.getAttribute(WebResult.HANDLE_OPTION_KEY_AUTO_DELAY) instanceof Integer){
	autoDelay = (Integer) request.getAttribute(WebResult.HANDLE_OPTION_KEY_AUTO_DELAY);
}

//读取普通文本信息
String msgText=null;
if(request.getAttribute(WebResult.RESULT_DATA_KEY_MSG_TEXT) instanceof String){
	msgText=(String)request.getAttribute(WebResult.RESULT_DATA_KEY_MSG_TEXT);
}
//读取异常错误信息
Throwable error=null;
if(request.getAttribute(WebResult.RESULT_DATA_KEY_ERROR) instanceof Throwable){
	error=(Throwable)request.getAttribute(WebResult.RESULT_DATA_KEY_ERROR);
}
if (exception != null) { //是isErrorPage导致的获取错误信息。
	error=exception; //首先处理isErrorPage默认的
}
//读取json信息对象
JSONObject resultJson=null;
if(request.getAttribute(WebResult.RESULT_DATA_KEY_RESULT_JSON) instanceof JSONObject){
	resultJson=(JSONObject)request.getAttribute(WebResult.RESULT_DATA_KEY_RESULT_JSON);
}
if(request.getAttribute(WebResult.RESULT_DATA_KEY_RESULT_JSON) instanceof String){
	resultJson=JSONObject.fromObject((String)request.getAttribute(WebResult.RESULT_DATA_KEY_RESULT_JSON));//FIXME 这里可能会应该解析 json而出错。
}

//
// 具体根据参数进行不同处理
//

//直接跳转情况
if(WebResult.HANDLE_TYPE_REDIRECT.equals(handleType) && redirectUrl!=null && autoDelay==null){
	response.sendRedirect(application.getContextPath()+redirectUrl); //这里其实只是跳转而已 并非forword。
	return ;
}
//
/* if (exception == null) {
	
	out.println("<b>");
	out.println(forwardUrl);
	out.println("</b><br>");
	if(forwardUrl!=null){
		response.sendRedirect(application.getContextPath()+forwardUrl); //这里其实只是跳转而已 并非forword。
		return ;
	}
	
	String msg = (String) request.getAttribute("msg");
	out.println("<b>");
	out.println(msg);
	out.println("</b><br>");

}else {
		out.println("<b>");
		String exceptionMsg1 = exception.getMessage();
		out.println(exceptionMsg1);
		out.println("</b><br>");
		out.println("<pre>");
		exception.printStackTrace(new PrintWriter(out));
		out.println("</pre>");
} */

// FIXME 后面输出代码有个问题如果当时out已经关闭如何处理？
%>
<jsp:include page="/header.jsp" flush="true">
	<jsp:param name="header_title" value="51youtu-login" />   
</jsp:include>
<jsp:include page="/neck.jsp" flush="true"/>
<div class="contianer">
<%if(redirectUrl!=null){%>
<p>Redirect Url:<a href="<%=application.getContextPath()+redirectUrl%>"><%=redirectUrl%></a></p>
<%out.flush();
}%>

<%if(autoDelay!=null){%>
<p>Auto-redirect delay(sec):<%=autoDelay%></p>
<%out.flush();
}%>

<%if(msgText!=null){%>
<p>MSG:<%=msgText%></p>
<%out.flush();
}%>
<%if(error!=null){%>
<pre>Exception content:
MSG:<%out.println(error.getMessage());
error.printStackTrace(new PrintWriter(out));
%></pre>
<%out.flush();}%>
<%if(resultJson!=null){%>
<p>
<%
resultJson.toString();
out.flush();
%>
</p>
<%}%>
</div>
<jsp:include page="/footer.jsp" flush="true"/>




