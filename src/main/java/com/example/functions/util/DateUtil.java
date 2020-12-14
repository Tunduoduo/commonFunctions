package com.example.functions.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * date
 * @author TIAN
 */
public class DateUtil {
	public static String TimeStamp2Date(String timestampString){ 
		Long timestamp = Long.parseLong(timestampString)*1000; 
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
		return date; 
	}
	
	public static Date str2Date(String str){
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
		 date = dateformat.parse(str);
		} catch (ParseException e) {
		 e.printStackTrace();
		}
		return date;
	}

	public static Date str2Date(String str, String dmt){
		DateFormat dateformat = new SimpleDateFormat(dmt);
		Date date = null;
		try {
		 date = dateformat.parse(str);
		} catch (ParseException e) {
		 e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date)
	{
		DateFormat format = new SimpleDateFormat("yyyyMMddHH");
		return format.format(date);
	}
	/**
	 *  yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date)
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	public static String formatDate(Date date,String format)
	{
		DateFormat datafFormat = new SimpleDateFormat(format);
		return datafFormat.format(date);
	}
	
	/**
	 *  E dd MMM yyyy hh:mm:ss a
	 * @param time
	 * @return
	 */
	public static String getTime(Long time){
		Date d = new Date(time);
		Format simpleFormat = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss a");
		String date = simpleFormat.format(d);
		return date;
	}
	
	/**
	 *
	 * @param date0
	 * @return
	 */
	public static String getFormulaDate(String date0) {
		String date;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat ddf = new SimpleDateFormat("MM月dd日 HH:mm");
		DateFormat dddf = new SimpleDateFormat("MM-dd HH:mm");
		
		String regex1 = "今天\\s\\d{2}:\\d{2}";
		String regex2 = "\\d{1,2}分钟";
		String regex3 = "\\d{2}月\\d{2}日\\s\\d{2}:\\d{2}";
		String regex4 = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}";
		String regex5 = "\\d{2}-\\d{2}\\s\\d{2}:\\d{2}";
		
		
		Pattern p = Pattern.compile(regex1);
		Matcher m = p.matcher(date0);		
		if (m.find()) {
			date = df.format(new Date()).substring(0, 11) + m.group().substring(3);
			return date;
		} 
		
		p = Pattern.compile(regex2);
		m = p.matcher(date0);
		if (m.find()) {
			Date d = new Date();
			long t = d.getTime()-Integer.parseInt(m.group().split("分钟")[0])*60*1000;			
			date = df.format(new Date(t));
			return date;
		}
		
		p = Pattern.compile(regex3);
		m = p.matcher(date0);
		if (m.find()) {
			try {
				date = df.format(new Date()).substring(0,5) + dddf.format(ddf.parse(m.group()));
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
				return "";
			}
		}
		p = Pattern.compile(regex4);
		m = p.matcher(date0);
		if (m.find()) {
			date = m.group();
			return date;
		}
		
		p=Pattern.compile(regex5);
		m=p.matcher(date0);
		if(m.find()){
			date = df.format(new Date()).substring(0,5)+date0;
			return date;
		}
		return "";
	}
	public static String formatTime(long createTime) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(createTime));

	}
	public static String formatTime(String createTime) {  
	    long msgCreateTime = Long.parseLong(createTime) * 1000L;  
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    return format.format(new Date(msgCreateTime));  
	}

	public static Long formatDateStrToTime(String dateStr) throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(dateStr).getTime();
	}

	public static String formatTime(String createTime,String fromat) {  
	    long msgCreateTime = Long.parseLong(createTime) * 1000L;  
	    DateFormat format = new SimpleDateFormat(fromat);  
	    return format.format(new Date(msgCreateTime));  

	}
	public static boolean getDelayTime(Long delayTime){
		Long nowTime = System.currentTimeMillis();
		long lg = nowTime - delayTime;
		if(lg > 1000*60){
			return true;
		}
		return false;
	}

	public static Date getNextDay() {
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long current = System.currentTimeMillis();// 当前时间毫秒数
		long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(zero));
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
//		Date date = calendar.getTime();
//		return format.format(date);
	}
	public static Date getNowDay() {
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(zero));
		return calendar.getTime();
	}
	public static int long2Int(long lg){
		return (int) (lg%1000000000);
	}
	//日期差
	public static int daysSub(Date fDate, Date oDate) {
	       Calendar aCalendar = Calendar.getInstance();
	       aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return day2 - day1;
	}
	//分钟查
	public static int minuteSub(Date prvDate,Date now){
		long preTime = prvDate.getTime();  
		long nowTime = now.getTime();  
		int minutes = (int) ((nowTime - preTime)/(1000 * 60));  
		return minutes;
	}
	//秒差
	public static int secondSub(Date prvDate,Date now){
		long preTime = prvDate.getTime();
		long nowTime = now.getTime();
		int second = (int) ((nowTime - preTime)/(1000));
		return second;
	}
	/**
	 * 时间转换
	 * x小时前
	 * x分钟前
	 * 刚刚
	 */
	public static Pattern hourPattern=Pattern.compile("(\\d+)小时前");
	public static Pattern minPattern=Pattern.compile("(\\d+)分钟前");
	public static Pattern monDayHourMinPattern=Pattern.compile("(\\d+)月(\\d+)日 (\\d+):(\\d+)");
	public static Pattern yearMonDayPattern=Pattern.compile("(\\d+)年(\\d+)月(\\d+)日");
	public static String convertTimeStamp(String timeStr){
		if(timeStr.equalsIgnoreCase("刚刚")){
			return String.valueOf(System.currentTimeMillis());
		}
		Matcher hourMatcher=hourPattern.matcher(timeStr);
		if(hourMatcher.find()){
			return String.valueOf(System.currentTimeMillis()-Integer.parseInt(hourMatcher.group(1))*60*60*1000);
		}
		Matcher minMatcher=minPattern.matcher(timeStr);
		if(minMatcher.find()){
			return String.valueOf(System.currentTimeMillis()-Integer.parseInt(minMatcher.group(1))*60*1000);
		}
		Matcher monDayHourMinMatcher=monDayHourMinPattern.matcher(timeStr);
		if(monDayHourMinMatcher.find()){
			int year=Integer.parseInt(formatDate(new Date()).split("-")[0]);
			String dateFormat=year+"-"+monDayHourMinMatcher.group(1)+"-"+monDayHourMinMatcher.group(2)
					+" "+monDayHourMinMatcher.group(3)+":"+monDayHourMinMatcher.group(4);
			return dateToLong(dateFormat,"yyyy-MM-dd HH:mm");
		}
		Matcher yearMonDayMatcher=yearMonDayPattern.matcher(timeStr);
		if(yearMonDayMatcher.find()){
			String dateFormat=yearMonDayMatcher.group(1)+"-"+yearMonDayMatcher.group(2)+"-"+yearMonDayMatcher.group(3);
			return dateToLong(dateFormat,"yyyy-MM-dd");
		}
		return timeStr;
	}

	public static String dateToLong(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return String.valueOf(sdf.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(System.currentTimeMillis());
	}
}
