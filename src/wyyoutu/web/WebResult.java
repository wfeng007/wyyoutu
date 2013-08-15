/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

/**
 * 临时的web servlet工具。后续修改放入framework。
 * 本类也包含了跳转的数据规范。
 * 
 * @author wfeng007
 * @date 2013-7-21 上午10:56:33
 */
public class WebResult {
	
	//？？？这里有何用
	public static final String HANDLE_TYPE_REDIRECT="HANDLE_TYPE_REDIRECT";
	public static final String HANDLE_TYPE_DISPLAY="HANDLE_TYPE_DISPLAY";

	//数据
	public static final String RESULT_DATA_KEY_REDIRECT_URL="RESULT_DATA_KEY_REDIRECT_URL";//用于跳转的路径。相对路径则指相对于本页面。
	public static final String RESULT_DATA_KEY_ERROR="RESULT_DATA_KEY_ERROR";//用于显示或通知的异常对象
	public static final String RESULT_DATA_KEY_MSG_TEXT="RESULT_DATA_KEY_MSG_TEXT"; //用于显示或通知的消息
	public static final String RESULT_DATA_KEY_RESULT_JSON="RESULT_DATA_KEY_RESULT_JSON";//复杂的json结构的结果集
	
	//参数
	public static final String HANDLE_OPTION_KEY_AUTO_DELAY="HANDLE_OPTION_KEY_AUTO_DELAY";//当该参数为null时，则直接跳转。//TODO <0时为手动跳转 >0时为页面自动定时跳转。
	
	

	private ServletRequest request;
	private ServletResponse response;
	
	/**
	 * @param request
	 * @param response
	 */
	public WebResult(ServletRequest request, ServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}
	
	public WebResult setJSON(JSONObject json){
		this.request.setAttribute(RESULT_DATA_KEY_RESULT_JSON, json);
		return this;
	}
	
	public WebResult setMsg(String msg){
		this.request.setAttribute(RESULT_DATA_KEY_MSG_TEXT, msg);
		return this;
	}
	
	//delay ==null则表示直接返回不等待
	public WebResult setRedirectUrl(String url,Integer delay){
		this.request.setAttribute(RESULT_DATA_KEY_REDIRECT_URL, url);
		this.request.setAttribute(HANDLE_OPTION_KEY_AUTO_DELAY, delay);
		return this;
	}
	
	public WebResult setException(Throwable th){
		this.request.setAttribute(RESULT_DATA_KEY_ERROR, th);
		return this;
	}
	
	//
	// TODO 考虑异常情况。
	public void sendToTraffic(){
		try {
			this.request.getRequestDispatcher("/result.jsp").forward(this.request, this.response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
