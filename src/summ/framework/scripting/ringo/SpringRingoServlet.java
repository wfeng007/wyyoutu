/**
 * 
 */
package summ.framework.scripting.ringo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.ringojs.engine.RhinoEngine;
import org.ringojs.engine.RingoWorker;
import org.ringojs.jsgi.JsgiServlet;
import org.ringojs.jsgi.JsgiServletProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import summ.framework.SpringContextHolder;

/**
 * 
 * @author wfeng007
 * @date 2013-9-20 下午03:01:11
 */
public class SpringRingoServlet extends JsgiServletProxy {
	private ApplicationContext _springContext;
	private RhinoEngine _ringoEngine; //在执行init之后才初始化engine

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final ServletContext context = config.getServletContext();
//		_springContext = WebApplicationContextUtils
//				.getRequiredWebApplicationContext(context);
		
		//使用summ框架的springholder来获取spring-context
		//必须放置在springlisenter之后。
		_springContext = SpringContextHolder.getApplicationContext();
		
		//获取父类中的engine。
		_ringoEngine=this.getEngine();
		
		//调用某个模块文件中的全局函数，注意：这里foo不是exports出来的函数引用，而是模块顶层的函数。
//		RingoWorker worker = _ringoEngine.getWorker();
//		try {
//			worker.invoke("summ", "foo"); //FIXME 读取模块的搜索路径？
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally{
//			worker.release();
//		}
		
		//临时方法。使用static缓存类。
		RingoEngineHolder.setRingoEngine(this._ringoEngine);
		//TODO 将_ringoEngine注册进入SpringContext中。可以使用访问代理方式。
		//TODO 将SpringContext作为模块放入ringo的模块库中以便引用，Spring外侧可以分装自己的framwork程序。
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
