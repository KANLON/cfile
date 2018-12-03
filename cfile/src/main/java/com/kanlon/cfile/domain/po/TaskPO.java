package com.kanlon.cfile.domain.po;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 数据库中对应任务表的持久层任务的实体类
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@Data
public class TaskPO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务id
	 */
	private Integer tid;

	/**
	 * 该任务对应的用户id
	 */
	private Integer uid;
	/**
	 * 任务名称
	 */
	@NotNull
	private String taskName;
	/**
	 * 限定提交截止时间
	 */
	private Date dendline;
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
	/**
	 * 删除标志
	 */
	private Integer dr = 0;
}
