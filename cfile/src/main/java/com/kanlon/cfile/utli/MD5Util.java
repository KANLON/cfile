package com.kanlon.cfile.utli;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class MD5Util {

	/**
	 * 传入原始密码，返回加密之后的密码字符串
	 *
	 * @param password
	 *            原始密码
	 * @return String 加密后的字符串
	 */
	public static String md5(String password) {
		try {
			// 1)创建加密类对象
			MessageDigest md = MessageDigest.getInstance("md5");
			// 2）进行加密
			byte[] byteArray = md.digest(password.getBytes());

			StringBuilder sb = new StringBuilder();
			for (byte b : byteArray) {
				sb.append(numToHex(b));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 利用盐按照一定规则加密密码
	 *
	 * @return 加密后的密码
	 */
	public static String encryptPwd(String password, String salt) {
		return md5(password + salt);
	}

	/**
	 * 传入一个10进制的字节数值，返回2位的十六进制的字符串
	 *
	 * @param num
	 *
	 * @return String
	 */
	private static String numToHex(byte num) {
		/*
		 * byte类型： 无符号位 ： 取值范围： 0 ~ 255 有符号为： 取值范围： -128 ~ 127
		 */
		int targetNum = 0;
		// 如果是负数，则先转为正数，再进行计算；否则，直接使用
		if (num < 0) {
			targetNum = 256 + num;
		} else {
			targetNum = num;
		}
		// 第一位字符的值
		int first = targetNum / 16;
		// 第二位字符的值
		int sencond = targetNum % 16;

		return strArray[first] + strArray[sencond];
	}

	private static String[] strArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };
}