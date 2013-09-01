<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ page language="java"%>
<%@ page session="false"%>
<%
//以下是参考myeclipse生成的部分的写法
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contextPath=path+"/";

%>
<hr/>
<link rel="stylesheet" href="<%=contextPath%>./res/footer.css"/>
<footer>
<div id="footer"> 
<div id="copyright">
51youtu v0.6@ 沪ICP备12041334号 Copyright © 2012 - 2013 wfeng007 <br/> 
wfeng007@163.com <br/>
<br/>
</div> 
</div>
</footer>
</body>
</html>