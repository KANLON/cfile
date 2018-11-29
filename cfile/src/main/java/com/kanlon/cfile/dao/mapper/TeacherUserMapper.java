package com.kanlon.cfile.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

	@Insert("INSERT INTO teacher(username,password,salt,nickname,email,ctime) VALUES(#{username}, #{password}, #{salt},#{nickname},#{email},#{ctime})")
	void insert(TeacherUserPO user);

	@Update("UPDATE teacher SET username=#{username},password=#{password},salt=#{salt},nickname=#{nickname},email=#{email},ctime=#{ctime} WHERE uid =#{uid}")
	void update(TeacherUserPO user);

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
	 * @param username
	 * @return
	 */
	@Select("select count(*) from teacher where email=#{email}")
	Integer selectTeacherByEmail(String email);
}
