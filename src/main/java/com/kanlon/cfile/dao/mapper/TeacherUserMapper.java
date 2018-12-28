package com.kanlon.cfile.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.kanlon.cfile.domain.po.TeacherUserPO;

/**
 * 教师或班委实体类的mapper(参考自：http://www.ityouknow.com/springboot/2016/11/06/spring-boo-mybatis.html)
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public interface TeacherUserMapper {
	@Select("SELECT * FROM teacher")
	@Results({ @Result(property = "username", column = "username") })
	List<TeacherUserPO> getAll();

	@Select("SELECT * FROM teacher WHERE uid = #{uid}")
	@Results({ @Result(property = "username", column = "username") })
	TeacherUserPO getOne(Integer id);

	@InsertProvider(type = TeacherUserProvider.class, method = "insertUserOneSql")
	// 将插入后的id传回到实体类中
	@Options(useGeneratedKeys = true, keyProperty = "tid")
	Integer insertUserOne(TeacherUserPO user);

	@UpdateProvider(type = TeacherUserProvider.class, method = "updateUserOneByKeySql")
	void updateUserOneByKey(TeacherUserPO user);

	@Delete("DELETE FROM teacher WHERE uid =#{uid}")
	void delete(Integer id);

	/**
	 * 查询用户名的数量
	 *
	 * @param username
	 * @return
	 */
	@Select("select count(*) from teacher where username=#{username}")
	Integer selectTeacherByUsername(String username);

	/**
	 * 查询某邮箱地址的数量
	 *
	 * @param email
	 * @return
	 */
	@Select("select count(*) from teacher where email=#{email}")
	Integer selectTeacherByEmail(String email);

	/**
	 * 根据用户名或邮箱查询某用户
	 *
	 * @param usernameOrEmail
	 *            用户名或邮箱
	 * @return
	 */
	@Select("SELECT * FROM teacher WHERE username = #{usernameOrEmail} or email=#{usernameOrEmail}")
	@Results
	TeacherUserPO getUserByUsernameOrEmail(String usernameOrEmail);

	/**
	 * 用户的动态sql提供类
	 *
	 * @author zhangcanlong
	 * @date 2018年12月2日
	 */
	class TeacherUserProvider {
		/**
		 * 插入一个用户的动态sql
		 *
		 * @param user
		 * @return
		 */
		public String insertUserOneSql(TeacherUserPO user) {
			return new SQL() {
				{
					INSERT_INTO("teacher");
					if (user.getUsername() != null) {
						VALUES("username", "#{username}");
					}
					if (user.getPassword() != null) {
						VALUES("password", "#{password}");
					}
					if (user.getSalt() != null) {
						VALUES("salt", "#{salt}");
					}
					if (user.getNickname() != null) {
						VALUES("nickname", "#{nickname}");
					}
					if (user.getEmail() != null) {
						VALUES("email", "#{email}");
					}
					if (user.getCtime() != null) {
						VALUES("ctime", "#{ctime}");
					}
					if (user.getDr() != null) {
						VALUES("dr", "#{dr}");
					}
				}
			}.toString();
		}

		/**
		 *
		 * 根据主键tid更新用户方法的动态sql
		 *
		 * @param user
		 * @return
		 */
		public String updateUserOneByKeySql(TeacherUserPO user) {
			return new SQL() {
				{
					UPDATE("teacher");
					if (user.getUsername() != null) {
						SET("username=#{username}");
					}
					if (user.getPassword() != null) {
						SET("password=#{password}");
					}
					if (user.getSalt() != null) {
						SET("salt=#{salt}");
					}
					if (user.getNickname() != null) {
						SET("nickname=#{nickname}");
					}
					if (user.getEmail() != null) {
						SET("email=#{email}");
					}
					if (user.getCtime() != null) {
						SET("ctime=#{ctime}");
					}
					WHERE("uid = #{uid}");
				}
			}.toString();
		}

	}

}
