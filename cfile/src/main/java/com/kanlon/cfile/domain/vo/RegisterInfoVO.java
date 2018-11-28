package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

/**
 * 注册信息的展示层实体类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class RegisterInfoVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1627675005037620143L;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 图片验证码
	 */
	private String imgCaptcha = "";

	/**
	 * 邮箱
	 */
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImgCaptcha() {
		return imgCaptcha;
	}

	public void setImgCaptcha(String imgCaptcha) {
		this.imgCaptcha = imgCaptcha;
	}

}
