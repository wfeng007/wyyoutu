load(servletContext.getRealPath("/")+"/scripting/jstl-1.0.1.js");

var context = {"user": {"name": "tome"}};
var source = "<% var user = pageContext.getAttribute('user'); out.println(user.name);%> </html><%='wf'%>";
var scriptlet = jsp.runtime.JspRuntime.compile(source);
var result = scriptlet.execute(context);

out.println(result);

