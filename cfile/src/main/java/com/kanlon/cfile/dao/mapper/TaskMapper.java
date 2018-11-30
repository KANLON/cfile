package com.kanlon.cfile.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kanlon.cfile.domain.po.TaskPO;

/**
 * 任务实体类的mapper(参考自：http://www.ityouknow.com/springboot/2016/11/06/spring-boo-mybatis.html)
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public interface TaskMapper {
	@Select("SELECT * FROM task")
	@Results({ @Result(property = "taskName", column = "task_name"),
			@Result(property = "fileType", column = "file_type"),
			@Result(property = "submitNum", column = "submit_num"),
			@Result(property = "submitingNum", column = "submiting_num"),
			@Result(property = "submitingList", column = "submiting_list") })
	List<TaskPO> getAll();

	@Select("SELECT count(*) FROM task")
	Integer getAllNum();

	/**
	 * 根据主键id查询出任务信息
	 *
	 * @param id
	 * @return
	 */
	@Select("SELECT * FROM task WHERE tid = #{tid}")
	@Results({ @Result(property = "taskName", column = "task_name"),
			@Result(property = "fileType", column = "file_type"),
			@Result(property = "submitNum", column = "submit_num"),
			@Result(property = "submitingNum", column = "submiting_num"),
			@Result(property = "submitingList", column = "submiting_list") })
	TaskPO getOne(Integer id);

	/**
	 * 根据用户id和任务名查询出任务信息
	 *
	 * @param uid
	 * @param taskName
	 * @return
	 */
	@Select("Select count(*)  from task where uid=#{uid} and task_name=#{taskName}")
	Integer selectTaskNameByUid(Integer uid, String taskName);

	/**
	 * 将一个新的任务插入到数据库中去
	 *
	 * @param taskPO
	 */
	@Insert("INSERT INTO task(uid,task_name,dendline,file_type,submit_num,submiting_num,remark,authentication,submiting_list,ctime,mtime,dr) VALUES(#{uid},#{taskName},#{dendline},#{file_type},#{submit_num},#{submiting_num},#{remark},#{authentication},#{submiting_list},#{ctime},#{mtime},#{dr})")
	void insertOne(TaskPO taskPO);

	/**
	 * 根据一个新的任务信息中的id，更新该任务所有信息
	 *
	 * @param taskPO
	 */
	@Update("UPDATE TASK SET uid=#{uid},task_name=#{taskName},dendline=#{dendline},file_type=#{fileType},submit_num=#{submitNum},remark=#{remark},authentication=#{authentication},submiting_list=#{submitingList},ctime=#{ctime},mtime=#{mtime},dr=#{dr} WHERE tid =#{tid}")
	void updateByKey(TaskPO taskPO);

	/**
	 * 根据 任务id在原来已经提交任务人数的基础上+1
	 *
	 * @param tid
	 *            任务id
	 * @return
	 */
	@Update("UPDATE TASK SET SUBMITING_NUM=SUBMITING_NUM+1 WHERE TID=#{TID}")
	int updateSubmitingNumByTid(Integer tid);

	/**
	 * 根据任务id删除某条任务
	 *
	 * @param id
	 */
	@Delete("DELETE FROM task WHERE tid =#{tid}")
	void deleteByKey(Integer id);

}
