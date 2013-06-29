package util.DbMeta.neogen;

/**
 * 
 * ResultSet Meta 
 * 
 * @author wfeng007
 *
 */
public class QueryColumnMeta {
	//
	//
	//
	//getCatalogName
	private String catalogName;
	//getColumnLabel
	private String columnName;
	//getColumnClassName 为长名一般为jdbc自动认出的java类型 比如 java.lang.String
	private String columnClassName;
	//getColumnDisplaySize 
	private int columnDisplaySize;
	//getSchemaName 模式 oracle一般为用户名称  mysql没有
	private String schemaName;
	//getColumnType 类型号
	private int columnType;
	//getColumnTypeName 数据库中的类型描述
	private String columnTypeName;
	//getTableName 字段所在表 多表查询时如何处理?
	private String tableName;
	//isAutoIncrement
	private boolean isAutoIncrement;
	//isNullable
	private int nullable;
	//isReadOnly 
	private boolean isReadOnly;
	//列能否出现在where中 isSearchable
	private boolean isSearchable;
	
	//
	// 
	//
	
	
	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}
	/**
	 * @param catalogName the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the columnClassName
	 */
	public String getColumnClassName() {
		return columnClassName;
	}
	/**
	 * @param columnClassName the columnClassName to set
	 */
	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	
	
	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}
	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	
	
	/**
	 * @return the columnTypeName
	 */
	public String getColumnTypeName() {
		return columnTypeName;
	}
	/**
	 * @param columnTypeName the columnTypeName to set
	 */
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the isAutoIncrement
	 */
	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	/**
	 * @param isAutoIncrement the isAutoIncrement to set
	 */
	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	/**
	 * @return the isReadOnly
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}
	/**
	 * @param isReadOnly the isReadOnly to set
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	/**
	 * @return the isSearchable
	 */
	public boolean isSearchable() {
		return isSearchable;
	}
	/**
	 * @param isSearchable the isSearchable to set
	 */
	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}
	/**
	 * @return the nullable
	 */
	public int getNullable() {
		return nullable;
	}
	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	/**
	 * @return the columnDisplaySize
	 */
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	/**
	 * @param columnDisplaySize the columnDisplaySize to set
	 */
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}
	/**
	 * @return the columnType
	 */
	public int getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryColumnMeta [catalogName=" + catalogName + ", columnName="
				+ columnName + ", columnClassName=" + columnClassName
				+ ", columnDisplaySize=" + columnDisplaySize + ", schemaName="
				+ schemaName + ", columnType=" + columnType
				+ ", columnTypeName=" + columnTypeName + ", tableName="
				+ tableName + ", isAutoIncrement=" + isAutoIncrement
				+ ", nullable=" + nullable + ", isReadOnly=" + isReadOnly
				+ ", isSearchable=" + isSearchable + "]";
	}
	
	
	
	
}
