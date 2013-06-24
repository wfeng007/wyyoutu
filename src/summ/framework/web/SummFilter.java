/**
 * 
 */
package summ.framework.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts2.RequestUtils; //部分周边工具直接依赖struts包
import org.springframework.core.io.Resource;

import summ.framework.scripting.CachedScript;
import summ.framework.scripting.ScriptCache;

//import sun.org.mozilla.javascript.internal.Scriptable; //sun.* 的包非公开标准api ant编译直接报错.

/**
 * 
 * TODO 开发中
 * TODO 主要目的是提供一个动态语言作为web action处理的功能。（暂时基于servlet 基于其他框架工作量可能比较大。）
 * TODO 首先考虑直接用集成java6的scripting模块
 * TODO 首先考虑截获部分 path进行特定js处理
 * 
 * 本框架的web模块 
 * 访问入口 
 * 依赖 scripting 模块
 * 
 * @author wfeng007
 * @date 2013-1-20 下午03:25:28
 */
public class SummFilter implements Filter {
	
	private final static Logger logger=Logger.getLogger(SummFilter.class);

//	private ScriptEngineManager scriptEngineManager;
	private ServletContext servletContext;
	
	private ScriptCache scriptCache;
	
	
	/**
	 * 一次请求以及相应进行相应
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		//1 读取请求处理时的 动态配置以及用到的永久配置
		//1.1 首先是所有原始http封装对象
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		ServletContext sContext = this.servletContext;
		String webFileSystemRoot=sContext.getRealPath("/"); //这个是一直到应用所在的文件系统目录位置 就是包括webcontext目录对应的文件系统
		logger.debug("WebFileSystemRoot:"+webFileSystemRoot);
		
		//1.2 获取并处理请求path (不带 context) 及其他path相关
		String requestPath=this.getUri(request);
		logger.debug("request-path:"+requestPath);
//		String contextPath=request.getContextPath();
//		logger.debug("context-path:"+contextPath);	
		
		//其他地方的写法		
		//1.3 从request path url等转换到资源路径
		String resourcePath = this.pathMapping(request);
		//next filter 取消拦截让之后的filter处理并返回，不做额外处理。
		if(resourcePath==null){
			chain.doFilter(req, resp);
			return ;
			//
		}
		
		//1.4 判断资源是否存在
		//TODO 13/4/30 根据资源地址来判断 资源 资源读取类型 脚本类型等参考 spring的资源读取器 org.springframework.core.io.Resource接口 及其同包下的实现。
		//
		String resourceUrl="file://"+webFileSystemRoot+resourcePath;
//    	try {
//			org.apache.commons.io.FileUtils.toFile(new URL(resourceUrl));
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		File f=this.resourceToFile("file://"+webFileSystemRoot+resourcePath);
		File f=new File(sContext.getRealPath(resourcePath));
		if(!f.exists() || !f.isFile()){
			//response.sendError(404,"resourcePath);
			// 让后续的filter尝试处理。
			chain.doFilter(req, resp); //该判断应该放入 scripting中进行实现。 //且使用resource概念而不是简单的
			return ;
		}
		//Resource r;
		
		
		//2 处理请求

		//2.1 建立/获取分发器
//		ScriptEngine scriptEngine=this.scriptCache.getEngine();
		//2.2 根据path分转执行
		try {
			//2.2.1
			//引擎级别绑定对象
			ScriptContext scThis=createScriptContext(request,response);
			//2.2.6
			//预先执行配置 通过预定义的脚本来配置引擎
			//增加文件存在判断? 
			runScript("/scripting/framework.jss.js",scThis); //这个eval绑定对象 context会在本次操作时覆盖掉整个 engine绑定对象,且该执行过程中复制给全局变量的参数都在evalBindings中而不是本身的那个bindings中。
			
			//
			//2.2.7
			//实际业务脚本执行
			//返回值是根据脚本请款过来判断的 
			Object retobj = runScript(resourcePath,scThis); //编译在scripting中考虑
			
			//3 考虑直接应答还是进入之后的 filter 以及servlet			
			//继续后续的filter
			//chain.doFilter(req, resp);
			handleResult(retobj);

		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Throwable th){
			//严重错误
			th.printStackTrace();
		}finally{
			
		}
	}
	
	protected void handleResult(Object retobj){
		//
		if (retobj == null) {
			System.out.println("ret is null!!");
		} else if (retobj instanceof Integer) {
			System.out.println("ret is Integer:" + retobj);
		} else if (retobj instanceof Double) {
			System.out.println("ret is Double:" + retobj);
		} else if (retobj instanceof Float) {
			System.out.println("ret is Float:" + retobj);
		} else if (retobj instanceof String) {
			System.out.println("ret is String:" + retobj);
		} else if (retobj instanceof Boolean) {
			System.out.println("ret is Boolean:" + retobj);
		} /*else if (retobj instanceof sun.org.mozilla.javascript.internal.Scriptable) {
			System.out.println("retobj:" + retobj);
			// 解析脚本中返回出来的对象
			sun.org.mozilla.javascript.internal.Scriptable st = (sun.org.mozilla.javascript.internal.Scriptable) retobj;
			System.out.println("classname:" + st.getClassName());
			System.out.println("CanonicalName:" + st.getClass().getCanonicalName());
//			System.out.println("wf:" + st.get("wf", st));
//			System.out.println("0:" + st.get(0, st));
		}*/		else{
			System.out.println("unknow-class:"+retobj.getClass());
		}
	}
	
	protected ScriptContext createScriptContext(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		//
//		ScriptEngineFactory sef=scriptCache.getEngine().getFactory();
		
		
		//
        ScriptContext scriptContext = new SimpleScriptContext();

        //当连续执行的脚本可能不是不同引擎比如 的则需要ScriptContext.GLOBAL_SCOPE ？
        int scope = ScriptContext.ENGINE_SCOPE; //虽然是engine级别但是事实上 在执行用于 eval参数。在一次执行中覆盖原有engine中的绑定。
//        int scope = 99;
		//创建一个传参的字符串数组 模拟 rhion
		String[] arguments=new String[0];
		scriptContext.setAttribute("arguments", arguments, scope);
		
    	//servlet风格
        scriptContext.setAttribute("servletContext", this.servletContext, scope);
        scriptContext.setAttribute("servletRequest", request, scope);
        scriptContext.setAttribute("servletResponse", response, scope);
        
        //jsp风格
//        scriptContext.setAttribute("config", this., scope);
        scriptContext.setAttribute("application", this.servletContext, scope);
//        scriptContext.setAttribute("session", request.getSession(), scope);
        scriptContext.setAttribute("request", request, scope);
        scriptContext.setAttribute("response", response, scope);
        scriptContext.setAttribute("out", response.getWriter(), scope);
        //scriptContext.setAttribute("factory",scriptCache.getEngine().getFactory(), scope);
       
      //io 配置,改变脚本内部输入输出的io器 对于js会影响默认的println函数等
//      scriptContext.setWriter(response.getWriter());
//		scriptContext.setWriter(writer);
//		scriptContext.setErrorWriter(writer);
//		scriptContext.setReader(reader);
        
        return scriptContext;
    }
	
	/**
	 * 
	 * 根据key执行 这个放入 scripting模块吧
	 * 其中包括每次执行前的配置。
	 * 
	 * @param uri
	 * @param scriptContext
	 * @return
	 * @throws ScriptException
	 * @throws IOException
	 */
	protected Object runScript(String uri, ScriptContext scriptContext)
    	throws ScriptException, IOException {
		CachedScript cachedScript=this.scriptCache.getCachedScript(uri);
		CompiledScript cds=this.scriptCache.getScript(uri);
		//框架用的基本内容  
		//再设置一个engine;
		
		scriptContext.setAttribute("engine",cds.getEngine(),ScriptContext.ENGINE_SCOPE);
		//scriptContext.setAttribute("scriptContext",cds.getEngine(),ScriptContext.ENGINE_SCOPE);
		
		//
		scriptContext.setAttribute("scriptname",uri, ScriptContext.ENGINE_SCOPE);
		//脚本文件
		scriptContext.setAttribute(ScriptEngine.FILENAME,cachedScript.getScriptFile().getAbsoluteFile(), ScriptContext.ENGINE_SCOPE);
		
		//通用参数 //但有可能 连续执行的context 要对应不同的引擎。 所以这些参数 可能是必要的
		scriptContext.setAttribute(ScriptEngine.NAME,cds.getEngine().getFactory().getEngineName() , ScriptContext.ENGINE_SCOPE);
		scriptContext.setAttribute(ScriptEngine.ENGINE,cds.getEngine().getFactory(), ScriptContext.ENGINE_SCOPE);
		scriptContext.setAttribute(ScriptEngine.ENGINE_VERSION,cds.getEngine().getFactory().getEngineVersion() , ScriptContext.ENGINE_SCOPE);
		scriptContext.setAttribute(ScriptEngine.LANGUAGE,cds.getEngine().getFactory().getLanguageName() , ScriptContext.ENGINE_SCOPE);
		scriptContext.setAttribute(ScriptEngine.LANGUAGE_VERSION,cds.getEngine().getFactory().getLanguageVersion() , ScriptContext.ENGINE_SCOPE);
		
		return cds.eval(scriptContext);
	}
	
	/**
	 * 
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {
		// 1 启动时读取永久配置
		this.servletContext=fc.getServletContext();
//		ServletConfig =  //不是servlet
		
		this.scriptCache = new ScriptCache(1000) {
            public File getScriptFile(String uri) {
            	
            	ServletContext sc=SummFilter.this.servletContext;
            	//TODO 这里最好分解为 getRealPath("/") 与传入路径分离？
            	
//            	String resourceUrl="file://"+webFileSystemRoot+resourcePath 
//            	try {
//        			return org.apache.commons.io.FileUtils.toFile(new URL(resourceUrl));
//        		} catch (MalformedURLException e) {
//        			e.printStackTrace();
//        			return null;
//        		}
        		
                return new File(sc.getRealPath(uri));
            }
        };
        
		//
		// 2 缓存永久配置
		
		
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		//1 永久配置 销毁
		
	}

	
	/**
	 * 
	 * 根据request获取对应实际 资源路径映射。
	 * 不如jss后缀映射到 资源文件的jss.js后缀
	 * 
	 * @param requestPath request中的路径
	 * @return 资源路径 resourcepath
	 */
	private String pathMapping(HttpServletRequest request){
		String requestPath=this.getUri(request);
//		logger.debug("request-path:"+requestPath);
		if(requestPath!=null){
			return requestPath.endsWith("jss")? requestPath+".js":null; //将
		}
		
		return null;
	}
	
	/**
	 * 返回通过url获取的相对uri即, contextpath之后的那段。
	 * 且去掉了查询参数后缀等。
	 * @param request
	 * @return
	 */
	private String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.(JSP) 考虑jsp include情况
        String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }
        
        //这里是通过uri查找servlet
//        uri = RequestUtils.getServletPath(request); 
//        if (uri != null && !"".equals(uri)) {
//            return uri;
//        }

        uri = request.getRequestURI(); //不带参数
        return uri.substring(request.getContextPath().length()); //去掉contextpath
    }
	

}
