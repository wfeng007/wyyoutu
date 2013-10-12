/**
 * 
 */
package summ.framework.scripting.ringo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.ringojs.engine.RhinoEngine;
import org.ringojs.engine.RingoWorker;
import org.ringojs.engine.ScriptError;
import org.ringojs.jsgi.JsgiRequest;
import org.ringojs.jsgi.JsgiServlet;
import org.ringojs.tools.RingoRunner;

/**
 * 使用filter作为jsgi上层接口实现。接收request转接到ringo-engine中去。
 * 参考官方jsgi servlet实现。
 * @author wfeng007
 * @date 2013-10-1 下午07:36:06
 */
public class JsgiFilter implements Filter {
	
	private final static Logger logger=Logger.getLogger(JsgiFilter.class);
	
	private ServletContext servletContext;
	private RhinoEngine engine;
	private FilterConfig filterConfig;
	private JsgiRequest requestProto;
	
	
	//当前jsgifilter所负责的jsgi-app
    String module;
    Object function;
    //
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {
		this.filterConfig=fc;
		this.servletContext=fc.getServletContext();
		this.engine=(RhinoEngine)this.servletContext.getAttribute(RingoListener.RINGO_ENGINE_ATTRIBUTE);
		this.requestProto=(JsgiRequest)this.servletContext.getAttribute(RingoListener.RINGO_REQUEST_PROTOTYPE_ATTRIBUTE);
		
		// 这两个参数也应该在filter中定义吧。
		// don't overwrite function if it was set in constructor
        if (function == null) {
            module = getStringParameter(fc, "app-module", "main");
            function = getStringParameter(fc, "app-name", "app");
        }
        
	}
	
	/**
	 * 当前没有调用，基本只要filter-mapping url-pattern正确就会执行，并不会执行后续操作。？
	 * chain.doFilter(req, resp);
	 */
	@Override
	public void doFilter(ServletRequest prequest, ServletResponse presponse,
			FilterChain filterChain) throws IOException, ServletException {
	
			//http
			HttpServletRequest request = (HttpServletRequest) prequest;
			HttpServletResponse response = (HttpServletResponse) presponse;
			//这是干啥？让request支持异步？具体是？
//		  	try {
//	            if (hasContinuation && ContinuationSupport.getContinuation(request).isExpired()) {
//	                return; // continuation timeouts are handled by ringo/jsgi module
//	            }
//	        } catch (Exception x) {
//	            // Continuations may not be set up even if class is available.
//	            // Set flag to false and ignore otherwise.
//	            log("Caught exception, disabling continuation support", x);
//	            hasContinuation = false;
//	            requestProto = new JsgiRequest(engine.getScope(), false);
//	        }
	        
	        JsgiRequest req = new FilterJsgiRequest(request, response, requestProto,
	                engine.getScope(),this.servletContext ,this,this.filterConfig,filterChain); //如果底层不进行修改需要生成一个模拟的servlet
	        
	        RingoWorker worker = engine.getWorker();
	        try {
	        	//filterChain.doFilter也可以由js内部完成。帮助过滤真正需要的内容。
	        	//区别于jsgiservlet一旦符合pattern（比如/XXX/*）必须进行处理。无法要求释放该请求过滤某个后缀。
	        	//servlet无法取消某个特定url的拦截。二filter可以doFilter。
	            worker.invoke("ringo/jsgi/connector", "handleRequest", module,
	                    function, req);
	            //
	        } catch (Exception x) {
	            List<ScriptError> errors = worker.getErrors();
	            boolean verbose = engine.getConfig().isVerbose();
	            try {
	                renderError(x, response, errors);
	                RingoRunner.reportError(x, System.err, errors, verbose);
	            } catch (Exception failed) {
	                // custom error reporting failed, rethrow original exception
	                // for default handling
	                RingoRunner.reportError(x, System.err, errors, false);
	                throw new ServletException(x);
	            }
	        } finally {
	            worker.release();
	        }
	        
	}
	
	/**
	 * 完全使用原来的error.html
	 */
	protected void renderError(Throwable t, HttpServletResponse response,
			List<ScriptError> errors) throws IOException {
		response.reset();
		InputStream stream = JsgiServlet.class
				.getResourceAsStream("error.html"); //在JsgiServlet下有一个error.html模板用来显示错误页面。
		byte[] buffer = new byte[1024];
		int read = 0;
		while (true) {
			int r = stream.read(buffer, read, buffer.length - read);
			if (r == -1) {
				break;
			}
			read += r;
			if (read == buffer.length) {
				byte[] b = new byte[buffer.length * 2];
				System.arraycopy(buffer, 0, b, 0, buffer.length);
				buffer = b;
			}
		}
		String template = new String(buffer, 0, read);
		String title = t instanceof RhinoException ? ((RhinoException) t)
				.details() : t.getMessage();
		StringBuilder body = new StringBuilder();
		if (t instanceof RhinoException) {
			RhinoException rx = (RhinoException) t;
			if (errors != null && !errors.isEmpty()) {
				for (ScriptError error : errors) {
					body.append(error.toHtml());
				}
			} else {
				body.append("<p><b>").append(rx.sourceName())
						.append("</b>, line <b>").append(rx.lineNumber())
						.append("</b></p>");
			}
			body.append("<h3>Script Stack</h3><pre>")
					.append(rx.getScriptStackTrace()).append("</pre>");
		}
		template = template.replaceAll("<% title %>", title);
		template = template.replaceAll("<% body %>", body.toString());
		response.setStatus(500);
		response.setContentType("text/html");
		response.getWriter().write(template);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		
	}
	
	protected String getStringParameter(FilterConfig c, String name,
			String defaultValue) {
		String value = c.getInitParameter(name);
		return value == null ? defaultValue : value;
	}

	protected int getIntParameter(FilterConfig c, String name,
			int defaultValue) {
		String value = c.getInitParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException nfx) {
				System.err.println("Invalid value for parameter \"" + name
						+ "\": " + value);
			}
		}
		return defaultValue;
	}
	
	protected boolean getBooleanParameter(FilterConfig c, String name,
			boolean defaultValue) {
		String value = c.getInitParameter(name);
		if (value != null) {
			if ("true".equals(value) || "1".equals(value) || "on".equals(value)) {
				return true;
			}
			if ("false".equals(value) || "0".equals(value)
					|| "off".equals(value)) {
				return false;
			}
			System.err.println("Invalid value for parameter \"" + name + "\": "
					+ value);
		}
		return defaultValue;
	}
	
	
	/**
	 * 用来适配request对象原型类
	 * @author wfeng007
	 * @date 2013-10-1 下午08:43:49
	 */
	static class FilterJsgiRequest extends JsgiRequest{

		
		/**
		 * @param scope
		 * @param asyncSupport
		 */
		public FilterJsgiRequest(Scriptable scope, boolean asyncSupport) {
			super(scope, asyncSupport);
		}
		
		/**
		 * @param request
		 * @param response
		 * @param prototype
		 * @param scope
		 * @param servlet
		 */
		public FilterJsgiRequest(HttpServletRequest request,
				HttpServletResponse response, JsgiRequest prototype,
				Scriptable scope, JsgiServlet servlet) {
			super(request, response, prototype, scope, servlet);
		}
		
		/**
		 * @param request
		 * @param response
		 * @param prototype
		 * @param scope
		 * @param servlet
		 */
		public FilterJsgiRequest(HttpServletRequest request,
				HttpServletResponse response, JsgiRequest prototype,
				Scriptable scope,ServletContext servletContext,Filter filter,FilterConfig filterConfig,FilterChain filterChain) {
			super(request, response, prototype, scope, new JsgiServlet()); //一个空的jsgiservlet之后需要删除。
			deleteProperty((Scriptable)this.getProperty(this, "env"),"servlet"); //删除该对象引用，与servlet互相排斥？
			
			//FIXED pathInfo一直为空串“”
			//null很奇怪的现象，无论web.xml怎么配置（/xxx/*）request.getPathInfo()似乎一直为null。而servletPath总是完整路径，tomcat版本问题？ 或filter的关系？
			System.out.println("pathInfo:"+request.getPathInfo());
			System.out.println("servletPath:"+request.getServletPath()); 
			//filter形式path映射直接修正映射以/ContextPath后为基准，即jsgi-pathInfo <=> ServletPath+PathInfo映射。（原来是以ServletPath之后为基准。）
			//修订pathInfo策略，设置为servletPath+pathinfo的形式。
			String pathInfo=request.getServletPath()+(request.getPathInfo()!=null?request.getPathInfo():"");
			String uri = request.getRequestURI();
			put("pathInfo", this, "/".equals(pathInfo) && !uri.endsWith("/") ? //为何要这样写？uri而不是pathInfo？
		                "" : (pathInfo==null?"":pathInfo));
			//
			
			//env-ext
			defineProperty((Scriptable)this.getProperty(this, "env"),"servletContext", //filter.filterConfig可以得到config?
	                new NativeJavaObject(scope, servletContext, null), PERMANENT);
			defineProperty((Scriptable)this.getProperty(this, "env"),"filter", //filter.filterConfig可以得到config?
	                new NativeJavaObject(scope, filter, null), PERMANENT);
			defineProperty((Scriptable)this.getProperty(this, "env"),"filterConfig", //filterConfig
	                new NativeJavaObject(scope, filterConfig, null), PERMANENT);
			defineProperty((Scriptable)this.getProperty(this, "env"),"filterChain",
	                new NativeJavaObject(scope, filterChain, null), PERMANENT);
		}
	}


	/**
	 * @return the filterConfig
	 */
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}
	
}
