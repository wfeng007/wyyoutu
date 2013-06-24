  /*
   * Generate time : 2013-04-18 20:01:00
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.HashMap;
import java.util.Map;
/**
 * RsTagged 
 *  
 */
public class RsTagged {

	private Long seqId ; /* 自增id，对应程序应该为long类型*/
	private String tagId ;	/* id，可以用显示名称代替之,TAG可以当做评论用么？*/
	private String targetId ;	/* 一般为项目标识，对应到iid，注意iid为item_id首选。（目标必须存在一个 128位以下的唯一键值）*/


	/**
	 * the constructor
	 */
	public RsTagged() {
		//TODO
	}
	
	

	/***GETTER-SETTER***/
	/**
	 * get the seqId - 自增id，对应程序应该为long类型
	 * @return the seqId
	 */
	public Long getSeqId() {
		return this.seqId;
	}
	
	/**
	 * set the seqId - 自增id，对应程序应该为long类型
	 */
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
	
	/**
	 * get the tagId - id，可以用显示名称代替之,TAG可以当做评论用么？
	 * @return the tagId
	 */
	public String getTagId() {
		return this.tagId;
	}
	
	/**
	 * set the tagId - id，可以用显示名称代替之,TAG可以当做评论用么？
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	/**
	 * get the targetId - 一般为项目标识，对应到iid，注意iid为item_id首选。（目标必须存在一个 128位以下的唯一键值）
	 * @return the targetId
	 */
	public String getTargetId() {
		return this.targetId;
	}
	
	/**
	 * set the targetId - 一般为项目标识，对应到iid，注意iid为item_id首选。（目标必须存在一个 128位以下的唯一键值）
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	/***GETTER-SETTER END***/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RsTagged [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "tagId=" + tagId  + ", "  
	 		+ "targetId=" + targetId  
		+ "]";
	}
	
	

}