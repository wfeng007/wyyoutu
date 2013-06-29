/**
 * 
 */
package util.DbMeta.neogen;

import java.util.List;

/**
 * @author wfeng007
 * @date 2011-9-13 下午10:39:00
 *
 */
public class QueryMeta {
	private String name;
	private String sql;
	private List<QueryColumnMeta> columnMetaList;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}
	/**
	 * @return the columnMetaList
	 */
	public List<QueryColumnMeta> getColumnMetaList() {
		return columnMetaList;
	}
	/**
	 * @param columnMetaList the columnMetaList to set
	 */
	public void setColumnMetaList(List<QueryColumnMeta> columnMetaList) {
		this.columnMetaList = columnMetaList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryMeta [name=" + name + ", sql=" + sql + ", columnMetaList="
				+ columnMetaList + "]";
	}
	
	
	
}
