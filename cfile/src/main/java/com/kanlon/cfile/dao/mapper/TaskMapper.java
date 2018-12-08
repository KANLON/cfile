package com.kanlon.cfile.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.kanlon.cfile.domain.po.TaskPO;

/**
 * 任务实体类的mapper(参考自：http://www.ityouknow.com/springboot/2016/11/06/spring-boo-mybatis.html)
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public interface TaskMapper {
	@Select("SELECT * FROM task order by mtime desc")
	@Options(useCache = true, timeout = 10000)
	@Results({ @Result(property = "taskName", column = "task_name"),
			@Result(property = "fileType", column = "file_type"),
			@Result(property = "submitNum", column = "submit_num"),
			@Result(property = "submitingNum", column = "submiting_num"),
			@Result(property = "submitingList", column = "submiting_list") })
	List<TaskPO> getAll() throws Exception;

	@Select("SELECT count(*) FROM task")
	Integer getAllNum();

	/**
	 * 根据主键id查询出任务信息
	 *
	 * @param id
	 * @return
	 */
	@Options(useCache = true, timeout = 10000)
	@Select("SELECT * FROM task WHERE tid = #{tid}")
	@Results({ @Result(property = "taskName", column = "task_name"),
			@Result(property = "fileType", column = "file_type"),
			@Result(property = "submitNum", column = "submit_num"),
			@Result(property = "submitingNum", column = "submiting_num"),
			@Result(property = "submitingList", column = "submiting_list") })
	TaskPO getOne(@Param(value = "tid") Integer tid);

	/**
	 * 根据用户id和任务名查询出任务信息
	 *
	 * @param uid
	 * @param taskName
	 * @return
	 */
	@Select("SELECT COUNT(*) NUM FROM TASK WHERE UID=#{uid} AND TASK_NAME=#{taskName}")
	@ResultType(value = Integer.class)
	Integer selectTaskNameByUid(@Param(value = "uid") Integer uid, @Param(value = "taskName") String taskName);

	/**
	 * 将一个新的任务插入到数据库中去
	 *
	 * @param taskPO
	 */

	@InsertProvider(type = TaskProvider.class, method = "insertTaskOneSql")
	// 将插入后的id传回到实体类中
	@Options(useGeneratedKeys = true, keyProperty = "tid")
	Integer insertOne(TaskPO taskPO);

	/**
	 * 根据一个新的任务信息中的id，更新该任务所有信息
	 *
	 * @param taskPO
	 */
	@UpdateProvider(type = TaskProvider.class, method = "updateTaskOneByKeySql")
	Integer updateByKey(TaskPO taskPO);

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
	Integer deleteByKey(Integer id);

	/**
	 * 任务的动态sql提供类
	 *
	 * @author zhangcanlong
	 * @date 2018年12月2日
	 */
	class TaskProvider {
		/**
		 * 插入一个任务的动态sql
		 *
		 * @param task
		 * @return
		 */
		public String insertTaskOneSql(TaskPO task) {
			return new SQL() {
				{
					INSERT_INTO("task");
					VALUES("uid", "#{uid}");
					if (task.getTaskName() != null) {
						VALUES("task_name", "#{taskName}");
					}
					if (task.getDendline() != null) {
						VALUES("dendline", "#{dendline}");
					}
					if (task.getFileType() != null) {
						VALUES("file_type", "#{fileType}");
					}
					if (task.getSubmitNum() != null) {
						VALUES("submit_num", "#{submitNum}");
					}
					if (task.getSubmitingNum() != null) {
						VALUES("submiting_num", "#{submitingNum}");
					}
					if (task.getRemark() != null) {
						VALUES("remark", "#{remark}");
					}
					if (task.getAuthentication() != null) {
						VALUES("authentication", "#{authentication}");
					}
					if (task.getSubmitingList() != null) {
						VALUES("submiting_list", "#{submitingList}");
					}
					if (task.getCtime() != null) {
						VALUES("ctime", "#{ctime}");
					}
					if (task.getMtime() != null) {
						VALUES("mtime", "#{mtime}");
					}
					if (task.getDr() != null) {
						VALUES("dr", "#{dr}");
					}
				}
			}.toString();
		}

		/**
		 *
		 * 根据主键tid更新任务方法的动态sql
		 *
		 * @param task
		 * @return
		 */
		public String updateTaskOneByKeySql(TaskPO task) {
			return new SQL() {
				{
					UPDATE("task");
					if (task.getTaskName() != null) {
						SET("task_name=#{taskName}");
					}
					if (task.getDendline() != null) {
						SET("dendline=#{dendline}");
					}
					if (task.getFileType() != null) {
						SET("file_type=#{fileType}");
					}
					if (task.getSubmitNum() != null) {
						SET("submit_num=#{submitNum}");
					}
					if (task.getSubmitingNum() != null) {
						SET("submiting_num=#{submitingNum}");
					}
					if (task.getRemark() != null) {
						SET("remark=#{remark}");
					}
					if (task.getAuthentication() != null) {
						SET("authentication=#{authentication}");
					}
					if (task.getSubmitingList() != null) {
						SET("submiting_list=#{submitingList}");
					}
					if (task.getCtime() != null) {
						SET("ctime=#{ctime}");
					}
					if (task.getMtime() != null) {
						SET("mtime=#{mtime}");
					}
					if (task.getDr() != null) {
						SET("dr=#{dr}");
					}
					WHERE("tid = #{tid}");
				}
			}.toString();
		}

	}

}
