<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>JSP Info</title>
</head>
<body>
	<h1>JSP Info(Mail: nscdl AT yahoo DOT com)</h1>
	<hr />
	Now is:
	<%=   new   java.util.Date()   %>
	<br /> OS name:
	<%=   System.getProperty("os.name")   %>
	<br /> OS version:
	<%=   System.getProperty("os.version")   %>
	<br /> OS arch:
	<%=   System.getProperty("os.arch")   %>
	<br /> User name:
	<%=   System.getProperty("user.name")   %>
	<br /> User home:
	<%=   System.getProperty("user.home")   %>
	<br /> User dir:
	<%=   System.getProperty("user.dir")   %>
	<br /> User language:
	<%=   System.getProperty("user.language")   %>
	<br /> User timezone:
	<%=   System.getProperty("user.timezone")   %>
	<br /> File encoding:
	<%=   System.getProperty("file.encoding")   %>
	<br /> File separator:
	<%=   System.getProperty("file.separator")   %>
	<br /> Path separator:
	<%=   System.getProperty("path.separator")   %>
	<br /> Line separator:
	<%=   System.getProperty("line.separator")   %>
	<hr />

	Java version:
	<%=   System.getProperty("java.version")   %>
	<br /> Java vendor:
	<%=   System.getProperty("java.vendor")   %>
	<br /> JVM name:
	<%=   System.getProperty("java.vm.name")   %>
	<br /> JVM version:
	<%=   System.getProperty("java.vm.version")   %>
	<br /> JVM vendor:
	<%=   System.getProperty("java.vm.vendor")   %>
	<br /> Java home:
	<%=   System.getProperty("java.home")   %>
	<br /> Java class path:
	<%=   System.getProperty("java.class.path")   %>
	<br /> Java class version:
	<%=   System.getProperty("java.class.version")   %>
	<br /> Free memory:
	<%=   Runtime.getRuntime().freeMemory()/1024.0/1024   %>MB
	<br /> Total memory:
	<%=   Runtime.getRuntime().totalMemory()/1024.0/1024   %>MB
	<hr />
	Your browser:
	<%=   request.getHeader("User-Agent")   %>
	<br /> JSP request method:
	<%=   request.getMethod()   %>
	<br /> Request URI:
	<%=   request.getRequestURI()   %>
	<br /> Request ContextPath:
	<%=   request.getContextPath()   %>
	<br /> Request protocol:
	<%=   request.getProtocol()   %>
	<br /> Servlet path:
	<%=   request.getServletPath()   %>
	<br /> Path info:
	<%=   request.getPathInfo()   %>
	<br /> Path translated:
	<%=   request.getPathTranslated()   %>
	<br /> Query string:
	<%=   request.getQueryString()   %>
	<br /> Content length:
	<%=   request.getContentLength()   %>
	<br /> Content type:
	<%=   request.getContentType()   %>
	<br /> Server name:
	<%=   request.getServerName()   %>
	<br /> Server port:
	<%=   request.getServerPort()   %>
	<br /> Remote user:
	<%=   request.getRemoteUser()   %>
	<br /> Remote address:
	<%=   request.getRemoteAddr()   %>
	<br /> Remote host:
	<%=   request.getRemoteHost()   %>
	<br /> Authorization scheme:
	<%=   request.getAuthType()   %>
	<hr />

	<%--<%  
  ServletContext   context   =   getServletContext();  
  java.util.Enumeration   enum   =   context.getAttributeNames();  
  while   (enum.hasMoreElements())   {  
  String   key   =   (String)enum.nextElement();  
  Object   value   =   context.getAttribute(key);  
  out.println(key);  
  out.println("<br   />");  
  out.println(value);  
  out.println("<hr   />");  
  }  
  %>--%>
</body>
</html>
