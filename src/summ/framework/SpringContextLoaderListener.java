/**
 * 
 */
package summ.framework;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * @author wfeng007
 * @date 2011-9-13 下午12:28:29
 * 扩展spring listener增加holder
 */
public class SpringContextLoaderListener extends ContextLoaderListener {

	/* (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		SpringContextHolder.setApplicationContext(null);
		System.out.println("to release container...");
		super.contextDestroyed(event);
		//do
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//do
		super.contextInitialized(event);
		System.out.println("to set spring-context into container...");	
		SpringContextHolder.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()));
	}

}
