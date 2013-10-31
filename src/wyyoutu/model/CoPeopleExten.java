  /*
   * Generate time : 2013-10-20 14:46:12
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.HashMap;
import java.util.Map;
/**
 * CoPeopleExten 
 *  
 */
public class CoPeopleExten {

	private Long seqId ; 
	private String peopleId ;	/* 用户id*/
	private String extenKey ;	/* 扩展键*/
	private String extenValue ;	/* 扩展值*/


	/**
	 * the constructor
	 */
	public CoPeopleExten() {
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
	 * get the peopleId - 用户id
	 * @return the peopleId
	 */
	public String getPeopleId() {
		return this.peopleId;
	}
	
	/**
	 * set the peopleId - 用户id
	 */
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
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
		return "CoPeopleExten [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "peopleId=" + peopleId  + ", "  
	 		+ "extenKey=" + extenKey  + ", "  
	 		+ "extenValue=" + extenValue  
		+ "]";
	}
	
	

}