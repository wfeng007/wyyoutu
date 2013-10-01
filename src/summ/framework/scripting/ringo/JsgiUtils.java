/**
 * 
 */
package summ.framework.scripting.ringo;

import javax.servlet.ServletContext;

import org.ringojs.engine.RhinoEngine;
import org.springframework.util.Assert;

/**
 * @author wfeng007
 * @date 2013-10-1 下午09:26:06
 */
final public class JsgiUtils {
	
	
	final public static RhinoEngine getRingoEngine(ServletContext sc ){
		return JsgiUtils.getRingoEngine(sc, RingoListener.RINGO_ENGINE_ATTRIBUTE);
	}
	
	final public static RhinoEngine getRingoEngine(ServletContext sc,String attrName ){
		Assert.notNull(sc, "ServletContext must not be null");
		Object attr = sc.getAttribute(attrName);
		if (attr == null) {
			return null;
		}
		if (attr instanceof RuntimeException) {
			throw (RuntimeException) attr;
		}
		if (attr instanceof Error) {
			throw (Error) attr;
		}
		if (attr instanceof Exception) {
			throw new IllegalStateException((Exception) attr);
		}
		if (!(attr instanceof RhinoEngine)) {
			throw new IllegalStateException("Context attribute is not of type RhinoEngine: " + attr);
		}
		return (RhinoEngine) attr;
	}
	
	

}
