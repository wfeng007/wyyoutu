<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%! public String wf=null; %>
<%
	out.println(""+application.getContextPath()+"<br/>");
	out.println(""+request.getRequestURI()+"<br/>");
	out.println(""+request.getRequestURL()+"<br/>");
	out.println(""+request.getServletPath()+"<br/>");
	out.println(""+request.getContextPath()+"<br/>");
	
	Set set=application.getResourcePaths("/res");
	
	for (Iterator it = set.iterator(); it.hasNext();) {
		Object o = (Object) it.next();
		out.println("o:"+o+":"+o.getClass().getCanonicalName()+"<br/>");
	}
	
	
%>