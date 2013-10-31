/**
 * 
 */
package wyyoutu.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import wyyoutu.web.WebPlugin.PluginFactory;

/**
 * 
 * 当spring容器以及js容器都初始化完成时将spring中指定pluginFactory初始化，id必须为pluginFactory。
 * 
 * @author wfeng007
 * @date 2013-10-20 下午03:46:26
 */
public class WebPluginFactoryListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final ServletContext context = sce.getServletContext();
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
		PluginFactory pf=(PluginFactory)springContext.getBean("pluginFactory");
	}
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	
	}
	
}
