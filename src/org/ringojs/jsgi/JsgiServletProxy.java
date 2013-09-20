/**
 * 
 */
package org.ringojs.jsgi;

import org.ringojs.engine.RhinoEngine;

/**
 * @author wfeng007
 * @date 2013-9-20 下午03:05:57
 */
public class JsgiServletProxy extends JsgiServlet {
	RhinoEngine engine;
	/**
	 * @return the engine
	 */
	protected RhinoEngine getEngine() {
		return super.engine;
	}
}
