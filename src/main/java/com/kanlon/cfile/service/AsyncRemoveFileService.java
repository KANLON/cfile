package com.kanlon.cfile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 异步删除文件
 *
 * @author zhangcanlong
 * @since 2022/10/13 12:13
 **/
@Slf4j
@Service
public class AsyncRemoveFileService {


    /**
     * 删除一个文件
     *
     * @param fileName 文件名称
     */
    @Async
    public void removeOneFile(String fileName) {
        // 删除压缩成子文件的重复提交文件
        File childFilesZip = new File(fileName);
        if (childFilesZip.exists()) {
            if (!childFilesZip.delete()) {
                log.error("删除文件失败！要删除文件的路径为：{}", childFilesZip.getAbsoluteFile());
            }
        }
    }

}
