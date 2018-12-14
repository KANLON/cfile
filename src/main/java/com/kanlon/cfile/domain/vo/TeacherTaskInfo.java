package com.kanlon.cfile.domain.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 教师或班委可以查看到的某个任务的信息
 *
 * @author zhangcanlong
 * @date 2018年12月7日
 */
@Data
public class TeacherTaskInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private Integer tid;

	/**
	 * 任务名称
	 */
	@NotNull
	private String taskName;
	/**
	 * 限定提交截止时间字符串（如：2018-11-11 10:22:12这种形式）
	 */
	private String dendlineStr;
	/**
	 * 限定提交文件类型，对应FileTypeConstant类中的常量
	 */
	private String fileType;
	/**
	 * 限定应该提交的人数（默认为10000人）
	 */
	private Integer submitNum;
	/**
	 * 目前已经提交的人数（默认为0）
	 */
	private Integer submitingNum;

	/**
	 * 本次任务的备注
	 */
	private String remark;

	/**
	 * 是否经过验证
	 */
	private Integer authentication;
	/**
	 * 已经提交的名单，默认以学号，逗号分隔，例如：151612220,151612221
	 */
	private String submitingList;
	/**
	 * 创建的时间
	 */
	private Date ctime = new Date();
	/**
	 * 修改时间
	 */
	private Date mtime = new Date();

}
