package com.kanlon.cfile.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kanlon.cfile.DemoApplication;
import com.kanlon.cfile.utli.RandomUtil;

/**
 * 随机数类的单元测试类
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class RandomUtilTest {

	@Test
	public void testSalt() {
		System.out.println(RandomUtil.createSalt());
		System.out.println(RandomUtil.createSalt().length());
	}
}
