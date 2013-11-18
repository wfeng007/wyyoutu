  /*
   * Generate time : 2013-11-18 21:31:26
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * RsBoard 
 *  
 */
public class RsBoard {

	private Integer seqId ; 
	private String bid ;	/* 展板标识；唯一键可以使用任意算法生成。系统展板id是固定的几个。*/
	private String name ;	/* 展板名称*/
	private String desc ;	/* 内容描述*/
	private String specialType ;	/* 面板内容领域类型domian_type,专业类型，包括视图的展示类型；基本列表，md-wiki，经验，问答，公告，专辑等等*/
	private String specialSubtype ;	/* 子类型；主要是展示各种视图*/
	private Date addTs ; /* 新增时间戳*/
	private Date modifyTs ; /* 最后更新时间戳*/
	private Integer status ; /* 项状态；锁定、删除、活动*/
	private String ownerId ;	/* 关联用户id，如果为null则说明是系统特殊展板，只有特定权限角色可以编辑。*/


	/**
	 * the constructor
	 */
	public RsBoard() {
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
	 * get the bid - 展板标识；唯一键可以使用任意算法生成。系统展板id是固定的几个。
	 * @return the bid
	 */
	public String getBid() {
		return this.bid;
	}
	
	/**
	 * set the bid - 展板标识；唯一键可以使用任意算法生成。系统展板id是固定的几个。
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	
	/**
	 * get the name - 展板名称
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * set the name - 展板名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get the desc - 内容描述
	 * @return the desc
	 */
	public String getDesc() {
		return this.desc;
	}
	
	/**
	 * set the desc - 内容描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * get the specialType - 面板内容领域类型domian_type,专业类型，包括视图的展示类型；基本列表，md-wiki，经验，问答，公告，专辑等等
	 * @return the specialType
	 */
	public String getSpecialType() {
		return this.specialType;
	}
	
	/**
	 * set the specialType - 面板内容领域类型domian_type,专业类型，包括视图的展示类型；基本列表，md-wiki，经验，问答，公告，专辑等等
	 */
	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}
	
	/**
	 * get the specialSubtype - 子类型；主要是展示各种视图
	 * @return the specialSubtype
	 */
	public String getSpecialSubtype() {
		return this.specialSubtype;
	}
	
	/**
	 * set the specialSubtype - 子类型；主要是展示各种视图
	 */
	public void setSpecialSubtype(String specialSubtype) {
		this.specialSubtype = specialSubtype;
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
	 * get the status - 项状态；锁定、删除、活动
	 * @return the status
	 */
	public Integer getStatus() {
		return this.status;
	}
	
	/**
	 * set the status - 项状态；锁定、删除、活动
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * get the ownerId - 关联用户id，如果为null则说明是系统特殊展板，只有特定权限角色可以编辑。
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	 * set the ownerId - 关联用户id，如果为null则说明是系统特殊展板，只有特定权限角色可以编辑。
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
		return "RsBoard [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "bid=" + bid  + ", "  
	 		+ "name=" + name  + ", "  
	 		+ "desc=" + desc  + ", "  
	 		+ "specialType=" + specialType  + ", "  
	 		+ "specialSubtype=" + specialSubtype  + ", "  
	 		+ "addTs=" + addTs  + ", "  
	 		+ "modifyTs=" + modifyTs  + ", "  
	 		+ "status=" + status  + ", "  
	 		+ "ownerId=" + ownerId  
		+ "]";
	}
	
	

}