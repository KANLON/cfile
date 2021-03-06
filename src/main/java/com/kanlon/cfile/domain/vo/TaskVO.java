package com.kanlon.cfile.domain.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 创建任务时，提交的信息的实体类展示类
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@Data
public class TaskVO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务名称（非空）
	 */
	@NotNull
	private String taskName;
	/**
	 * 任务提交截止时间字符串（以2018-11-02 11:12:11）这种形式
	 */
	private String dendlineStr;
	/**
	 * 任务提交的文件的类型
	 */
	private String fileType;
	/**
	 * 预计提交的人数
	 */
	private Integer submitNum;

	/**
	 * 备注
	 */
	private String remark;
}
