package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 * 学生提交文件的展示层vo对象
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class StudentSubmitFileVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4964984763012815442L;

	/**
	 * 学号
	 */
	private String studentId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 文件
	 */
	private MultipartFile file;
	/**
	 * 备注
	 */
	private String remark;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
