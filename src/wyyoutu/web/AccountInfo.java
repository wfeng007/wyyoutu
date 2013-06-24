/**
 * 
 */
package wyyoutu.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * 账号及用户信息对象
 * 用于session维护
 * 
 * @author wfeng007
 * @date 2012-11-7 下午10:17:46
 */
public class AccountInfo {
	
	public static final String SESSION_KEY="ACCOUNT_INFO_SESSION_KEY";
	/**
	 * 默认方式从session中获取 账号及用户信息。
	 * @param request
	 * @return Session中得到的AccountInfo实例，如果没有符合规则的账号信息则返回null
	 */
	public static AccountInfo lookupAccountInfo(HttpServletRequest request){
		HttpSession session=request.getSession(false);
		//一般只要有AccountInfo对象都应该为正常的 账户信息
		if(session!=null&&session.getAttribute(AccountInfo.SESSION_KEY)!=null&&session.getAttribute(AccountInfo.SESSION_KEY) instanceof AccountInfo){
			
			System.out.println("lookupAccountInfo():"+session.getAttribute(AccountInfo.SESSION_KEY));
			
			return (AccountInfo)session.getAttribute(AccountInfo.SESSION_KEY);
		}
		return null;
	}
	
	/**
	 * 用户id、登录账号
	 */
	private String userId;
	/**
	 * 用户显示名称
	 */
	private String userName;
	
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountInfo [userId=" + userId + ", userName=" + userName + "]";
	}
	

	
}
