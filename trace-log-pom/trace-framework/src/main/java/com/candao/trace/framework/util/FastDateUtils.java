package com.candao.trace.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import com.candao.trace.framework.bean.FastDateFormat;

/**
 * 时间帮助类
 * 
 */
public class FastDateUtils extends DateUtils{
	
	/** 锁对象 */
    private static final Object lockObj = new Object();
	
	/** 存放不同的日期模板格式的sdf的Map */
    private static final Map<FastDateFormat, ThreadLocal<SimpleDateFormat>> dateFormatMap = new HashMap<FastDateFormat, ThreadLocal<SimpleDateFormat>>();

    public static final long minutes_1 = 1 * 60 * 1000;
    public static final long minutes_3 = 3 * 60 * 1000;
    public static final long minutes_5 = 5 * 60 * 1000;
    public static final long minutes_10 = 10 * 60 * 1000;
    public static final long minutes_15 = 15 * 60 * 1000;
    public static final long minutes_20 = 20 * 60 * 1000;
    public static final long minutes_30 = 30 * 60 * 1000;
    public static final long minutes_45 = 45 * 60 * 1000;
    public static final long hours_1 = 1 * 60 * 60 * 1000;
    public static final long hours_2 = 1 * 60 * 60 * 1000;
    public static final long hours_3 = 1 * 60 * 60 * 1000;
    
	/**
	 * 是否为Timestamp
	 * @param dateStr
	 * @return
	 */
	public static Timestamp isTimestamp(String dateStr){
		
		Date date = isDate(dateStr);
		
		return date != null ? new Timestamp(date.getTime()) : null;
	}
	
	/**
	 * 是否为UTC时间格式
	 * @param dataStr
	 * @return
	 */
	public static Date isUTCDate(String dateStr){
		
		if(FastStringUtils.isBlank(dateStr)){
			return null;
		}
		
		String newDateStr = FastStringUtils.trimToEmpty(dateStr);
		
		Date date = null;
		
		try{
			
			int len = newDateStr.length();
			
			if(len == 20){
				date = getDate(newDateStr, FastDateFormat.UTC_DATE_TIME);
			}
			
			else if(len == 24){
				date = getDate(newDateStr, FastDateFormat.UTC_FULL_DATE_TIME);
			}
			
		}catch(Throwable e){
			// 转换异常则代表不是时间格式字符串
		}
		
		return date;
	}
	
	/**
	 * 是否为时间格式
	 * 	-格式包括(yyyy-MM-dd;yyyy-MM-dd HH:mm:ss;yyyy-MM-dd HH:mm:ss.S)
	 * @param dateStr
	 * @return
	 */
	public static Date isDate(String dateStr){
		
		if(FastStringUtils.isBlank(dateStr)){
			return null;
		}
		
		String newDateStr = FastStringUtils.trimToEmpty(dateStr);
		
		Date date = null;
		
		try{

			int len = newDateStr.length();
			
			// yyyy-MM-dd
			if(len == 10) date = getDate(newDateStr, FastDateFormat.DATE);
			
			// yyyy-MM-dd HH:mm:ss
			else if(len == 19) date = getDate(newDateStr, FastDateFormat.DATE_TIME);
			
			// yyyy-MM-dd HH:mm:ss.S
			else if(len == 21) date = getDate(newDateStr, FastDateFormat.FULL_DATE_TIME);
			
		}catch(Throwable e){
			// 转换异常则代表不是时间格式字符串
		}
		
		return date;
	}
	
	/**
	 * 第一次调用get将返回null<br/>
	 * 获取线程的变量副本，如果不覆盖initialValue，第一次get返回null，<br/>
	 * 故需要初始化一个SimpleDateFormat，并set到threadLocal中<br/>
	 * @return
	 */
	public static DateFormat getDateFormat() {
		return getDateFormat(FastDateFormat.DATE_TIME);
	}
	
	public static DateFormat getUTFDateFormat(){
		
		return getDateFormat(FastDateFormat.UTC_DATE_TIME);
	}
	
	/**
	 * 第一次调用get将返回null<br/>
	 * 获取线程的变量副本，如果不覆盖initialValue，第一次get返回null，<br/>
	 * 故需要初始化一个SimpleDateFormat，并set到threadLocal中<br/>
	 * @param fastDateFormat
	 * @return
	 */
	public static synchronized DateFormat getDateFormat(final FastDateFormat fastDateFormat) {
		
		if(fastDateFormat == null) return null;
		
		ThreadLocal<SimpleDateFormat> tl = dateFormatMap.get(fastDateFormat);
		
		if(tl == null){
			synchronized (lockObj) {
                tl = dateFormatMap.get(fastDateFormat);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fastDateFormat.value);
                            if(fastDateFormat.equals(FastDateFormat.UTC_FULL_DATE_TIME)
                            		|| fastDateFormat.equals(FastDateFormat.UTC_DATE_TIME)) simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            return simpleDateFormat;
                        }
                    };
                    dateFormatMap.put(fastDateFormat, tl);
                }
            }
		}
		
		return tl.get();
	}

	/**
	 * 获取当前时间的指定格式字符串
	 * @param pattern-如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurTimeStr(FastDateFormat dateFormat) {
		return getDateFormat(dateFormat).format(new Date());
	}

	/**
	 * 获取当前时间的默认字符串形式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDefaultCurTimeStr() {
		return getDateFormat().format(new Date());
	}
	
	/**
	 * 格式化时间
	 * @param createTime
	 * @param dateFormat
	 * @return
	 */
	public static String format(Date createTime, FastDateFormat dateFormat) {
		DateTime dt = new DateTime(createTime);
		return dt.toString(dateFormat.value);
	}
	
	/**
	 * 返回指定时间格式的毫秒数
	 * @param pattern - FastDateFormat
	 * @return
	 */
	public static long getTime(String timeStr, FastDateFormat dateFormat) {
		try {
			Date date = getDateFormat(dateFormat).parse(timeStr);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 返回指定时间格式的日期Date
	 * @param timeStr
	 * @param pattern FastDateFormat
	 * @return
	 */
	public static Date getDate(String timeStr, FastDateFormat dateFormat) {
		try {
			return getDateFormat(dateFormat).parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取某个时间加天数或者减天数的日期
	 * @param date
	 * @param days 加天数，次为正数，减天数为负数
	 * @return
	 */
	public static Date getDateAfter(Date date, int days) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}

	/**
	 * 根据 date 对象格式化时间，返回字符串
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return getDateFormat(FastDateFormat.DATE_TIME).format(date);
	}
	
	/**
	 * 根据 毫秒数 getTime 格式化时间，返回字符串
	 * @param time
	 * @return
	 */
	public static String formatDate(Long time){
		Date formatDate = new Date(time);
		return getDateFormat(FastDateFormat.DATE_TIME).format(formatDate);
	}

	/**
	 * 根据 timestamp 对象格式化时间，返回字符串
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(Timestamp timestamp){
		return formatDate(timestamp.getTime());
	}
	
	/**
	 * 获取一个当前的时间的 timestamp
	 * @return
	 */
	public static Timestamp getTimeStamp(){
		return new Timestamp(new Date().getTime());
	}
	
	/**
	 * 获取今天，格式为yyyy-HH-dd
	 * @return
	 * @throws ParseException 
	 */
	public static Date getToday() throws ParseException{
		Date date = new Date();		
		String strToDay = getDateFormat(FastDateFormat.DATE).format(date) + " 00:00:00";		//格式为 2017-07-24 00:00:00		
		return getDateFormat().parse(strToDay);
	}
	
	public static long toMinutes(long diffTime){
		return diffTime / (1000 * 60);
	}
	
	public static double toPreciseMinutes(long diffTime){
		return diffTime / (1000 * 60.0);
	}
	
	public static long toHours(long diffTime){
		return diffTime / (1000 * 60 * 60);
	}
	
	public static double toPreciseHours(long diffTime){
		return diffTime / (1000 * 60 * 60.0);
	}
	
	/**
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int compareTo(Date end, Date begin){
		
		if(begin == null && end == null) return 0;
		
		else if(end == null) return -1;

		else if(begin == null) return 1;
		
		else return end.compareTo(begin);
	}
	
	public static long getDiffHours(Date begin, Date end){
		return toHours(getDiff(begin, end));
	}
	
	public static double getPreciseDiffHours(Date begin, Date end){
		return toPreciseHours(getDiff(begin, end));
	}
	
	public static long getDiffMinutes(Date begin, Date end){
		return toMinutes(getDiff(begin, end));
	}
	
	public static double getPreciseDiffMinutes(Date begin, Date end){
		return toPreciseMinutes(getDiff(begin, end));
	}
	
	public static long getDiff(Date begin, Date end){
		
		if(begin == null || end == null) return 0;
		
		long begin_time = begin.getTime();
		long end_time = end.getTime();
		
		return end_time - begin_time;
	}
	
	/*
	 * 毫秒转化时分秒毫秒
	 */
	public static String formatMillisecond(Long ms) {
		
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		if (milliSecond > 0) {
			sb.append(milliSecond + "毫秒");
		}
		return sb.toString();
	}
	
}