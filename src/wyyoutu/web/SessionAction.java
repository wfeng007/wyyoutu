/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import wyyoutu.model.CoPeople;
import wyyoutu.service.CoService;
import wyyoutu.service.LoginService;

/**
 * 与账户token及session有关的功能
 * @author wfeng007
 * @date 2012-11-7 下午10:06:04
 */
public class SessionAction /*extends BasicAction*/ { //struts2不继承actionsupport也可以使用 //默认形式使用类似activerecord的方式//适合直接使用activerecord的类型
	
	private static Logger logger = Logger.getLogger(SessionAction.class);
	
	// for spring DI
	private LoginService loginService;
	private CoService coService;
   
	//
	private String userId;
	private String password;
	//
	
	// 该功能是手工用out.println实现的
    // 返回给前台的内容，其实给ui的是result.toString()返回的字符串,即response内容默认用result返回
    private JSONObject result;

    /**
     * 登录。
     */
	public void login(){
		
		logger.debug("To login! userId:"+userId);
		
//		System.out.println(ServletActionContext.getServletContext().getRealPath("/"));
//		System.out.println(ServletActionContext.getServletContext().getContextPath());
//		ServletActionContext.getRequest().getAttribute("");
//		Set set=ServletActionContext.getServletContext().getResourcePaths("");
//		for (Iterator it = set.iterator(); it.hasNext();) {
//			Object object = (Object) it.next();
//			
//		}
		
		//
		logger.info("user&password"+this.userId+"/"+this.password);
    	CoPeople cp= this.loginService.login(this.userId, this.password);
    	
    	logger.info(cp);
//    	try{
    	if(cp!=null){
    		
    		//生成Session账户信息
    		AccountInfo accountInfo=new AccountInfo();
    		accountInfo.setUserId(cp.getId());
    		accountInfo.setUserName(cp.getName());
    		//设置权限与角色备用
    		//FIXME 执行了两次查询role
    		List<String> allPerms=this.coService.listAllPermIdByPeopleId(accountInfo.getUserId());
    		List<String> roles=this.coService.listRoleIdByPeopleId(accountInfo.getUserId());
    		accountInfo.setAllPerms(allPerms);
    		accountInfo.setRoles(roles);
    		//
    		
    		//生成session-scope context
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
//        	HttpServletRequest req= ServletActionContext.getRequest();
//        	try {
//        		req.setAttribute("RESULT_DATA_KEY_RESULT_JSON", this.result);
//        		req.setAttribute("HANDLE_OPTION_KEY_AUTO_DELAY",null); //直接跳转
//        		req.setAttribute("RESULT_DATA_KEY_REDIRECT_URL", "/index.jsp");//index.html -> index.jsp TODO 应该修改为动态跳转目标 由登录页面选择跳转到哪里去
//        		req.setAttribute("RESULT_DATA_KEY_MSG_TEXT", "ok");
//    			req.getRequestDispatcher("/result.jsp").forward(req, ServletActionContext.getResponse());
//    			return; //forward
//    		} catch (Exception e1) {
//    			throw new RuntimeException(e1);
//    		}
    		WebResult re=new WebResult(ServletActionContext.getRequest(),ServletActionContext.getResponse());
    		re.setJSON(this.result).setRedirectUrl("/index.jsp?owner="+accountInfo.getUserId(), null).setMsg("ok").sendToTraffic();
    		
    		return;
    	}else{
    		Map<String, Object> map = new HashMap<String, Object>(0);
    		map.put("msg", "failed!");
    		map.put("success", false);
    		this.result=JSONObject.fromObject(map);
    		
    		
    		//错误则进入登录界面
    		//结果处理 页面跳转
    		WebResult re=new WebResult(ServletActionContext.getRequest(),ServletActionContext.getResponse());
    		re.setJSON(this.result).setRedirectUrl("/login.jsp", -1).setMsg("登录失败").sendToTraffic();
    		return ;
    	}
    	
//    	}
//    	catch(Exception e){
//    		e.printStackTrace();
//    	}
    	
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

	public void logout(){
		HttpSession sess=ServletActionContext.getRequest().getSession(false);
		if(sess!=null){
			sess.invalidate();
			sess=null;
		}
		WebResult re=new WebResult(ServletActionContext.getRequest(),ServletActionContext.getResponse());
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("msg", "logout OK!");
		map.put("success", true);
		re.setJSON(this.result).setRedirectUrl("/index.jsp", null).setMsg("ok").sendToTraffic();
	}
    
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

	/**
	 * @return the coService
	 */
	public CoService getCoService() {
		return coService;
	}

	/**
	 * @param coService the coService to set
	 */
	public void setCoService(CoService coService) {
		this.coService = coService;
	}

	
    
}
