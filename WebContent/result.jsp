<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page isErrorPage="true" %>
<%
if (exception == null) {
	String forwardUrl = (String) request.getAttribute("forwardUrl");
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
}


%>