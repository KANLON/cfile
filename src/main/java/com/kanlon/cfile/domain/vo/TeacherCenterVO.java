package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 教师或班委 个人中心的信息
 *
 * @author zhangcanlong
 * @date 2018年12月27日
 */
@Data
public class TeacherCenterVO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 是否经过验证
	 */
	private Integer authentication;

}
