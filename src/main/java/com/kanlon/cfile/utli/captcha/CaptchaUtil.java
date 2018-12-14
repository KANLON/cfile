package com.kanlon.cfile.utli.captcha;

/**
 * 验证码工具类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class CaptchaUtil {

	/**
	 * 创建新的验证码实例
	 *
	 * @return 验证码信息
	 */
	public static Captcha create() {
		return new Captcha();
	}
}