package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 教师或班委查看所有任务时的任务列表实体类
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@Data
public class TaskInfoListsVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务id
	 */
	private Integer tid;
	/**
	 * 应该提交的人数
	 */
	private Integer submitNum;
	/**
	 * 实际提交的人数
	 */
	private Integer submitingNum;
	/**
	 * 认证状态(0为未认证，1为已认证，2为认证中)
	 */
	private Integer authencation;
	/**
	 * 截止时间字符串(2018-11-11 11:12:12)这种形式
	 */
	private String dendlineStr;

	/**
	 * 限定提交的文件类型
	 */
	private String fileType;
	/**
	 * 备注信息
	 */
	private String remark;
}
