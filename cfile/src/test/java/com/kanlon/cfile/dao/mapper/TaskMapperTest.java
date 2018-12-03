package com.kanlon.cfile.dao.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kanlon.cfile.DemoApplication;
import com.kanlon.cfile.domain.po.TaskPO;

/**
 * 单元测试taskmapper类
 *
 * @author zhangcanlong
 * @date 2018年12月1日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@EnableAutoConfiguration
public class TaskMapperTest {

	@Autowired
	private TaskMapper taskMapper;

	@Test
	public void testAllMethod() throws Exception {
		System.out.println("执行了查询操作===========================================");
		System.out.println(taskMapper.getAll());
		System.out.println(taskMapper.getAllNum());
		System.out.println(taskMapper.getOne(1));
		System.out.println(taskMapper.selectTaskNameByUid(1, "测试任务"));
		// System.out.println(taskMapper.getAll());
		// System.out.println(taskMapper.getAll());
		// System.out.println(taskMapper.getAll());
	}

	@Test
	public void testInsert() {
		TaskPO po = new TaskPO();
		po.setAuthentication(1);
		po.setCtime(new Date());
		po.setTaskName("测试任务4");
		po.setUid(1);
		System.out.println("执行了插入一个任务");
		System.out.println(taskMapper.insertOne(po));
		System.out.println("插入的任务的主键是：" + po.getTid());
	}

	@Test
	public void testUpdate() {
		TaskPO po = new TaskPO();
		po.setAuthentication(1);
		po.setCtime(new Date());
		po.setTaskName("测试任务4");
		po.setUid(1);
		po.setTid(2);
		System.out.println("执行了更新一个任务");
		System.out.println(taskMapper.updateByKey(po));
	}

	@Test
	public void testOther() {
		System.out.println(taskMapper.updateSubmitingNumByTid(2));
		System.out.println(taskMapper.deleteByKey(2));
		System.out.println(taskMapper.deleteByKey(2));
		System.out.println(taskMapper.deleteByKey(3));
		System.out.println(taskMapper.deleteByKey(4));
	}

}
