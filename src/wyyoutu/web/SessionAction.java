/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import wyyoutu.model.CoPeople;
import wyyoutu.service.LoginService;

/**
 * 
 * @author wfeng007
 * @date 2012-11-7 下午10:06:04
 */
public class SessionAction /*extends BasicAction*/ { //struts2不继承actionsupport也可以使用 //默认形式使用类似activerecord的方式//适合直接使用activerecord的类型
	
	private static Logger logger = Logger.getLogger(SessionAction.class);
	
	// for spring DI
	private LoginService loginService;
	
   
	//
	private String userId;
	private String password;
	//
	
	// 该功能是手工用out.println实现的
    // 返回给前台的内容，其实给ui的是result.toString()返回的字符串,即response内容默认用result返回
    private JSONObject result;

    //登录
	public void login(){
		
		logger.debug("userId:"+userId);
		
//		System.out.println(ServletActionContext.getServletContext().getRealPath("/"));
//		System.out.println(ServletActionContext.getServletContext().getContextPath());
//		ServletActionContext.getRequest().getAttribute("");
//		Set set=ServletActionContext.getServletContext().getResourcePaths("");
//		for (Iterator it = set.iterator(); it.hasNext();) {
//			Object object = (Object) it.next();
//			
//		}
		
		//
		logger.debug("user&password"+this.userId+"/"+this.password);
    	CoPeople cp= this.loginService.login(this.userId, this.password);
    	
    	logger.info(cp);
    	if(cp!=null){
    		AccountInfo accountInfo=new AccountInfo();
    		accountInfo.setUserId(cp.getId());
    		accountInfo.setUserName(cp.getName());
    		
    		//set session
    		HttpServletRequest request=ServletActionContext.getRequest();
    		HttpSession session=request.getSession(true);
    		session.setAttribute(AccountInfo.SESSION_KEY, accountInfo);
    		logger.info("login-ed:"+accountInfo+" session:"+session.getId());
    		
    		//
    		Map<String, Object> map = new HashMap<String, Object>(0);
    		map.put("msg", "OK!");
    		map.put("sessionId", session.getId());
    		map.put("success", true);
    		this.result=JSONObject.fromObject(map);
    		
    		
    		
    		//登录成功会转发到 result.jsp 并使其redirect to /index.jsp
//    		HttpServletResponse response=ServletActionContext.getResponse();
        	HttpServletRequest req= ServletActionContext.getRequest();
        	try {
        		req.setAttribute("resultJson", this.result);
        		req.setAttribute("forwardUrl", "/index.jsp");//index.html -> index.jsp TODO 应该修改为动态跳转目标 由登录页面选择跳转到哪里去
        		req.setAttribute("msg", "ok");
    			req.getRequestDispatcher("/result.jsp").forward(req, ServletActionContext.getResponse());
    			
    			return; //forward
    		} catch (Exception e1) {
    			throw new RuntimeException(e1);
    		}
    		
    	}else{
    		Map<String, Object> map = new HashMap<String, Object>(0);
    		map.put("msg", "failed!");
    		map.put("success", false);
    		this.result=JSONObject.fromObject(map);
    		
    		
    		
    		//错误则进入login.jsp
//    		HttpServletResponse response=ServletActionContext.getResponse();
        	HttpServletRequest req= ServletActionContext.getRequest();
        	try {
        		req.setAttribute("resultJson", this.result);
        		req.setAttribute("forwardUrl", "/login.jsp");//index.html -> index.jsp TODO 应该修改为动态跳转目标 由登录页面选择跳转到哪里去
        		req.setAttribute("msg", "failed");
    			req.getRequestDispatcher("/result.jsp").forward(req, ServletActionContext.getResponse());
    			return; //forward
    		} catch (Exception e1) {
    			throw new RuntimeException(e1);
    		}
    		
    	}
    	
    	
		/*
		ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
		
		//输出
		PrintWriter out=null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(result); //写的其实是内容response 
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(out!=null)
				out.close();
		}
		*/
		
		
    }
	
	/**
	 * 
	 * 检测session情况。
	 * 一般为前台 ajax调用检查sesson情况。
	 * 
	 */
	/*public void session(){
		logger.debug("userId:"+userId);
		
		HttpServletRequest request=ServletActionContext.getRequest();
		
		HttpSession session=request.getSession(false);
//		if(	this.userId!=null
//				&& session!=null&&session.getAttribute(AccountInfo.SESSION_KEY)!=null
//				&& session.getAttribute(AccountInfo.SESSION_KEY) instanceof AccountInfo 
//				&& this.userId.equals(((AccountInfo)session.getAttribute(AccountInfo.SESSION_KEY)).getUserId())
//				){
		if(session!=null&&session.getAttribute(AccountInfo.SESSION_KEY)!=null){
			
			Map<String, Object> map = new HashMap<String, Object>(0);
    		map.put("msg", "OK!");
    		map.put("userId", ((AccountInfo)session.getAttribute(AccountInfo.SESSION_KEY)).getUserId());
    		map.put("sessionId", session.getId());
    		map.put("success", true);
    		this.result=JSONObject.fromObject(map);
			
    		System.out.println("session():"+session.getAttribute(AccountInfo.SESSION_KEY));
    		
    		//
    		ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
    		//输出
    		PrintWriter out=null;
    		try {
    			out = ServletActionContext.getResponse().getWriter();
    			out.print(result); //写的其实是内容response 
    			out.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		finally{
    			if(out!=null)
    				out.close();
    			return;
    		}
			
		}else{
			try {
				ServletActionContext.getResponse().getWriter().print("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}*/
    
    
//	/*
//	 * 其中 pmSmSystemInfo是aciton名字中间不带方法  act为指定后缀(act就是后缀而已)
//	 * pm/pmSmSystemInfo.act
//	 * @see com.opensymphony.xwork2.ActionSupport#execute()
//	 */
//	@Override
//	public String execute() throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
    

    
    /**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}



	/**
	 * @return the result
	 */
	public JSONObject getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(JSONObject result) {
		this.result = result;
	}

	/**
	 * @return the loginService
	 */
	public LoginService getLoginService() {
		return loginService;
	}

	/**
	 * @param loginService the loginService to set
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	
    
}
