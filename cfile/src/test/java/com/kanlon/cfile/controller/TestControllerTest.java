package com.kanlon.cfile.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kanlon.cfile.controller.TestController;

/**
 * 测试spring boot的例子的单元测试
 *
 * @author zhangcanlong
 * @date 2018年11月27日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestControllerTest {
	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();

	}

	@Test
	public void getHello() throws Exception {
		RequestBuilder bulider = MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON);
		mvc.perform(bulider).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
