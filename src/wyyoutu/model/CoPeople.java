  /*
   * Generate time : 2013-02-06 20:08:56
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * CoPeople 
 *  
 */
public class CoPeople {

	private String id ;	/* 用户ID 登录用login*/
	private Integer seqId ; 
	private String name ;	/* 用户名称 显示用*/
	private String password ;	/* 密码*/
	private String email ;	/* 邮箱*/
	private Date addTs ; /* 新增时间戳*/


	/**
	 * the constructor
	 */
	public CoPeople() {
		//TODO
	}
	
	

	/***GETTER-SETTER***/
	/**
	 * get the id - 用户ID 登录用login
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * set the id - 用户ID 登录用login
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * get the seqId 
	 * @return the seqId
	 */
	public Integer getSeqId() {
		return this.seqId;
	}
	
	/**
	 * set the seqId 
	 */
	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}
	
	/**
	 * get the name - 用户名称 显示用
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * set the name - 用户名称 显示用
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get the password - 密码
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * set the password - 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * get the email - 邮箱
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * set the email - 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * get the addTs - 新增时间戳
	 * @return the addTs
	 */
	public Date getAddTs() {
		return this.addTs;
	}
	
	/**
	 * set the addTs - 新增时间戳
	 */
	public void setAddTs(Date addTs) {
		this.addTs = addTs;
	}
	
	/***GETTER-SETTER END***/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoPeople [" 
	 		+ "id=" + id  + ", "  
	 		+ "seqId=" + seqId  + ", "  
	 		+ "name=" + name  + ", "  
	 		+ "password=" + password  + ", "  
	 		+ "email=" + email  + ", "  
	 		+ "addTs=" + addTs  
		+ "]";
	}
	
	

}