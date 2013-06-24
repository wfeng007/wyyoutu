package summ.framework.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.ServletActionContext;

import summ.framework.Paging;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

//import ecdata.iccs.framework.Paging;

/**
 * 扩展struts
 * 基础Action
 * 
 * 
 * @author baoguodong(前同事)
 * 
 */

public class BasicAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private JSONObject result;

	public static String success = "SUCCESS";

	public static String msg = "";

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	/**
	 * 返回request
	 * 
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获得参数
	 * 
	 * @param param
	 * @return
	 */
	protected String getParam(String param) {
		String value = getRequest().getParameter(param.trim());
		return ("".equals(value) || value == null) ? "" : value;
	}

	protected String[] getParams(String param) {
		return getRequest().getParameterValues(param);
	}

	protected Integer getIntParam(String param) {
		String integer = getRequest().getParameter(param);
		return (integer == null || integer.trim().equals("")) ? null : Integer
				.parseInt(integer);
	}

	protected double getDoubleParam(String param) {
		String doublevalue = getRequest().getParameter(param);
		return (doublevalue == null || doublevalue.trim().equals("")) ? 0d
				: Double.valueOf(doublevalue);
	}

	/**
	 * 返回response
	 * 
	 * @return
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 返回session
	 * 
	 * @return
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 
	 * @return
	 */
	protected Map getSessionMap() {
		return ActionContext.getContext().getSession();
	}

	/**
	 * 返回servlet上下文
	 * 
	 * @return
	 */
	protected ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	protected String getRealyPath(String path) {
		return getServletContext().getRealPath(path);
	}

	/**
	 * 取得JSON
	 * 
	 * @param obj
	 * @return
	 */
	protected JSONObject getJsonObject(Object obj) {
		return JSONObject.fromObject(obj);

	}

	/**
	 * 取得异常处理
	 */
	protected void outData() {
		try {
			Map<String, String> map = new HashedMap();
			map.put("success", success);
			map.put("msg", msg);
			result = getJsonObject(map);
			getResponse().setContentType("text/javascript;charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 打印JSON
	 */
	protected void outJSON(Object obj) {
		try {
			result = getJsonObject(obj);
			getResponse().setContentType("text/javascript;charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得带root的JSON
	 * 
	 * @param obj
	 * @return
	 */
	protected JSONObject Object2Json(Object obj) {
		Map<String, Object> map = new HashedMap();
		map.put("root", obj);
		return JSONObject.fromObject(map);
	}

	/**
	 * 取得带root的分页JSON
	 * 
	 * @param obj
	 * @param paging
	 * @return
	 */
	protected JSONObject Object2JsonPage(Object obj, Paging paging) {
		Map<String, Object> map = new HashedMap();
		map.put("root", obj);
		map.put("totalProperty", paging.getTotalCount());
		return JSONObject.fromObject(map);
	}

	/**
	 * 打印带root根的JSON
	 * 
	 * @param obj
	 */
	protected void outJSONData(Object obj) {
		try {
			result = Object2Json(obj);
			getResponse().setContentType("text/javascript;charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印带root根的分页JSON
	 * 
	 * @param obj
	 * @param paging
	 */
	protected void outJSONPageData(Object obj, Paging paging) {
		try {
			result = Object2JsonPage(obj, paging);
			getResponse().setContentType("text/javascript;charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
