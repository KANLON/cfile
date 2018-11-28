package com.kanlon.cfile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试的第一个controller
 *
 * @author zhangcanlong
 * @date 2018年11月27日
 */
@RestController
public class TestController {
	/**
	 * 测试打印出hello world
	 * 
	 * @return hello world
	 */
	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";

	}

}
