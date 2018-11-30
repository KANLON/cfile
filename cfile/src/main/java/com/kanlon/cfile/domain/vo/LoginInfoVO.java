package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 登陆时的信息
 *
 * @author zhangcanlong
 * @date 2018年11月29日
 */
public class LoginInfoVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8948348858012820487L;
	/**
	 * 用户名或邮箱
	 */
	@Getter
	@Setter
	private String username;
	/**
	 * 密码
	 */
	@Getter
	@Setter
	private String password;
	/**
	 * 登陆验证码
	 */
	@Getter
	@Setter
	private String captcha;

}
