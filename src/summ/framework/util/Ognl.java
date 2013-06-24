/**
 * 
 */
package summ.framework.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * 主要用于mybatis配置文件中ognl语法调用 
 * 
 * @author wfeng007
 * @date 2012-2-24 上午01:26:31 
 * 
 */
public class Ognl {
	/**
	 * test for Map,Collection,String,Array isEmpty
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException { //illegal?
//		System.out.println("isEmpty o:"+o); //太频繁的调用需要优化
		if (o == null)
			return true;

		if (o instanceof String) {
			if (((String) o).length() == 0) {
				return true;
			}
		} else if (o instanceof Collection) {
			if (((Collection) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (Array.getLength(o) == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).isEmpty()) {
				return true;
			}
		} else {
			return false;
		}
		
		return false;
	}

	/**
	 * test for Map,Collection,String,Array isNotEmpty
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	/**
	 * 判断是否是sql合法的比较运算符 暂时无用
	 * @param o
	 * @return
	 */
	public static boolean isSqlComparisonOperator (Object o) {
//		if(o==null)return false;
		String str=((o==null)||(o instanceof String))?(String)o:o.toString(); //String str=(String)null;?
		if(str==null)return false;
		if("<".equalsIgnoreCase(str) 
			|| ">".equalsIgnoreCase(str) 
			|| "<>".equalsIgnoreCase(str) 
			|| "=".equalsIgnoreCase(str) 
			|| "<=".equalsIgnoreCase(str)
			|| ">=".equalsIgnoreCase(str)
			|| "!=".equalsIgnoreCase(str)
			|| "IS".equalsIgnoreCase(str) //
			|| "IS NOT".equalsIgnoreCase(str) //is not null ? IS+NULL?
			|| "LIKE".equalsIgnoreCase(str)
				)return true;
		return false;
	}
	
	/**
	 * 判断是否是sql合法的比较运算符 暂时无用
	 * @param o
	 * @return
	 */
	public static boolean isNotSqlComparisonOperator (Object o) {
		return !isSqlComparisonOperator(o);
	}
	
	public static boolean isSqlLogicalOperator(Object o) {
		String str=((o==null)||(o instanceof String))?(String)o:o.toString(); //String str=(String)null;?
		if(str==null)return false;
		if("AND".equalsIgnoreCase(str) 
			|| "OR".equalsIgnoreCase(str) 
			|| "NOT".equalsIgnoreCase(str) 
				)return true;
		return false;
	}
	
	public static boolean isNotSqlLogicalOperator(Object o) {
		return !isSqlLogicalOperator(o);
	}
	
}
