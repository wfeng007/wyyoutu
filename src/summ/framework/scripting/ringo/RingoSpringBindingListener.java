/**
 * 
 */
package summ.framework.scripting.ringo;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.ringojs.engine.RhinoEngine;
import org.springframework.context.ApplicationContext;

import summ.framework.SpringContextHolder;

/**
 * 
 * TODO 尚未完成：绑定到springcontext
 * @author wfeng007
 * @date 2013-9-20 下午03:01:11
 */
public class RingoSpringBindingListener extends RingoListener {
	private ApplicationContext _springContext;
//	private RhinoEngine _ringoEngine; //父类初始化

	/* (non-Javadoc)
	 * @see summ.framework.scripting.ringo.JsgiListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		
		final ServletContext context = sce.getServletContext();
//		_springContext = WebApplicationContextUtils
//				.getRequiredWebApplicationContext(context);
		
		//使用summ框架的springholder来获取spring-context
		//必须放置在springlisenter之后。
		_springContext = SpringContextHolder.getApplicationContext();
		
		//获取父类中的engine。
		RhinoEngine ringoEngine=this.getEngine();
		
		
		//
		throw new java.lang.UnsupportedOperationException("todo");
		//TODO 将_ringoEngine注册进入SpringContext中。可以使用访问代理方式。
		//TODO 将SpringContext作为模块放入ringo的模块库中以便引用，Spring外侧可以分装自己的framwork程序。
	}

	/* (non-Javadoc)
	 * @see summ.framework.scripting.ringo.JsgiListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		super.contextDestroyed(sce);
	}

	public ApplicationContext getSpringContext() {
		return _springContext;
	}

	/**
	 * get 获取bean。
	 * js内部可以用以下形式获取spring的bean
	 *  var servlet = req.env.servlet;
	 *  var datasource = servlet.getBean('datasource');
	 *  
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		return _springContext.getBean(name);
	}
}
