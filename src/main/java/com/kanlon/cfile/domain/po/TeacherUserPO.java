package com.kanlon.cfile.domain.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师或班委用户对应的数据库的持久层实体类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class TeacherUserPO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2673741785965677663L;
	/**
	 * 自增主键id
	 */
	private int uid;
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
	private int dr;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	@Override
	public String toString() {
		return "TeacherUserPO [uid=" + uid + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", nickname=" + nickname + ", email=" + email + ", ctime=" + ctime + ", mtime=" + mtime + ", dr=" + dr
				+ "]";
	}

}
