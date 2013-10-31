  /*
   * Generate time : 2013-10-31 19:06:37
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.HashMap;
import java.util.Map;
/**
 * RsItemExten 
 *  
 */
public class RsItemExten {

	private Long seqId ; 
	private String itemIid ;	/* 资源项iid*/
	private String extenKey ;	/* 扩展键*/
	private String extenValue ;	/* 扩展值*/


	/**
	 * the constructor
	 */
	public RsItemExten() {
		//TODO
	}
	
	

	/***GETTER-SETTER***/
	/**
	 * get the seqId 
	 * @return the seqId
	 */
	public Long getSeqId() {
		return this.seqId;
	}
	
	/**
	 * set the seqId 
	 */
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
	
	/**
	 * get the itemIid - 资源项iid
	 * @return the itemIid
	 */
	public String getItemIid() {
		return this.itemIid;
	}
	
	/**
	 * set the itemIid - 资源项iid
	 */
	public void setItemIid(String itemIid) {
		this.itemIid = itemIid;
	}
	
	/**
	 * get the extenKey - 扩展键
	 * @return the extenKey
	 */
	public String getExtenKey() {
		return this.extenKey;
	}
	
	/**
	 * set the extenKey - 扩展键
	 */
	public void setExtenKey(String extenKey) {
		this.extenKey = extenKey;
	}
	
	/**
	 * get the extenValue - 扩展值
	 * @return the extenValue
	 */
	public String getExtenValue() {
		return this.extenValue;
	}
	
	/**
	 * set the extenValue - 扩展值
	 */
	public void setExtenValue(String extenValue) {
		this.extenValue = extenValue;
	}
	
	/***GETTER-SETTER END***/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RsItemExten [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "itemIid=" + itemIid  + ", "  
	 		+ "extenKey=" + extenKey  + ", "  
	 		+ "extenValue=" + extenValue  
		+ "]";
	}
	
	

}