package com.kanlon.cfile.utli;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间的工具类
 *
 * @author zhangcanlong
 * @date 2018年11月12日
 */
public class TimeUtil {

	private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

	/**
	 * 根据当前时间得到例如20181112190311的时间格式
	 *
	 * @param millis
	 *            时间的毫秒值
	 * @return 返回yyyyMMddHHmmss之类的时间字符串格式
	 */
	public static String getLocalDateTimeByDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = format.format(date);
		return dateStr;
	}

	/**
	 * 根据当前时间得到例如2018-11-12 19:03:11的时间格式
	 *
	 * @param millis
	 *            时间毫秒值
	 * @return 返回yyyy-MM-dd HH:mm:ss之类的时间格式字符串
	 */
	public static String getSimpleDateTimeByDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = format.format(date);
		return dateStr;
	}

	/**
	 * 将本地时间（如：20181112190311）解析为时间类
	 *
	 * @param LocalDateStr
	 * @return
	 */
	public static Date getDateByLocalDateStr(String LocalDateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = format.parse(LocalDateStr);
		} catch (ParseException e) {
			logger.error("解析时间出现错误！", e);
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateBySimpleDateStr(String simpleDateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(simpleDateStr);
		} catch (ParseException e) {
			logger.error("解析时间出现错误！", e);
			e.printStackTrace();
		}
		return date;
	}

}