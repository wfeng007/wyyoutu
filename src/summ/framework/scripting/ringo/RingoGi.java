/**
 * 
 */
package summ.framework.scripting.ringo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.RhinoException;
import org.ringojs.engine.RhinoEngine;
import org.ringojs.engine.RingoConfig;
import org.ringojs.engine.RingoWorker;
import org.ringojs.engine.ScriptError;
//import org.ringojs.jsgi.ContinuationSupport;
import org.ringojs.jsgi.JsgiRequest;
import org.ringojs.jsgi.JsgiServlet;
import org.ringojs.repository.FileRepository;
import org.ringojs.repository.Repository;
import org.ringojs.repository.WebappRepository;
import org.ringojs.tools.RingoRunner;
import org.ringojs.util.StringUtils;

/**
 * 
 * FIXME 实验用
 * 
 * 抽象初始化以及调用过程？
 * 
 * 简单的Ringo引擎调用分装接口（gateway interface）。
 * Pojo形式可以用于spring bean。可以直接通过参数初始化一个独立的ringo engine。
 * 
 * @author wfeng007
 * @date 2013-9-20 下午01:39:50
 */
public class RingoGi {
	
		//function
//	 	String module;
//	    Object function;
	    
	    //engine
	    RhinoEngine engine;
	    JsgiRequest requestProto;
//	    boolean hasContinuation = false;
	    String ringoHome="./";
	    String modulePath="./app";
	    String bootScripts=null;
	    int optlevel=0;
	    boolean debug=false;
	    boolean production=false;
	    boolean verbose=false;
	    boolean legacyMode=false;
	    
	    
	    //
	    public RingoGi() {}
	    
	    public RingoGi(RhinoEngine engine) {
	    	this.engine=engine;
	    }

//	    public RingoGi(RhinoEngine engine) throws ServletException {
//	        this(engine, null);
//	    }

//	    public RingoGi(RhinoEngine engine, Callable callable) throws ServletException {
//	        this.engine = engine;
//	        this.function = callable;
//	    }

	    /**
	     * 需要时初始化。
	     */
	    public void init(){

	        // don't overwrite function if it was set in constructor
//	        if (function == null) {
//	            module = getStringParameter(config, "app-module", "main");
//	            function = getStringParameter(config, "app-name", "app");
	        	
//	        }

	        //初始化引擎。
	        if (engine == null) {
	        	
	        	//基本信息
//	            String ringoHome = getStringParameter(config, "ringo-home", "/WEB-INF");
//	            String modulePath = getStringParameter(config, "module-path", "WEB-INF/app");
//	            String bootScripts = getStringParameter(config, "bootscript", null);
//	            int optlevel = getIntParameter(config, "optlevel", 0);
//	            boolean debug = getBooleanParameter(config, "debug", false);
//	            boolean production = getBooleanParameter(config, "production", false);
//	            boolean verbose = getBooleanParameter(config, "verbose", false);
//	            boolean legacyMode = getBooleanParameter(config, "legacy-mode", false);
	        	String ringoHome = this.ringoHome;
	            String modulePath = this.modulePath; 
	            String bootScripts = this.bootScripts;
	            int optlevel = this.optlevel;
	            boolean debug = this.debug;
	            boolean production = this.production;
	            boolean verbose = this.verbose;
	            boolean legacyMode = this.legacyMode;

	            //创建并初始化脚本资源库。
//	            ServletContext context = config.getServletContext();
//	            Repository base = new WebappRepository(context, "/");
//	            Repository home = new WebappRepository(context, ringoHome);
	            Repository base;
	            Repository home;
	            try {
	            	base = new FileRepository("./");
	            	home = new FileRepository(ringoHome); //
	            	
	                if (!home.exists()) {
	                    home = new FileRepository(ringoHome);
	                    System.err.println("Resource \"" + ringoHome + "\" not found, "
	                            + "reverting to file repository " + home);
	                }
	                // Use ',' as platform agnostic path separator
	                String[] paths = StringUtils.split(modulePath, ",");//可以逗号分隔
	                String[] systemPaths = {"modules", "packages"}; //用于查找资源的目录。
	                /*
	                 *  创建引擎配置
	                 */
	                RingoConfig ringoConfig =
	                        new RingoConfig(home, base, paths, systemPaths);
	                ringoConfig.setDebug(debug);
	                ringoConfig.setVerbose(verbose);
	                ringoConfig.setParentProtoProperties(legacyMode);
	                ringoConfig.setStrictVars(!legacyMode && !production);
	                ringoConfig.setReloading(!production);
	                ringoConfig.setOptLevel(optlevel);
	                
	                //	启动时运行的脚本
	                if (bootScripts != null) {
	                    ringoConfig.setBootstrapScripts(Arrays.asList(
	                            StringUtils.split(bootScripts, ",")));//逗号分隔
	                }
	                /**
	                 * 	创建并初始化ringo-engine
	                 */
	                engine = new RhinoEngine(ringoConfig, null);
	            } catch (Exception x) {
	                throw new RuntimeException(x); //TODO 之后修改
	            }
	        }

//	        try {
//	            hasContinuation = ContinuationSupport.class != null;
//	        } catch (NoClassDefFoundError ignore) {
//	            hasContinuation = false;
//	        }

	        //初始化的参数比如web中的request
	        //requestProto = new JsgiRequest(engine.getScope(), hasContinuation);
	    }

	    
	    public void invoke(Object module, Object function, Object... args){
	    	// request 原型检查处理
//	        try {
//	            if (hasContinuation && ContinuationSupport
//	                    .getContinuation(request).isExpired()) {
//	                return; // continuation timeouts are handled by ringo/jsgi module
//	            }
//	        } catch (Exception x) {
	            // Continuations may not be set up even if class is available.
	            // Set flag to false and ignore otherwise.
//	            log("Caught exception, disabling continuation support", x);
//	            hasContinuation = false;
//	            requestProto = new JsgiRequest(engine.getScope(), false);
//	        }
	    	
	    	//request对象分装创建
//	        JsgiRequest req = new JsgiRequest(request, response, requestProto,
//	                engine.getScope(), this);
	    	
	    	//
	        RingoWorker worker = engine.getWorker();
	        try {
	        	//通过engine调用内部module的某个函数（方法）
	        	//web方式其实是调用一个连接器分装由连接器继续调用jsgi的函数处理。
//	            worker.invoke("ringo/jsgi/connector", "handleRequest", module,
//	                    function, req);
	            //
	        	worker.invoke(module,function,args);
	        	
	        } catch (Exception x) {
	        	//执行过程的内部错误
	            List<ScriptError> errors = worker.getErrors();
	            
	            //
	            boolean verbose = engine.getConfig().isVerbose();
//	            try {
//	                renderError(x, response, errors); //web层的话跳转到错误页面。
//	                RingoRunner.reportError(x, System.err, errors, verbose); //处理内部的错做。
//	            } catch (Exception failed) {
	                // custom error reporting failed, rethrow original exception
	                // for default handling
//	                RingoRunner.reportError(x, System.err, errors, false);
//	                throw new ServletException(x);
//	            }
	            RingoRunner.reportError(x, System.err, errors, verbose); //处理内部的错做。
	        } finally {
	            worker.release();//执行结束将worker放回engine备用
	        }
	    }

//	    protected void renderError(Throwable t, HttpServletResponse response,
//	                               List<ScriptError> errors) throws IOException {
//	        response.reset();
//	        InputStream stream = JsgiServlet.class.getResourceAsStream("error.html");
//	        byte[] buffer = new byte[1024];
//	        int read = 0;
//	        while (true) {
//	            int r = stream.read(buffer, read, buffer.length - read);
//	            if (r == -1) {
//	                break;
//	            }
//	            read += r;
//	            if (read == buffer.length) {
//	                byte[] b = new byte[buffer.length * 2];
//	                System.arraycopy(buffer, 0, b, 0, buffer.length);
//	                buffer = b;
//	            }
//	        }
//	        String template = new String(buffer, 0, read);
//	        String title = t instanceof RhinoException ?
//	                ((RhinoException)t).details() : t.getMessage();
//	        StringBuilder body = new StringBuilder();
//	        if (t instanceof RhinoException) {
//	            RhinoException rx = (RhinoException) t;
//	            if (errors != null && !errors.isEmpty()) {
//	                for (ScriptError error : errors) {
//	                    body.append(error.toHtml());
//	                }
//	            } else {
//	                body.append("<p><b>").append(rx.sourceName())
//	                        .append("</b>, line <b>").append(rx.lineNumber())
//	                        .append("</b></p>");
//	            }
//	            body.append("<h3>Script Stack</h3><pre>")
//	                    .append(rx.getScriptStackTrace())
//	                    .append("</pre>");
//	        }
//	        template = template.replaceAll("<% title %>", title);
//	        template = template.replaceAll("<% body %>", body.toString());
//	        response.setStatus(500);
//	        response.setContentType("text/html");
//	        response.getWriter().write(template);
//	    }

//	    protected String getStringParameter(ServletConfig config, String name,
//	                                        String defaultValue) {
//	        String value = config.getInitParameter(name);
//	        return value == null ? defaultValue : value;
//	    }
//
//	    protected int getIntParameter(ServletConfig config, String name,
//	                                  int defaultValue) {
//	        String value = config.getInitParameter(name);
//	        if (value != null) {
//	            try {
//	                return Integer.parseInt(value);
//	            } catch (NumberFormatException nfx) {
//	                System.err.println("Invalid value for parameter \"" + name
//	                                 + "\": " + value);
//	            }
//	        }
//	        return defaultValue;
//	    }
//
//	    protected boolean getBooleanParameter(ServletConfig config, String name,
//	                                          boolean defaultValue) {
//	        String value = config.getInitParameter(name);
//	        if (value != null) {
//	            if ("true".equals(value) || "1".equals(value) || "on".equals(value)) {
//	                return true;
//	            }
//	            if ("false".equals(value) || "0".equals(value) || "off".equals(value)) {
//	                return false;
//	            }
//	            System.err.println("Invalid value for parameter \"" + name
//	                             + "\": " + value);
//	        }
//	        return defaultValue;
//	    }
	    void destroy(){   	
	    }
}
