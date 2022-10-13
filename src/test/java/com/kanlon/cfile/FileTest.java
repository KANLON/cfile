package com.kanlon.cfile;

import com.kanlon.cfile.utli.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件操作相关的单元测试
 *
 * @author zhangcanlong
 * @since 2022/10/13 22:39
 **/
public class FileTest {

    /**
     * 打印测试
     */
    @Test
    public void testPrint() {
        System.out.println("FileTest");
    }

    /**
     * 测试创建文件
     */
    @Test
    public void testCreatFileWithStr() {
        String parentPathDir = "/data/cfile/upload";
        String fileName1 = "../testFile.txt";
        String fileName2 = "testFile2.txt";
        File cFile1 = new File(parentPathDir, fileName1);
        File cFile2 = new File(parentPathDir, fileName2);
        File cFile1Parent = cFile1.getParentFile();
        File cFile2Parent = cFile2.getParentFile();
        if (!cFile1Parent.exists()) {
            System.out.println("cFile1 的父目录创建结果：" + cFile1Parent.mkdirs() + ",cFile1的父目录绝对路径为：" + cFile1Parent.getAbsolutePath());
        }
        if (!cFile2Parent.exists()) {
            System.out.println("cFile2 的父目录创建结果：" + cFile2Parent.mkdirs() + ",cFile2的父目录绝对路径为：" + cFile2Parent.getAbsolutePath());
        }
        // 创建cFile1 和 cFile2
        try {
            System.out.println("cFile1创建结果：" + cFile1.createNewFile());
            System.out.println("cFile2创建结果：" + cFile2.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试路径 拼接
     */
    @Test
    public void testPath() {
        Path path1 = Paths.get("/data/cfile/upload", "../testFile.txt");
        Path path2 = Paths.get("/data/cfile/upload", "testFile2.txt");
        System.out.println(path1);
        System.out.println(path2);
    }

    /**
     * 测试子文件
     */
    @Test
    public void testIsSubFile() {
        String parentPathDir = "/data/cfile/upload";
        String fileName1 = "../testFile.txt";
        String fileName2 = "/重复的文件/testFile2.txt";
        Path path1 = Paths.get(parentPathDir, fileName1);
        Path path2 = Paths.get(parentPathDir, fileName2);
        try {
            System.out.println(FileUtil.isSubFile(parentPathDir, path1.toString()));
            System.out.println(FileUtil.isSubFile(parentPathDir, path2.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
