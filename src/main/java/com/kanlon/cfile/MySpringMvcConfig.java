package com.kanlon.cfile;

import com.kanlon.cfile.filter.AuthenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring MVC的配置
 *
 * @author zhangcanlong
 * @date 2022/10/13
 */
@SpringBootConfiguration
public class MySpringMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenFilter authenFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenFilter).addPathPatterns("/**");
    }

}
