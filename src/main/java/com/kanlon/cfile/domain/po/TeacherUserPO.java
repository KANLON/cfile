package com.kanlon.cfile.domain.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 教师或班委用户对应的数据库的持久层实体类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@Data
public class TeacherUserPO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2673741785965677663L;
	/**
	 * 自增主键id
	 */
	private Integer uid;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 用于加密的盐
	 */
	private String salt;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 创建时间
	 */
	private Date ctime = new Date();
	/**
	 * 修改时间
	 */
	private Date mtime;
	/**
	 * 删除标志
	 */
	private Integer dr;

}
