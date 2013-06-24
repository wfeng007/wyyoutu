/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wfeng007
 * @date 2012-11-10 下午06:05:20
 */
public class SessionFilter implements Filter{

	private ServletContext sc = null;

	/**
	 * 验证用户session
	 * 但部分url的请求除外
	 * 需要优化
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
//		System.out.println(req.getRequestURI());
		//
//		boolean go=req.getRequestURI().endsWith("index.html")||	req.getRequestURI().contains("session");
		//
//		if(go){
//			chain.doFilter(request, response);
//			return;
//		}
		
		HttpSession session = req.getSession(false);
//		System.out.println(session);
		if(session!=null&&session.getAttribute(AccountInfo.SESSION_KEY)!=null
				&& session.getAttribute(AccountInfo.SESSION_KEY) instanceof AccountInfo){
			AccountInfo ai=(AccountInfo)session.getAttribute(AccountInfo.SESSION_KEY);
//			String str=ai.getUserId();
			chain.doFilter(request, response);
		}
		else{
			//
			// TODO 
			// 现在没有登录就应该跳转
			// 需要提供两种返回方法 json 以及 直接跳转  如何返回应该由 发送者的参数以及 逻辑决定 配置在配置文件中太 难维护。
			//
			res.sendRedirect(this.sc.getContextPath()+"/finger.jsp");
		}
		
//		if (session.getAttribute("userId") == null) {// 身份验证
//			// res.sendRedirect("../login.jsp");这两种方式等价
//			// 原来sendRedirect("/")里面的/表示相对于服务器根目录，即：http://localhost:8080
//			// 而
//			// forward("/")里面的/表示相对于web应用根目录，即：http://localhost:8080/filterDemo
//			res.sendRedirect("/filterDemo/login.jsp");
//			return;
//		}
//		String username = (String) session.getAttribute("username");
//		String func = (String) session.getAttribute("func");
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {
		this.sc=fc.getServletContext();
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.sc=null;
	}

}
