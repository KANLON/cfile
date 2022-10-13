package com.kanlon.cfile.utli;

import java.io.File;
import java.io.IOException;

/**
 * 文件的工具类
 *
 * @author zhangcanlong
 * @since 2022/10/13 23:08
 **/
public class FileUtil {

    private FileUtil() {}

    /**
     * 是否为父目录的下的子文件或目录
     *
     * @param parent 父目录文件路径
     * @param child  子目录文件路径
     * @return boolean
     * @throws IOException ioException
     */
    public static boolean isSubFile(String parent, String child) throws IOException {
        return isSubFile(new File(parent), new File(child));
    }

    /**
     * 是否为父目录的下的子文件或目录
     *
     * @param parent 父目录文件
     * @param child  子目录文件
     * @return boolean
     * @throws IOException ioException
     */
    public static boolean isSubFile(File parent, File child) throws IOException {
        return child.getCanonicalPath().startsWith(parent.getCanonicalPath() + File.separator);
    }


}
