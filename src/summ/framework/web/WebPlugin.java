/**
 * 
 */
package summ.framework.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * TODO 继续130920
 * 
 * 一个Plugin对应一个cj-module。Plugin就是一个特定module的封装。用于java语言中调用。
 * @author wfeng007
 * @date 2013-9-20 下午05:40:24
 */
public class WebPlugin {
	//handleId - Plugin
	public static Map<String,List<WebPlugin>> pluginCache=null;
	public static void doHandle(String handleId,HttpServletRequest req,HttpServletResponse resp){
		List<WebPlugin> plgLs=pluginCache.get(handleId);
		for (WebPlugin plg : plgLs) {
			plg.handle(handleId, req,resp);
		}
	}
	public static void doHandle(String handleId,String ... pluginId){
	}
	public static void addPlugin(String pluginId){
	}
	private String id;//可以映射为底层module-id
//	private String moduleId;
	
	private ServletContext application =null;
	/**
	 * 对应到函数名称
	 */
	public void handle(String handleId,HttpServletRequest req,HttpServletResponse resp){
		//
	}
	
	static public class Context{
		
	}
	
	
	
}
