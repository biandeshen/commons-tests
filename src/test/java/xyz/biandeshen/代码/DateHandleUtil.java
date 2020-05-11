package xyz.biandeshen.代码;

import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: WangRui
 * @create: 2018/8/2 13:45
 * @description:
 **/

public class DateHandleUtil {
	public static final String DefaultTime = "1970-01-01 00:00:00";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_DD = "yyyy-MM-dd";
	private static final String DATE_FORMAT2 = "yyyy/MM/dd HH:mm:ss";
	private static final String DATE_FORMATSS = "yyyy-MM-dd HH:mm:ss:SSS";
	private static final String DATE_FORMATY = "yyMMdd";
	private static final String DATE_FORMATM = "yyyyMM";
	private static final String DATE_FORMATHS = "HHmmss";
	private static final String DATE_FORMATHMS = "HH:mm:ss";
	private static final String DATE_FORMAT_MM = "yyyy-MM";
	
	private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	
	@ApiModelProperty(value = "获取自1970-1-1 00:00:00 到现在的秒数")
	public static Long getCurSeconds() {
		//获取毫秒数
		Long curTime = System.currentTimeMillis();
		//获取秒数
		long totalSeconds = curTime / 1000;
		return totalSeconds;
	}
	
	@ApiModelProperty(value = "获取当前日期  格式为 yyyy-MM-dd HH:mm:ss SSS")
	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATSS);
		return sdf.format(new Date());
	}
	
	
	@ApiModelProperty(value = "获取当前日期  格式为 yyyyMMdd")
	public static String getyyyyMMdd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	
	@ApiModelProperty(value = "获取当前时间 格式为 yyyy-MM-dd HH:mm:ss")
	public String getCurTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(new Date());
	}
	
	
	public static String getDateStr(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMAT).format(date);
		return dateStr;
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocalss = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMATSS);
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocal = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMAT);
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocaldd = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMAT_DD);
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocal2 = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMAT2);
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocalHMS = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMATHMS);
		}
	};
	@SuppressWarnings("rawtypes")
	private static ThreadLocal threadLocalMM = new ThreadLocal() {
		@Override
		protected synchronized Object initialValue() {
			return new SimpleDateFormat(DATE_FORMAT_MM);
		}
	};
	
	public static DateFormat getDateFormatSS() {
		return (DateFormat) threadLocalss.get();
	}
	
	public static DateFormat getDateFormat() {
		return (DateFormat) threadLocal.get();
	}
	
	public static DateFormat getDateFormat2() {
		return (DateFormat) threadLocal2.get();
	}
	
	public static DateFormat getDateFormatDD() {
		return (DateFormat) threadLocaldd.get();
	}
	
	public static DateFormat getDateFormatHMS() {
		return (DateFormat) threadLocalHMS.get();
	}
	
	public static DateFormat getDateFormatMM() {
		return (DateFormat) threadLocalMM.get();
	}
	
	
	public static String formateDate3(Date t) {
		try {
			return getDateFormat2().format(t);
		} catch (Exception e) {
			logger.error("formateDate3出错", e);
			return null;
		}
	}
	
	/**
	 * <p>Title: getString2DateYY<／p>
	 * <p>Description: YYYYMMddHH24MISS转换成java.util.Date<／p>
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static Date getString2DateYY(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				String year = strDate.substring(0, 4);
				String month = strDate.substring(4, 6);
				String day = strDate.substring(6, 8);
				String hour = strDate.substring(8, 10);
				String min = strDate.substring(10, 12);
				String sec = strDate.substring(12, 14);
				String str = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
				date = getString2Date(str);
			} catch (Exception e) {
				logger.error("getString2DateYY出错。", e);
			}
		}
		return date;
	}
	
	
	/**
	 * 将字符串日期yyyy-MM-dd HH:mm:ss转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2Date(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormat().parse(strDate);
			} catch (ParseException e) {
				logger.error("getString2Date出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 将字符串日期yyyy-MM-dd HH:mm:ss:SSS转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2DateSS(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormatSS().parse(strDate);
			} catch (ParseException e) {
				logger.error("getString2DateSS出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 将字符串日期yyyy/MM/dd HH:mm:ss转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2DateOther(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormat2().parse(strDate);
			} catch (ParseException e) {
				logger.error("getString2DateOther出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 将字符串日期yyyy-MM-dd转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2DateDD(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormatDD().parse(strDate);
			} catch (ParseException e) {
				// date = defaultTime();
				logger.error("getString2DateDD出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 将字符串日期yyyy-MM 转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2DateMM(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormatMM().parse(strDate);
			} catch (ParseException e) {
				// date = defaultTime();
				logger.error("getString2DateMM出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 取某天零点零分零秒零毫秒
	 */
	public static Date getAnydayStart(int addDay) {
		Calendar calendar = Calendar.getInstance();
		// 如果没有这种设定的话回去系统的当期的时间
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, addDay);
		Date date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * Title: getString2DateHMS Description:将字符串时间HH-mm-ss 转换为java.util.Date
	 *
	 * @param strDate
	 *
	 * @return
	 */
	public static final Date getString2DateHMS(String strDate) {
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = getDateFormatHMS().parse(strDate);
			} catch (ParseException e) {
				logger.error("getString2DateHMS出错。", e);
			}
		}
		return date;
	}
	
	/**
	 * 获取默认时间
	 *
	 * @return
	 */
	public static Date defaultTime() {
		Date d = new Date();
		try {
			d = getDateFormat().parse(DefaultTime);
		} catch (Exception e) {
			logger.error("defaultTime出错。", e);
		}
		return d;
	}
	
	public static boolean isBeforeDefaultTime(Date timeSpan) {
		boolean flag = false;
		try {
			flag = timeSpan.before(defaultTime());
		} catch (Exception e) {
			logger.error("isBeforeDefaultTime出错。", e);
		}
		return flag;
	}
	
	/**
	 * Title: getDate
	 * <p>
	 * Description:获得时间 ，自定义day 和 hour
	 *
	 * @param day
	 * @param hour
	 *
	 * @return
	 */
	public static Date getDate(int day, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		Date date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * 获取某天之后的日期
	 *
	 * @param currDate
	 * @param days
	 *
	 * @return
	 */
	public static Date getDateAfter(Date currDate, int days) {
		long l = currDate.getTime() + MILLIS_PER_DAY * days;
		return new Date(l);
	}
	
	/**
	 * 获取某月之后的月份
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Date getNextMonth(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		Date nextDate = new Date(calendar.getTimeInMillis());
		return nextDate;
	}
	
	/**
	 * Title: sysDate
	 * <p>
	 * Description:获取系统时间
	 *
	 * @return Date
	 */
	public static Date sysDate() {
		return new Date();
	}
	
	/**
	 * Title: sysTime
	 * <p>
	 * Description:获取系统时间 毫秒数
	 *
	 * @return long
	 */
	public static long sysTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Title: getDateYY
	 * <p>
	 * Description:返回 YYMMDD类型
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateYY(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMATY).format(date);
		return dateStr;
	}
	
	/**
	 * Title: getDateYYMM
	 * <p>
	 * Description:返回 YYMM类型
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateYYMM(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMATM).format(date);
		return dateStr;
	}
	
	
	/**
	 * Title: getDateYS
	 * <p>
	 * Description:返回yyMMddHHmmss格式
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateHS(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMATHS).format(date);
		return dateStr;
	}
	
	/**
	 * Title: getDateYMD
	 * <p>
	 * Description:返回yyyy-MM-dd格式
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateYMD(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMAT_DD).format(date);
		return dateStr;
	}
	
	/**
	 * Title: getDateYM
	 * <p>
	 * Description:返回yyyy-MM格式
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateYM(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMAT_MM).format(date);
		return dateStr;
	}
	
	/**
	 * Title: getMillis Description:将date时间转换成毫秒数
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Long getMillis(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			return (long) (((hour * 60 + minute) * 60 + second) * 1000);
		}
		return null;
	}
	
	/**
	 * 几天前或者几天后的当前时间
	 */
	public static Date getSystemTime(int addDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, addDay);
		Date date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * Title: getDateSStr Description:
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getDateSStr(Date date) {
		String dateStr = new SimpleDateFormat(DATE_FORMATSS).format(date);
		return dateStr;
	}
	
	/**
	 * Title: getDateSStr Description:取N小时后到 时间
	 *
	 * @param n
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static Date getTimeAfterNHour(int n) {
		Date d = new Date();
		return getTimeAfterNHour(d, n);
	}
	
	/**
	 * <p>Title: getTimeAfterNHour<／p>
	 * <p>Description: 将date延后n小时<／p>
	 *
	 * @param date
	 * @param n
	 *
	 * @return
	 */
	public static Date getTimeAfterNHour(Date date, int n) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) + n);
		
		return now.getTime();
	}
	
	/**
	 * <p>Title: getTimeAfterNMinutes<／p>
	 * <p>Description: 将date延后n分钟<／p>
	 *
	 * @param date
	 * @param n
	 *
	 * @return
	 */
	public static Date getTimeAfterNMinutes(Date date, int n) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + n);
		return now.getTime();
	}
	
	
	/**
	 * @param date
	 *
	 * @return Date
	 *
	 * @author zhangzhongdi
	 * @time 2013-10-2 上午2:58:44
	 * @describe 将传过来的date进行格式化成0点0分0秒，年月日不变，并返回
	 */
	public static Date formatDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化成23点59分59秒，年月日不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @param date
	 * 		时间
	 *
	 * @return <p>
	 *
	 * @author 邱晓晨
	 * @time 2014-6-24 下午5:15:17
	 * @since 1.0
	 */
	public static Date formatDateTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化当月第一天的0点0分0秒0毫秒，年月不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @param date
	 * 		date
	 *
	 * @return <p>
	 *
	 * @since 1.0
	 */
	public static Date getFormatFirstDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化当月最后一天的23点59分59秒，年月不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @param date
	 * 		date
	 *
	 * @return <p>
	 *
	 * @since 1.0
	 */
	public static Date getFormatLastDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		int days = calendar.get(Calendar.DATE);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, days);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	
	/**
	 * 方法功能描述：将传过来的date进行格式化当月第二天的23点59分59秒999毫秒，年月不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @param date
	 * 		date
	 *
	 * @return <p>
	 *
	 * @since 1.0
	 */
	public static Date getFormatSecondDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 2);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		date = new Date(calendar.getTimeInMillis());
		return date;
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化上月最后一天，时间不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @return <p>
	 *
	 * @author 邱晓晨
	 * @time 2015-7-8 下午5:15:48
	 * @since 1.0
	 */
	public static Date getFormatLastMonthLastDate() {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化下月的某一天，时间不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @return <p>
	 *
	 * @author 邱晓晨
	 * @time 2015-7-8 下午5:15:48
	 * @since 1.0
	 */
	public static Date getFormatNextMonthSomeDate(int day) {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 方法功能描述：将传过来的date进行格式化下月的某一天，时间不变，并返回
	 * <p>
	 * <b>参数说明</b>
	 *
	 * @return <p>
	 *
	 * @author 邱晓晨
	 * @time 2015-7-8 下午5:15:48
	 * @since 1.0
	 */
	public static Date getFormatThisMonthSomeDate(int day) {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 取得上月第一天  贾惠有
	 *
	 * @return
	 */
	public static String getFormatBefMonthFirstDayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = new Date(calendar.getTimeInMillis());
		return getDateStr(date);
	}
	
	/**
	 * 取得上月最后一天  贾惠有
	 *
	 * @return
	 */
	public static String getFormatBefMonthLastDayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date date = new Date(calendar.getTimeInMillis());
		return getDateStr(date);
	}
	
}
