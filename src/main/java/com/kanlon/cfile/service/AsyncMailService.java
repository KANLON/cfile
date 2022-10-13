package com.kanlon.cfile.service;

import com.kanlon.cfile.configure.ProjectConfigProperty;
import com.kanlon.cfile.utli.MailUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * 异步发送邮件的service
 *
 * @author zhangcanlong
 * @since 2022/10/8 21:00
 **/
@Service
public class AsyncMailService {


    @Resource
    private MailUtil mailUtil;
    @Resource
    private ProjectConfigProperty projectConfigProperty;

    /**
     * 异步发送邮件
     *
     * @param fileStorePath 文件存储的地址
     * @param fileNewName   新的文件名
     * @param clientIp      上传者的ip
     **/
    @Async
    public void sentEmail(File fileStorePath, String fileNewName, String clientIp) {
        // 发送邮件，进行备份
        String logStr = "文件上传到" + fileStorePath + "/" + fileNewName + "了！" + "发送者IP地址为：" + clientIp;
        mailUtil.sendAttachmentsMail(projectConfigProperty.getDefaultFileEmailRec(), "备份-" + fileNewName, logStr, fileStorePath + "/" + fileNewName);
    }
}
