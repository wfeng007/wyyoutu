/**
 * 
 */
package summ.framework;

import org.springframework.context.ApplicationContext;

/**
 * @author wfeng007
 * @date 2011-9-13 下午12:31:34
 * 由于只是初始化用时set 且其他地方只读暂时不用 synchronized
 */
public class SpringContextHolder {
	static private ApplicationContext springContext = null;

	static public ApplicationContext getApplicationContext() {
		return springContext;
	}

	static public void setApplicationContext(ApplicationContext context) {
		springContext = context;
	}
	
	static public Object getBean(String beanId){
		try{
			return springContext.getBean(beanId);
		}catch (Throwable th) {
			th.printStackTrace(System.err);
			throw null;
		}
	}
}
