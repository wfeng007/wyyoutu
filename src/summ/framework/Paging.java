/**
 * 
 */
package summ.framework;

/**
 * @author wfeng007
 * @date 2011-10-24 下午04:13:54
 *
 */
public class Paging {
	
	
	/* (non-Javadoc)
	 * @see ecdata.iccs.framework.Pagingable#getTotalPage()
	 */
	public int getTotalPage() {
		int pageCount=totalCount/countPerPage;
		pageCount=pageCount+((totalCount%countPerPage)>0?1:0);
		return pageCount;
	}
	
	private int totalCount;
	private int currentPosition=1; //初始的时候为1
	private int countPerPage=15;
	
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
	
	/**
	 * 0起计数
	 * 第一页
	 * @param pagePosition
	 */
	public void setCurrentPagePosition(int pagePosition){
		int p=pagePosition;
		if(p<0)p=0;
		this.currentPosition=((pagePosition*this.countPerPage)+1);
	}
	
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * @return the currentPosition
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * @return the currentPosition
	 */
	public int getCurrentPositionWithZeroStart() {
		return currentPosition-1;
	}
	
	/**
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	
	
	/**
	 * @return the countPerPage
	 */
	public int getCountPerPage() {
		return countPerPage;
	}
	/**
	 * @param countPerPage the countPerPage to set
	 */
	public void setCountPerPage(int countPerPage) {
		
		this.countPerPage = countPerPage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Paging [totalCount=" + totalCount + ", currentPosition="
				+ currentPosition + ", countPerPage=" + countPerPage
				+ ", getTotalPage()=" + getTotalPage() + "]";
	}
	
	
}
