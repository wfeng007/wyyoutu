  /*
   * Generate time : 2013-02-06 20:08:56
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * RsItem 
 *  
 */
public class RsItem {

	private Integer seqId ; 
	private String iid ;	/* 项目标识；唯一键可以使用任意算法生成。*/
	private String name ;	/* 名称或抬头*/
	private String url ;	/* 访问定位路径*/
	private Integer accessType ; /* 访问类型*/
	private Date addTs ; /* 新增时间戳*/
	private Date modifyTs ; /* 最后更新时间戳*/
	private Integer status ; /* 项目状态；正常、删除、隐藏等*/
	private String text ;	/* 文本内容*/
	private String ownerId ;	/* 关联用户id*/


	/**
	 * the constructor
	 */
	public RsItem() {
		//TODO
	}
	
	

	/***GETTER-SETTER***/
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
	 * get the iid - 项目标识；唯一键可以使用任意算法生成。
	 * @return the iid
	 */
	public String getIid() {
		return this.iid;
	}
	
	/**
	 * set the iid - 项目标识；唯一键可以使用任意算法生成。
	 */
	public void setIid(String iid) {
		this.iid = iid;
	}
	
	/**
	 * get the name - 名称或抬头
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * set the name - 名称或抬头
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get the url - 访问定位路径
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * set the url - 访问定位路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * get the accessType - 访问类型
	 * @return the accessType
	 */
	public Integer getAccessType() {
		return this.accessType;
	}
	
	/**
	 * set the accessType - 访问类型
	 */
	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
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
	
	/**
	 * get the modifyTs - 最后更新时间戳
	 * @return the modifyTs
	 */
	public Date getModifyTs() {
		return this.modifyTs;
	}
	
	/**
	 * set the modifyTs - 最后更新时间戳
	 */
	public void setModifyTs(Date modifyTs) {
		this.modifyTs = modifyTs;
	}
	
	/**
	 * get the status - 项目状态；正常、删除、隐藏等
	 * @return the status
	 */
	public Integer getStatus() {
		return this.status;
	}
	
	/**
	 * set the status - 项目状态；正常、删除、隐藏等
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * get the text - 文本内容
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * set the text - 文本内容
	 */
	public void setText(String text) {
		this.text = text;
	}
	

	

	
	/**
	 * get the ownerId - 关联用户id
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	 * set the ownerId - 关联用户id
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	/***GETTER-SETTER END***/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RsItem [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "iid=" + iid  + ", "  
	 		+ "name=" + name  + ", "  
	 		+ "url=" + url  + ", "  
	 		+ "accessType=" + accessType  + ", "  
	 		+ "addTs=" + addTs  + ", "  
	 		+ "modifyTs=" + modifyTs  + ", "  
	 		+ "status=" + status  + ", "  
	 		+ "text=" + text  + ", "  
	 		+ "ownerId=" + ownerId  
		+ "]";
	}
	
	

}