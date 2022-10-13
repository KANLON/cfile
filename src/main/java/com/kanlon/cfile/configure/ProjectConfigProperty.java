package com.kanlon.cfile.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目自身的相关配置
 *
 * @author zhangcanlong
 * @since 2022/10/13 12:24
 **/
@Data
@Component
@ConfigurationProperties("project.config")
public class ProjectConfigProperty {

    /**
     * 默认文件邮件 接收者， 上传文件会发送到邮件
     */
    private String defaultFileEmailRec = "s19961234@126.com";

    /**
     * 上传文件路径
     */
    private String updateFileBasePath = "/opt/cfile/upload/student";
}
