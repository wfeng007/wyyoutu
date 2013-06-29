/**
 * 
 */
package util.DbMeta.neogen;

/**
 * @author wfeng007
 * @date 2011-9-15 上午12:07:43
 *
 */
public class JavaPropertyMeta4Query {
	
	private QueryColumnMeta queryColumnMeta;
	public JavaPropertyMeta4Query(QueryColumnMeta queryColumnMeta){
		this.queryColumnMeta =queryColumnMeta;
//		this.className=mapTableToClass(this.queryMeta.getName());
		this.javaTypeName=this.queryColumnMeta.getColumnClassName();
		this.propertyName=mapColumnToProperty(this.queryColumnMeta.getColumnName());
		this.shortJavaTypeName=mapToShortJavaType(this.queryColumnMeta.getColumnClassName());
	}
	
	private String javaTypeName;
	
	private String shortJavaTypeName;
	
	private String propertyName;
	
	/**
	 * 需要修订
	 * @param columnClassName
	 * @return
	 */
	public static String mapToShortJavaType(String columnClassName){
		if(columnClassName==null){
			return null; 
		}
		return columnClassName.substring(columnClassName.lastIndexOf('.')+1);
	}
	
	/**
	 * xxx_xxx_xxx => xxxXxxXxx
	 * 
	 * 将数据库的字段的名字转换成类的字段名
	 * @param 数据库字段的名字.
	 * @return 转换后的field的名字� 
	 */
	public static String mapColumnToProperty(String columnName) {
//		StringBuffer buffer = new StringBuffer();
//		char[] cs = columnName.toLowerCase().trim().toCharArray(); //将字段名转换成小冄1�7
//
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_' && i != 1 && (i + 1) < cs.length) { //后的字母转换成大冄1�7
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//
//		return buffer.toString();
		return NameUtils.dbColumnNameToJavaPropertyName(columnName);
	}
	/**
	 * @return the queryColumnMeta
	 */
	public QueryColumnMeta getQueryColumnMeta() {
		return queryColumnMeta;
	}
	/**
	 * @return the javaTypeName
	 */
	public String getJavaTypeName() {
		return javaTypeName;
	}
	/**
	 * @return the shortJavaTypeName
	 */
	public String getShortJavaTypeName() {
		return shortJavaTypeName;
	}
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JavaProperityMeta4Query [queryColumnMeta=" + queryColumnMeta
				+ ", javaTypeName=" + javaTypeName + ", shortJavaTypeName="
				+ shortJavaTypeName + ", propertyName=" + propertyName + "]";
	}
	
	
}
