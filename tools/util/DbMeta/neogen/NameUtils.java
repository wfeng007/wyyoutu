/**
 * 
 */
package util.DbMeta.neogen;

/**
 * @author wfeng007
 * @date 2012-2-14 上午10:57:09
 *
 */
public final class NameUtils {
	
	/**
	 * 表名转换为Java类名
	 * @param tableName
	 * @return
	 */
	public static String dbTableNameToJavaClassName(String tableName){
		StringBuffer buffer = new StringBuffer();
		char[] cs = tableName.toLowerCase().trim().toCharArray(); //先将表名转换为小写

		//将表名的首字母转换成大写
		if (cs[0] >= 97 && cs[0] <= 122) {
			cs[0] = (char) (cs[0] - 32);
		}
		//将_后的字母转换成大写 并忽略_
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] == '_') {  //将_之后转换为大写并分析一下一个字节
				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
					cs[i + 1] = (char) (cs[i + 1] - 32);
				}
			} else
				buffer.append(cs[i]);
		}

		return buffer.toString();
	}
	
	/**
	 * 表列名转换为Java类的属性名(或变量命名规则)
	 * @param columnName
	 * @return
	 */
	public static String dbColumnNameToJavaPropertyName(String columnName){
		StringBuffer buffer = new StringBuffer();
		char[] cs = columnName.toLowerCase().trim().toCharArray(); //将字段名转换成小写

		for (int i = 0; i < cs.length; i++) {

			if (cs[i] == '_' && i != 1 && (i + 1) < cs.length) { //后的字母转换成大写 忽略第一个字母-第一个字母必须为小写
				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
					cs[i + 1] = (char) (cs[i + 1] - 32);
				}
			} else
				buffer.append(cs[i]);
		}

		return buffer.toString();
	}
	
	

}
