  /*
   * Generate time : 2013-11-18 21:31:26
   * Version : 3.0.0
   */
package wyyoutu.model;

import java.util.HashMap;
import java.util.Map;
/**
 * RsBoardItemRef 
 *  
 */
public class RsBoardItemRef {

	private Long seqId ; 
	private String itemIid ;	/* 项目id*/
	private String boardBid ;	/* 展板id*/
	private Integer refSeqNum ; /* 展板中的序号,1开始记录。*/


	/**
	 * the constructor
	 */
	public RsBoardItemRef() {
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
	 * get the itemIid - 项目id
	 * @return the itemIid
	 */
	public String getItemIid() {
		return this.itemIid;
	}
	
	/**
	 * set the itemIid - 项目id
	 */
	public void setItemIid(String itemIid) {
		this.itemIid = itemIid;
	}
	
	/**
	 * get the boardBid - 展板id
	 * @return the boardBid
	 */
	public String getBoardBid() {
		return this.boardBid;
	}
	
	/**
	 * set the boardBid - 展板id
	 */
	public void setBoardBid(String boardBid) {
		this.boardBid = boardBid;
	}
	
	/**
	 * get the refSeqNum - 展板中的序号,1开始记录。
	 * @return the refSeqNum
	 */
	public Integer getRefSeqNum() {
		return this.refSeqNum;
	}
	
	/**
	 * set the refSeqNum - 展板中的序号,1开始记录。
	 */
	public void setRefSeqNum(Integer refSeqNum) {
		this.refSeqNum = refSeqNum;
	}
	
	/***GETTER-SETTER END***/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RsBoardItemRef [" 
	 		+ "seqId=" + seqId  + ", "  
	 		+ "itemIid=" + itemIid  + ", "  
	 		+ "boardBid=" + boardBid  + ", "  
	 		+ "refSeqNum=" + refSeqNum  
		+ "]";
	}
	
	

}