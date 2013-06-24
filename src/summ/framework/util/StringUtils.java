package summ.framework.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;



/**
 * String工具类，主要用于框架内部，可考虑Commons Lang的String工具类。
 * @author	杨福军
 * @version	1.2
 * @history	2006-11-22	杨福军创建。
 * @history 2007-05-14 王玉球添加int long float double 转换成字符串函数
 */
public abstract class StringUtils {
	

	public static final int INT_NEED = 4;
	public static final int SCALE_NEED = 3;
	public static final int NOT_NEED = 2;
	public static final int ALL_NEED = 1;
	
	public static int defaultType = SCALE_NEED;
	
	/**
	 * 将<code>String</code>首字母大写，其余字母不变。
	 * @param str 大写处理的String，可以为<code>null</code>
	 * @return 大写处理后String, 如果原字符串为空，将返回<code>null</code>
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * 将<code>String</code>首字母小写，其余字母不变。
	 * @param str 小写处理的String，可以为<code>null</code>
	 * @return 小写处理后String, 如果原字符串为空，将返回<code>null</code>
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		}
		else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		
		buf.append(str.substring(1));
		
		return buf.toString();
	}
	
	public static String defaultIfEmpty(String str, String defaultString) {
		
		if(str == null) {
			return defaultString;
		}
		if(str.trim().length() == 0) {
			return " ";
		}
		
		return str;
	}
	
	public static String toString(Object obj) {
		
		if(obj == null) {
			return null;
		}
		else if(obj instanceof Date) {
			
			return DateUtils.toDateStr((Date)obj);
		} 
		else {
			
			return obj.toString();
		}
		
	}
	
	/**
	 * 获取format字符串，以SCALE_NEED为默认type
	 * @param intLength 整数部分长度
	 * @param scaleLength 小数部分长度
	 * @return
	 */
	public static String getFormat(int intLength,int scaleLength){
//		String formatStr = "";
//		for(int i = 0;i<filedLength;i++){
//				formatStr +="0";
//		}
//		if(scaleLength != 0){
//			formatStr +=".";
//			for(int i=0;i<scaleLength;i++){
//				if(i == 0)
//					formatStr +="0";
//				else
//					formatStr +="#";
//			}
//		}
		return getFormat(intLength, scaleLength, defaultType);
	}

	/**
	 * 根据用户指定的整数部分长度、小数部分长度以及精度的类别，来得到format的字符串
	 * 其中精度类别包含以下几种：
	 * INT_NEED：指整数部分需要指定精度，不足的前面补0
	 * SCALE_NEED：指小数部分需要指定精度，不足的后面补0
	 * NOT_NEED：指所有的部分都不需要指定精度，有则显示
	 * ALL_NEED：指所有的部分都需要指定精度，不足的各自在前面、后面补0
	 * 
	 * @param intLength 整数部分长度
	 * @param scaleLength 小数部分长度
	 * @param type 得到format string的类型，指明是否需要特定精度
	 * @return
	 */
	public static String getFormat(int intLength,int scaleLength,int type){

		StringBuffer formatStr = new StringBuffer(intLength + scaleLength + 1);
		if (type == INT_NEED || type == ALL_NEED) {//需要整数部分的精度
			for (int i = 0; i < intLength - 1; i++) {
					formatStr.append("0");
			}
		}
		formatStr.append("0");//默认得到format string: 0
		
		if (scaleLength > 0) {
			formatStr.append(".");
			if (type == SCALE_NEED || type == ALL_NEED) {// 需要小数部分的精度
				for (int i = 0; i < scaleLength; i++) {
					formatStr.append("0");
				}
			} else {// 不需要小数部分的精度
				for (int i = 0; i < scaleLength; i++) {
					if (i == 0)
						formatStr.append("0");
					else
						formatStr.append("#");
				}
			}
		}
		
		return formatStr.toString();
	}

	
//	public static String toString(Object obj, EiColumn col) {
//		
//		return toString(obj, col.getFieldLength(), col.getScaleLength());
//	}
	
	public static String toString(Object obj, int filedLength, int scaleLength) {

		DecimalFormat format;
		int intLength = filedLength;//整数部分长度，默认等于总长度
		if(scaleLength > 0)//小数部分长度不为0，总长度减去小数部分长度及小数位
			intLength = intLength - scaleLength -1;

		if(obj == null){
			return null;
		}
		else if(obj instanceof Integer){
			if(((Integer)obj).intValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength));
			return format.format((Integer)obj);
		} else if(obj instanceof Long){
			if(((Long)obj).longValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength));
			return format.format((Long)obj);
		} else if(obj instanceof Float){
			if(((Float)obj).floatValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength));
			return format.format((Float)obj);
		} else if(obj instanceof Double){
			if(((Double)obj).doubleValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength));
			return format.format((Double)obj);
		} else if(obj instanceof BigDecimal){
			if(((BigDecimal) obj).doubleValue()<0)
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength));
			return format.format((BigDecimal)obj);
		} else if(obj instanceof Date){
			return DateUtils.toDateStr((Date)obj);
		}
		else {
			return obj.toString();
		}
		
	}
	
	public static String toString(Object obj, int filedLength, int scaleLength, int type) {

		DecimalFormat format;
		int intLength = filedLength;//整数部分长度，默认等于总长度
		if(scaleLength > 0)//小数部分长度不为0，总长度减去小数部分长度及小数位
			intLength = intLength - scaleLength -1;

		if(obj == null){
			return null;
		}
		else if(obj instanceof Integer){
			if(((Integer)obj).intValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength,type));
			return format.format((Integer)obj);
		} else if(obj instanceof Long){
			if(((Long)obj).longValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength,type));
			return format.format((Long)obj);
		} else if(obj instanceof Float){
			if(((Float)obj).floatValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength,type));
			return format.format((Float)obj);
		} else if(obj instanceof Double){
			if(((Double)obj).doubleValue() < 0)//如果当前对象小于0，整数部分长度减1（符号位）
				intLength--;	
			format = new DecimalFormat(getFormat(intLength,scaleLength,type));
			return format.format((Double)obj);
		} else if(obj instanceof BigDecimal){
			if(((BigDecimal) obj).doubleValue()<0)
				intLength--;
			format = new DecimalFormat(getFormat(intLength,scaleLength,type));
			return format.format((BigDecimal)obj);
		} else if(obj instanceof Date){
			return DateUtils.toDateStr((Date)obj);
		}
		else {
			return obj.toString();
		}
		
	}
	
	public static boolean hasContent(String s){
		if (s == null) return false;
		if ( s.trim().length() == 0 )return false;
		return true;
	}

}
