package summ.framework.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class DateUtils {

	/**
	 * public static String getCurrentDateString(String s) { Calendar calendar =
	 * Calendar.getInstance(TimeZone.getDefault()); SimpleDateFormat
	 * simpledateformat = new SimpleDateFormat(s);
	 * simpledateformat.setTimeZone(TimeZone.getDefault()); return
	 * simpledateformat.format(calendar.getTime()); }
	 * 
	 * public static String getDateString(Date date, String s) {
	 * SimpleDateFormat simpledateformat = new SimpleDateFormat(s); return
	 * simpledateformat.format(date); }
	 * 
	 * public static Date getDate(String s, String s1) { SimpleDateFormat
	 * simpledateformat = new SimpleDateFormat(s1); ParsePosition parseposition =
	 * new ParsePosition(0); return simpledateformat.parse(s, parseposition); }
	 * 
	 * public static Date addDays(String s, String s1, int i) { return
	 * addDays(getDate(s, s1), i); }
	 * 
	 * public static Date addDays(Date date, int i) { GregorianCalendar
	 * gregoriancalendar = new GregorianCalendar();
	 * gregoriancalendar.setTime(date); gregoriancalendar.add(5, i); return
	 * gregoriancalendar.getTime(); }
	 * 
	 * public static boolean isDateBetween(Date date, Date date1, Date date2) {
	 * return (date1.before(date) || date1.equals(date)) && (date.before(date2) ||
	 * date.equals(date2)); }
	 * 
	 * public static String getCalendarString(Calendar calendar, String s) {
	 * SimpleDateFormat simpledateformat = new SimpleDateFormat(s); return
	 * simpledateformat.format(calendar.getTime()); }
	 * 
	 * public static Calendar getCalendar(String s) { int i =
	 * Integer.parseInt(s.substring(0, 4)); int j =
	 * Integer.parseInt(s.substring(5, 7)) - 1; int k =
	 * Integer.parseInt(s.substring(8, 10)); Calendar calendar =
	 * Calendar.getInstance(); calendar.set(i, j, k); return calendar; }
	 */

	/**
	 * 默认日期格式：yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 默认时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认时间戳格式，到毫秒 yyyy-MM-dd HH:mm:ss SSS
	 */
	public static final String DEFAULT_DATEDETAIL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

	/**
	 * 其中date8代表yyyyMMdd 其中date10代表yyyy-MM-dd 其中time6代表HHmmss 其中time8代表HH:mm:ss
	 * 其中datetime14代表yyyyMMddHHmmss 其中datetime19代表yyyy-MM-dd HH:mm:ss
	 * 其中datemsel18代表yyyyMMddHHmmssSSSS
	 */

	/**
	 * date8 pattern: yyyyMMdd
	 */
	public static final String DATE8_PATTERN = "yyyyMMdd";

	/**
	 * date10 pattern: yyyy-MM-dd
	 */
	public static final String DATE10_PATTERN = "yyyy-MM-dd";

	/**
	 * time6 pattern: HHmmss
	 */
	public static final String TIME6_PATTERN = "HHmmss";

	/**
	 * time8 pattern: HH:mm:ss
	 */
	public static final String TIME8_PATTERN = "HH:mm:ss";

	/**
	 * datetime14 pattern: yyyyMMddHHmmss
	 */
	public static final String DATETIME14_PATTERN = "yyyyMMddHHmmss";

	/**
	 * datetime19 pattern: yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATETIME19_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * datemsel18 pattern: yyyyMMddHHmmssSSSS
	 */
	public static final String DATEMSEL18_PATTERN = "yyyyMMddHHmmssSSSS";
	
	public static Date DefaultDate = null;

	//private static HashMap parsers = new HashMap();

	private static final Logger logger = Logger.getLogger(DateUtils.class);

	/*
	 * private static GregorianCalendar calendar = new GregorianCalendar();
	 * 
	 * //设置一周的第一天为周一 static { calendar.setFirstDayOfWeek(Calendar.MONDAY); }
	 */

	/*
	 * private static GregorianCalendar getCalendar(){ if(calendar == null) {
	 * calendar = new GregorianCalendar();
	 * calendar.setFirstDayOfWeek(Calendar.MONDAY); } return calendar; }
	 */

	/**
	 * 由于安全原因,修改实现,不对format进行缓存
	 * edited by zhong at 20070910161052
	 */
	private static SimpleDateFormat getDateParser(String pattern) {
		
		return new SimpleDateFormat(pattern);
/*		Object parser = parsers.get(pattern);
		if (parser == null) {
			parser = new SimpleDateFormat(pattern);
			parsers.put(pattern, parser);
		}
		return (SimpleDateFormat) parser;
*/	}

	/**
	 * 取得当前系统日期
	 * 
	 * @return
	 */
	public static Date curDate() {
		return new Date();
	}

	/**
	 * 取得系统当前日期，返回指定日期格式的字符串。
	 * 
	 * @param strFormat 日期格式字符串
	 * @return
	 */
	public static String curDateStr(String strFormat) {
		Date date = new Date();
		return getDateParser(strFormat).format(date);
	}

	/**
	 * 取得系统当前日期，返回默认日期格式的字符串(yyyy-MM-dd)
	 * 
	 * @return
	 */
	public static String curDateStr() {
		Date date = new Date();
		return getDateParser(DEFAULT_DATE_PATTERN).format(date);
	}

	/**
	 * 取得当前系统时间戳
	 * 
	 * @return
	 */
	public static Timestamp curTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 将日期字符串转换为java.util.Date对象
	 * 
	 * @param dateString
	 * @param pattern
	 *            日期格式
	 * 
	 * @return
	 * 
	 */
	public static Date toDate(String dateString, String pattern) {
		Date date = null;
		try
		{
		 date = getDateParser(pattern).parse(dateString);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateString + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将日期字符串转换为java.util.Date对象，使用默认日期格式(yyyy-MM-dd)
	 * 
	 * @param dateString
	 * @return
	 */
	public static java.util.Date toDate(String dateString) {
		Date date = null;
		try
		{
		 date = getDateParser(DEFAULT_DATE_PATTERN).parse(dateString);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateString + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将时间字符串转换为java.util.Date对象,使用默认时间格式(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param dateString
	 * @return
	 */
	public static java.util.Date toDateTime(String dateString) {
		
		Date date = null;
		try
		{
		 date = getDateParser(DEFAULT_DATETIME_PATTERN).parse(dateString);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateString + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将java.util.Date对象转换为字符串
	 * 
	 * @param date 日期
	 * @param pattern 日期格式字符串
	 * @return
	 */
	public static String toDateStr(java.util.Date date, String pattern) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}
		return getDateParser(pattern).format(date);
	}

	/**
	 * 将java.util.Date对象转换为字符串，使用默认日期格式(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateStr(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DEFAULT_DATE_PATTERN).format(date);
	}

	/**
	 * 将java.util.Date对象转换为时间字符串，使用默认时间格式(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateTimeStr(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DEFAULT_DATETIME_PATTERN).format(date);
	}

	/**
	 * 新增若干方法,方便使用 
	 * 其中date8代表yyyyMMdd 
	 * 其中date10代表yyyy-MM-dd 
	 * 其中time6代表HHmmss
	 * 其中time8代表HH:mm:ss 
	 * 其中datetime14代表yyyyMMddHHmmss 
	 * 其中datetime19代表yyyy-MM-dd HH:mm:ss 
	 * 其中datemsel18代表yyyyMMddHHmmssSSSS
	 */

	/**
	 * 获取8位的当前时间str 以yyyyMMdd为pattern
	 */
	public static String curDateStr8() {
		java.util.Date date = new java.util.Date();
		return getDateParser(DATE8_PATTERN).format(date);
	}

	/**
	 * 获取10位的当前时间str 以yyyy-MM-dd为pattern
	 */
	public static String curDateStr10() {
		java.util.Date date = new java.util.Date();
		return getDateParser(DATE10_PATTERN).format(date);
	}

	/**
	 * 获取14位的当前时间str 以yyyyMMddHHmmss为pattern
	 */
	public static String curDateTimeStr14() {
		java.util.Date date = new java.util.Date();
		return getDateParser(DATETIME14_PATTERN).format(date);
	}

	/**
	 * 获取19位的当前时间str 以yyyy-MM-dd HH:mm:ss为pattern
	 */
	public static String curDateTimeStr19() {
		java.util.Date date = new java.util.Date();
		return getDateParser(DATETIME19_PATTERN).format(date);
	}

	/**
	 * 获取6位的当前时间str 以HHmmss为pattern
	 */
	public static String curTimeStr6() {
		java.util.Date date = new java.util.Date();
		return getDateParser(TIME6_PATTERN).format(date);
	}

	/**
	 * 获取18位的当前时间str 以yyyyMMddHHmmssSSSS为pattern
	 */
	public static String curDateMselStr18() {
		java.util.Date date = new java.util.Date();
		return getDateParser(DATEMSEL18_PATTERN).format(date);
	}

	/**
	 * 将日期字符串转为日期格式,以yyyyMMdd为pattern
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date toDate8(String dateStr) {
		
		Date date = null;
		try
		{
		 date = getDateParser(DATE8_PATTERN).parse(dateStr);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateStr + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将日期字符串转为日期格式,以yyyy-MM-dd为pattern
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date toDate10(String dateStr) {
		
		Date date = null;
		try
		{
		 date = getDateParser(DATE10_PATTERN).parse(dateStr);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateStr + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将日期字符串转为日期格式,以yyyyMMddHHmmss为pattern
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date toDateTime14(String dateStr) {
		
		Date date = null;
		try
		{
		 date = getDateParser(DATETIME14_PATTERN).parse(dateStr);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateStr + "ex:" + e);
			return null;
		}
		return date;

	}

	/**
	 * 将日期字符串转为日期格式,以yyyy-MM-dd HH:mm:ss为pattern
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date toDateTime19(String dateStr) {
		
		Date date = null;
		try
		{
		 date = getDateParser(DATETIME19_PATTERN).parse(dateStr);
		}catch(Exception e)
		{
			logger.warn("解析date字符串时出错,返回null. dateString:" + dateStr + "ex:" + e);
			return null;
		}
		return date;
	}

	/**
	 * 将日期转为字符串,以yyyyMMdd为pattern
	 * @param date
	 * @return
	 */
	public static String toDateStr8(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DATE8_PATTERN).format(date);
	}

	/**
	 * 将日期转为字符串,以yyyy-MM-dd为pattern
	 * @param date
	 * @return
	 */
	public static String toDateStr10(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DATE10_PATTERN).format(date);
	}

	/**
	 * 将日期转为字符串,以yyyyMMddHHmmss为pattern
	 * @param date
	 * @return
	 */
	public static String toDateTimeStr14(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DATETIME14_PATTERN).format(date);
	}

	/**
	 * 将日期转为字符串,以yyyy-MM-dd HH:mm:ss为pattern
	 * @param date
	 * @return
	 */
	public static String toDateTimeStr19(java.util.Date date) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回空字符串");
			return "";
		}

		return getDateParser(DATETIME19_PATTERN).format(date);
	}


	/**
	 * 获取一个日期增减若干天后的日期
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回null");
			return null;
		}

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 判断一个日期是否介于两个日期之间
	 * @param date
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDateBetween(Date date, Date date1, Date date2) {
		return (date1.before(date) || date1.equals(date))
				&& (date.before(date2) || date.equals(date2));
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @return 间隔天数
	 */
	public static int getDaysInterval(Date fromDate, Date toDate){
		if(fromDate == null || toDate == null)
		{
			logger.warn("getDaysInterval时,传入的对象为空,返回默认值0");
			return 0;
		}
		long timeInterval = toDate.getTime() - fromDate.getTime();
		int daysInterval = (int)(timeInterval/(24*3600*1000));
		return daysInterval;
	}
	
	/**
	 * 获取日期为本年的第几个星期
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date){
		if(date == null)
		{
			logger.warn("传入的对象为空,返回默认值-1");
			return -1;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	/**
	 * 获取日期为星期几
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date){
		if(date == null)
		{
			logger.warn("传入的对象为空,返回默认值-1");
			return -1;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day -= 1;
		if(0 == day)
			day = 7;
		return day;
	}

	/**
	 * 获取所在月的最后一天的日期
	 * @param date
	 * @return
	 */
	//似乎比原来的方法更简洁,替换原实现
	public static Date getLastDayInMonth(Date date) {
		return getLastDayInMonth(date,0);
	}

	/**
	 * 获取下一月的最后一天的日期
	 * @param date
	 * @return
	 */
	public static Date getLastDayInNextMonth(Date date) {
		return getLastDayInMonth(date,1);
	}

	/**
	 * 获取传入日期前后若干月的最后一天的日期
	 * @param date 传入的日期
	 * @param i 与当前传入日期相隔的月数
	 * @return
	 */
	public static Date getLastDayInMonth(Date date, int i) {
		if(date == null)
		{
			if(logger.isInfoEnabled())
				logger.info("传入的date对象为空,返回null");
			return null;
		}

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i+1);
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获取所在月的最后一天的日期
	 * 不如上面的方法简洁,取消
	 * @param date
	 * @return
	 */
	/*
	public static Date getLastDayInMonth(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);

		int lastDay = getDaysInMonth(calendar.isLeapYear(year), month);
		
		calendar.set(Calendar.DATE, lastDay);

		return calendar.getTime();
	}

	private static int getDaysInMonth(boolean isLeap, int month) {
		int days = 31;
		switch (month+1) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			if (isLeap)
				days = 29;
			else
				days = 28;
		}
		return days;
	}
	*/
	
	public static String toDateTime(long times) {
		  times = times/1000;
	      long hours, minutes, seconds;
	      hours = times / 3600;
	      times = times - (hours * 3600);
	      minutes = times / 60;
	      times = times - (minutes * 60);
	      seconds = times;
	      String result = hours + "(h) " + minutes + "(m) " + seconds + "(s)";
	      return result;
	   }

}
