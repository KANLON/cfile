package com.kanlon.cfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kanlon.cfile.filter.AuthenFilter;

@SpringBootConfiguration
public class MySpringmvcConfig implements WebMvcConfigurer {
	@Autowired
	private AuthenFilter authenFilter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenFilter).addPathPatterns("/**");
	}

}
