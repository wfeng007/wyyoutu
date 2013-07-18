<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page isErrorPage="true" %>
<%!

//处理类型
public static final String HANDLE_TYPE_REDIRECT="HANDLE_TYPE_REDIRECT";
public static final String HANDLE_TYPE_DISPLAY="HANDLE_TYPE_DISPLAY";
public static final String HANDLE_OPTION_KEY_AUTO_DELAY="HANDLE_OPTION_KEY_AUTO_DELAY";//当该参数为null时，则直接跳转。//TODO <0时为手动跳转 >0时为页面自动定时跳转。
//数据
public static final String RESULT_DATA_KEY_REDIRECT_URL="RESULT_DATA_KEY_REDIRECT_URL";//用于跳转的路径。相对路径则指相对于本页面。
public static final String RESULT_DATA_KEY_ERROR="RESULT_DATA_KEY_ERROR";//用于显示或通知的异常对象
public static final String RESULT_DATA_KEY_MSG_TEXT="RESULT_DATA_KEY_MSG_TEXT"; //用于显示或通知的消息
public static final String RESULT_DATA_KEY_RESULT_JSON="RESULT_DATA_KEY_RESULT_JSON";//复杂的json结构的结果集
%>
<%

//TODO 增加404等协议状态处理。
//确定类型
String handleType=HANDLE_TYPE_DISPLAY;
String redirectUrl=null;
if(request.getAttribute(RESULT_DATA_KEY_REDIRECT_URL) instanceof String){
	redirectUrl = (String) request.getAttribute(RESULT_DATA_KEY_REDIRECT_URL);
	handleType=HANDLE_TYPE_REDIRECT;
}
Integer autoDelay=null;
if(request.getAttribute(HANDLE_OPTION_KEY_AUTO_DELAY) instanceof Integer){
	autoDelay = (Integer) request.getAttribute(HANDLE_OPTION_KEY_AUTO_DELAY);
}

//读取普通文本信息
String msgText=null;
if(request.getAttribute(RESULT_DATA_KEY_MSG_TEXT) instanceof String){
	msgText=(String)request.getAttribute(RESULT_DATA_KEY_MSG_TEXT);
}
//读取异常错误信息
Throwable error=null;
if(request.getAttribute(RESULT_DATA_KEY_ERROR) instanceof Throwable){
	error=(Throwable)request.getAttribute(RESULT_DATA_KEY_ERROR);
}
if (exception != null) { //是isErrorPage导致的获取错误信息。
	error=exception; //首先处理isErrorPage默认的
}
//读取json信息对象
JSONObject resultJson=null;
if(request.getAttribute(RESULT_DATA_KEY_RESULT_JSON) instanceof JSONObject){
	resultJson=(JSONObject)request.getAttribute(RESULT_DATA_KEY_RESULT_JSON);
}
if(request.getAttribute(RESULT_DATA_KEY_RESULT_JSON) instanceof String){
	resultJson=JSONObject.fromObject((String)request.getAttribute(RESULT_DATA_KEY_RESULT_JSON));//FIXME 这里可能会应该解析 json而出错。
}

//
// 具体根据参数进行不同处理
//

//直接跳转情况
if(HANDLE_TYPE_REDIRECT.equals(handleType) && redirectUrl!=null && autoDelay==null){
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





