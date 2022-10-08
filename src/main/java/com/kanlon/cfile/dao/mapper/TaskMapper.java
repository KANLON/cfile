package com.kanlon.cfile.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
	/**
	 * 获取所有发布的任务
	 * @param uid 用户id
	 * @return 任务信息列表集合
	 **/
	@Select("SELECT * FROM task  where uid=#{uid} order by mtime desc")
	List<TaskPO> getAll(@Param(value = "uid") Integer uid);


	/**
	 * 得到所有任务的数量
	 * @param uid 用户id
	 * @return 任务总数
	 **/
	@Select("SELECT count(*) FROM task where uid=#{uid}")
	Integer getAllNum(@Param(value = "uid") Integer uid);

	/**
	 * 根据主键id查询出任务信息
	 *
	 * @param tid 用户id
	 * @return 任务信息类
	 */
	@Select("SELECT * FROM task WHERE tid = #{tid}")
	TaskPO getOne(@Param(value = "tid") Integer tid);

	/**
	 * 根据用户id和任务名查询出任务信息
	 *
	 * @param uid 用户id主键
	 * @param taskName 任务名
	 * @return
	 */
	@Select("SELECT COUNT(*) NUM FROM task WHERE UID=#{uid} AND TASK_NAME=#{taskName}")
	Integer selectTaskNameByUid(@Param(value = "uid") Integer uid, @Param(value = "taskName") String taskName);

	/**
	 * 将一个新的任务插入到数据库中去
	 * @param taskPO 任务信息
	 * @return 1
	 */
	@InsertProvider(type = TaskProvider.class, method = "insertTaskOneSql")
	@Options(useGeneratedKeys = true, keyProperty = "tid")
	Integer insertOne(TaskPO taskPO);

	/**
	 * 根据一个新的任务信息中的id，更新该任务所有信息
	 * @param taskPO 新的任务信息
	 * @return 1
	 */
	@UpdateProvider(type = TaskProvider.class, method = "updateTaskOneByKeySql")
	Integer updateByKey(TaskPO taskPO);

	/**
	 * 根据 任务id在原来已经提交任务人数的基础上+1
	 *
	 * @param tid
	 *            任务id
	 * @return 1 表示1行
	 */
	@Update("UPDATE task SET SUBMITING_NUM=SUBMITING_NUM+1 WHERE TID=#{TID}")
	Integer updateSubmitingNumByTid(Integer tid);

	/**
	 * 根据任务id删除某条任务
	 *
	 * @param id 任务id主键
	 * @return 1
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
