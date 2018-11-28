package com.kanlon.cfile.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kanlon.cfile.utli.Constant;

/**
 * 常量测试
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConstantTest {
	@Test
	public void pathTest() {
		System.out.println(Constant.WEB_ROOT);
	}

}
