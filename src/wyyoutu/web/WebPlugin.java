/**
 * 
 */
package wyyoutu.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.spi.container.WebApplication;

import summ.framework.SpringContextHolder;
import summ.framework.scripting.ringo.RingoEngineHolder;

/**
 * 
 * 注意如果都是直接映射的话就没必要提供plugin实例化功能了。直接用string对应即可。主要考虑是否能够提供java版本的实现？
 * 
 * 一个Plugin对应一个cj-module。Plugin就是一个特定module的封装。用于java语言中调用。
 * @author wfeng007
 * @date 2013-9-20 下午05:40:24
 * @date 2013-9-28 
 */
public class WebPlugin {
	
	public static final String DATAKEY=WebPlugin.class.getName()+".DATAKEY";
	
	private static final Logger _logger=Logger.getLogger(WebPlugin.class);
	
	//handleId - Plugin
	public static Map<String,LinkedHashSet<WebPlugin>> _handleMapping=null;
//	public static 
	static{
		_handleMapping=new HashMap<String,LinkedHashSet<WebPlugin>>();
	}
	
	
//	//使用单个函数作为入口的插件。
//	public static Map<String,WebPlugin> _shPluginCache=new HashMap<String,WebPlugin>();
//	
	/**
	 * 执行某个plugin-hanlde
	 * TODO 注意plugin调用的顶层需要注意异常处理以及完备处理
	 * 
	 * @param handleId
	 * @param req
	 * @param resp
	 */
	public static void doHandle(String handleId,HttpServletRequest req,HttpServletResponse resp){
		if(handleId==null)return;//不做任何事情
		Set<WebPlugin> plgLs=_handleMapping.get(handleId);
		if(plgLs==null||plgLs.size()<=0)return;//钩子上没有插件也不做任何事情
		if(plgLs.size()>20)_logger.warn("There are too much plugin-handle need to process! q:"+plgLs.size());
		for (WebPlugin plg : plgLs) {
			plg.handle(handleId, req,resp);
		}
	}
	
	/**
	 * 执行指定插件的handle
	 * FIXME 当前效率很差。
	 * @param handleId
	 * @param pluginId
	 */
//	public static void doHandle(String handleId,HttpServletRequest req,HttpServletResponse resp,String ... pluginIds){
//		Set<WebPlugin> plgLs=_handleMapping.get(handleId);
//		for (WebPlugin plg : plgLs) {
//			for (int i = 0; i < pluginIds.length; i++) {
//				if(plg.id.equals(pluginIds[i])){
//					plg.handle(handleId, req,resp);
//				}
//			}
//		}
//		
//	}
	
	/**
	 * 加载指定插件（配置），内部使用module方式。
	 * 
	 * pluginId == moduleId
	 * @param pluginId
	 */
	public static void addPlugin(String pluginId){
		if(pluginId==null)return;
		
		WebPlugin wp=new WebPlugin(pluginId);
//		wp.id=;
		
	
		//获取初始化后的名称列表
		List<String> funNs=wp.getHandleNameList();
		//
		
		//加入所有关联plg
		for (String handleName : funNs) {
			//如果没有该handle就创建一个
			if(_handleMapping.get(handleName)==null)_handleMapping.put(handleName, new LinkedHashSet<WebPlugin>());
			
			//
			Set<WebPlugin> plgLs= _handleMapping.get(handleName);
			
//			plgLs.contains(wp);
			boolean isOk=plgLs.add(wp);
			if(isOk)
				_logger.info("add plugin:"+pluginId+" handle:"+handleName);
			else
				_logger.info("The plugin is already exists. plugin:"+pluginId+" at handle:"+handleName);
		}
		
	}
	
//	/**
//	 * 直接使用底层单个函数作为插件的唯一handle。
//	 * 注意：singleHandleName不是export的函数，而是module的顶层元素。
//	 */
//	public static void addPlugin(String pluginId,String singleHandleName){
//		if(pluginId==null)return;
//		WebPlugin wp=new WebPlugin();
//		wp.id=pluginId;
//
//		_shPluginCache.put(singleHandleName,wp);
//		
//	}
	
	//
	private final String scriptingSuffix =".plg"; 
	private final String id;//映射为底层module-id
	private final String moduleId;
	
//	private ServletContext application =null;
	
	//构造时初始化。
	/**
	 * 插件内部的操作句柄列表
	 */
	private List<String> handleNameList=new ArrayList<String>(2);;
	

	/**
	 * @param id
	 */
	public WebPlugin(String id) {
		super();
		if(id==null)throw new NullPointerException("Plugin Id must be not Null!!");
		this.id = id;
		this.moduleId=id+scriptingSuffix;
		//初始化plugin（底层的module），让其回写export的函数。的handle。
		//TODO 应提供一种方式区别普通moduleexport的函数以及plg特定的handle函数。否则不太安全。
		//TODO 比如必须export一个hdls数组以描述那些export函数指定为handle。即handle函数的名称。
		//TODO 考虑增加解析获取plugin内部特定 plugin属性配置的能力，不是简单的直接把所有export的函数作为handle
		RingoEngineHolder.invoke("plg/connector", "addPlugin", this.moduleId,this.handleNameList);
	}
//	/**
//	 * @param id
//	 */
//	public WebPlugin(String id,ServletContext application) {
//		super();
//		this.id = id;
//		this.application=application;
//	}
	
	/**
	 * 
	 */
	public void handle(String handleId,HttpServletRequest req,HttpServletResponse resp){
		RingoEngineHolder.invoke("plg/connector", "handle", this.moduleId,handleId,req,resp);
	}
	
	
	/**
	 * 返回一个不能修改的list用于查询。
	 * @return the handleNameList
	 */
	public List<String> getHandleNameList() {
		return Collections.unmodifiableList(handleNameList);
	}
	
	/*
	 * 以下是eclipse生成的用于集合。
	 */

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebPlugin other = (WebPlugin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	/**
	 * 用来初始化默认的plugin
	 * 这些plugin在启动的时候被加载
	 * @author wfeng007
	 * @date 2013-10-20 下午03:36:43
	 */
	public static class PluginFactory{
		public PluginFactory(List<String> initPluginIdLs) {
			System.out.println(initPluginIdLs);
			for (String id : initPluginIdLs) {
				WebPlugin.addPlugin(id);
			}
		}
	}
	

	
}
