/**
 * 
 */
package summ.framework.scripting.ringo;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.ringojs.engine.RhinoEngine;
import org.ringojs.engine.RingoConfig;
import org.ringojs.jsgi.JsgiRequest;
import org.ringojs.repository.FileRepository;
import org.ringojs.repository.Repository;
import org.ringojs.repository.WebappRepository;
import org.ringojs.util.StringUtils;

/**
 * 
 * 使用webapp的环境初始化一个ringo engine并绑定到servletContext。
 * 子类可以绑定其他部件。
 * 
 * @author wfeng007
 * @date 2013-9-29 下午08:57:21
 */
public class JsgiServletContextListener implements ServletContextListener {

	static Logger logger=Logger.getLogger(JsgiServletContextListener.class);
	
	final static String RINGO_ENGINE_ATTRIBUTE = JsgiServletContextListener.class.getName() + ".RINGO_ENGINE";
	final static String RINGO_CONFIG_="";
	
	
    String module;
    Object function;
    RhinoEngine engine;
    JsgiRequest requestProto;
    boolean hasContinuation = false;
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		if (servletContext.getAttribute(RINGO_ENGINE_ATTRIBUTE) != null) {
			throw new IllegalStateException(
					"Cannot initialize ringo-engine because there is already a root application context present - " +
					"check whether you have multiple definitions in your web.xml!");
		}
		
		// 这两个参数也应该在filter中定义吧。
		// don't overwrite function if it was set in constructor
        if (function == null) {
            module = getStringParameter(servletContext, "app-module", "main");
            function = getStringParameter(servletContext, "app-name", "app");
        }
		
		servletContext.log("Initializing ringo-engine");
		this.engine = createRingoEngine(servletContext);
		servletContext.setAttribute(RINGO_ENGINE_ATTRIBUTE, this.engine);
		
        // 考虑使用反射来判断
//        try {
//            hasContinuation = ContinuationSupport.class != null;
//        } catch (NoClassDefFoundError ignore) {
//            hasContinuation = false;
//        }
    		
        //这个句话应该放在filter中或servlet中吧？
        requestProto = new JsgiRequest(engine.getScope(), hasContinuation);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info(this.getClass().getName()+":contextDestroyed()");
	}
	
	protected RhinoEngine createRingoEngine(ServletContext servletContext){
        
        if (engine == null) {
            String ringoHome = getStringParameter(servletContext, "ringo-home", "/WEB-INF");
            String modulePath = getStringParameter(servletContext, "module-path", "WEB-INF/app");
            String bootScripts = getStringParameter(servletContext, "bootscript", null);
            int optlevel = getIntParameter(servletContext, "optlevel", 0);
            boolean debug = getBooleanParameter(servletContext, "debug", false);
            boolean production = getBooleanParameter(servletContext, "production", false);
            boolean verbose = getBooleanParameter(servletContext, "verbose", false);
            boolean legacyMode = getBooleanParameter(servletContext, "legacy-mode", false);

//            ServletContext context = servletContext;
            Repository base = new WebappRepository(servletContext, "/");
            Repository home = new WebappRepository(servletContext, ringoHome);

            try {
                if (!home.exists()) {
                    home = new FileRepository(ringoHome);
                    System.err.println("Resource \"" + ringoHome + "\" not found, "
                            + "reverting to file repository " + home);
                }
                // Use ',' as platform agnostic path separator
                String[] paths = StringUtils.split(modulePath, ",");
                String[] systemPaths = {"modules", "packages"};
                RingoConfig ringoConfig =
                        new RingoConfig(home, base, paths, systemPaths);
                ringoConfig.setDebug(debug);
                ringoConfig.setVerbose(verbose);
                ringoConfig.setParentProtoProperties(legacyMode);
                ringoConfig.setStrictVars(!legacyMode && !production);
                ringoConfig.setReloading(!production);
                ringoConfig.setOptLevel(optlevel);
                if (bootScripts != null) {
                    ringoConfig.setBootstrapScripts(Arrays.asList(
                            StringUtils.split(bootScripts, ",")));
                }
                engine = new RhinoEngine(ringoConfig, null);
                return engine;
            } catch (RuntimeException ex) {
            	logger.error("Ringo-engine initialization failed", ex);
    			servletContext.setAttribute(RINGO_ENGINE_ATTRIBUTE, ex);
    			throw ex;
    		} catch (Exception e) {
    			logger.error("Ringo-engine initialization failed", e);
    			servletContext.setAttribute(RINGO_ENGINE_ATTRIBUTE, e);
    			throw new java.lang.RuntimeException("Ringo-engine initialization failed",e);
    		} catch (Error err) {
    			logger.error("Ringo-engine initialization failed", err);
    			servletContext.setAttribute(RINGO_ENGINE_ATTRIBUTE, err);
    			throw err;
    		}
    	}
        else{
        	return this.engine;
        }
		
	}
	
	
	protected String getStringParameter(ServletContext sc, String name,
			String defaultValue) {
		String value = sc.getInitParameter(name);
		return value == null ? defaultValue : value;
	}

	protected int getIntParameter(ServletContext sc, String name,
			int defaultValue) {
		String value = sc.getInitParameter(name);
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

	protected boolean getBooleanParameter(ServletContext sc, String name,
			boolean defaultValue) {
		String value = sc.getInitParameter(name);
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
	
}
