/**
 * 
 */
package util.DbMeta.neogen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author wfeng007
 * @date 2011-9-14 下午11:13:23
 *
 */
public class JavaMeta4Query {
	
	private QueryMeta queryMeta;
	
	//当前默认就是内部query的名称
	public String getName(){
		return this.queryMeta.getName();
	}
	
	public JavaMeta4Query(QueryMeta queryMeta){
		if(queryMeta==null)throw new NullPointerException("QueryMeta not be NULL!!");
		this.queryMeta =queryMeta;
		this.className=mapTableToClass(this.queryMeta.getName()); //query name -> class name
		this.propertyMetaList=mapColumnListToPropertyList(queryMeta.getColumnMetaList());
	}
	
	private String className;
	private List<JavaPropertyMeta4Query> propertyMetaList;
	
	public static List<JavaPropertyMeta4Query> mapColumnListToPropertyList(List<QueryColumnMeta> columnMetaList) {
		List<JavaPropertyMeta4Query> propertyMetaList=new ArrayList<JavaPropertyMeta4Query>();
		for (QueryColumnMeta queryColumnMeta : columnMetaList) {
			JavaPropertyMeta4Query propertyMeta=new JavaPropertyMeta4Query(queryColumnMeta);
			propertyMetaList.add(propertyMeta);
		}
		return propertyMetaList;
	}
	
	/**
	 * xxx_xxx_xxx => XxxXxxXxx
	 * 
	 * 将数据库的表的名字转换成java类的名字
	 * @param 数据库表名
	 * @return 转换后的类的名字.
	 */
	public static String mapTableToClass(String tableName) {
		
//		StringBuffer buffer = new StringBuffer();
//		char[] cs = tableName.toLowerCase().trim().toCharArray(); //先将表名转换为小写
//
//		//将表名的首字母转换成大写
//		if (cs[0] >= 97 && cs[0] <= 122) {
//			cs[0] = (char) (cs[0] - 32);
//		}
//		//将_后的字母转换成大写 并忽略_
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_') {  //将_之后转换为大写并分析一下一个字节
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//
//		return buffer.toString();
		return NameUtils.dbTableNameToJavaClassName(tableName);
	}
	

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}


	/**
	 * @return the queryMeta
	 */
	public QueryMeta getQueryMeta() {
		return queryMeta;
	}


	/**
	 * @return the propertyMetaList
	 */
	public List<JavaPropertyMeta4Query> getPropertyMetaList() {
		return propertyMetaList;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JavaMeta4Query [queryMeta=" + queryMeta + ", className="
				+ className + ", propertyMetaList=" + propertyMetaList + "]";
	}
	
}
