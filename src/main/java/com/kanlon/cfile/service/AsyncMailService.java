package com.kanlon.cfile.service;

import com.kanlon.cfile.utli.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 异步发送邮件的service
 *
 * @author zhangcanlong
 * @since 2022/10/8 21:00
 **/
@Service
public class AsyncMailService {


    @Autowired
    private MailUtil mailUtil;

    /**
     * 异步发送邮件
     *
     * @param fileStorePath 文件存储的地址
     * @param fileNewName 新的文件名
     * @param clientIP 上传者的ip
     **/
    @Async
    public void sentEmail(File fileStorePath,String fileNewName,String clientIP) {
        // 发送邮件，进行备份
        String logStr = "文件上传到" + fileStorePath + "/" + fileNewName + "了！" + "发送者IP地址为：" + clientIP;
        mailUtil.sendAttachmentsMail("s19961234@126.com", "备份-" + fileNewName, logStr, fileStorePath + "/" + fileNewName);
    }
}
