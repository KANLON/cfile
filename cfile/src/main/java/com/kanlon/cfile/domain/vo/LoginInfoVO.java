package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

/**
 * 登陆时的信息
 *
 * @author zhangcanlong
 * @date 2018年11月29日
 */
public class LoginInfoVO implements Serializable {

	/**
	 * 用户名或邮箱
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 登陆验证码
	 */
	private String captcha;

}
