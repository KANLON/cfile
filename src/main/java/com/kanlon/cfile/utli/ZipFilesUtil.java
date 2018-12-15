package com.kanlon.cfile.utli;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将指定文件夹下的所有文件压缩 参考资料：https://blog.csdn.net/u010366748/article/details/78615004
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
public class ZipFilesUtil {

	private static Logger logger = LoggerFactory.getLogger(ZipFilesUtil.class);

	/**
	 * 压缩指定目录中所有文件
	 *
	 * @param sourceFilePath
	 *            要压缩的目录
	 * @param baseDir
	 *            在压缩目前之上新建一个文件夹，递归方法需要该函数，一般填写""就可以了
	 */
	public static File compress(File sourceFilePath, String baseDir) {
		if (!sourceFilePath.exists()) {
			logger.info("待压缩的文件目录或文件" + sourceFilePath.getName() + "不存在");
			return null;
		}

		File[] fs = sourceFilePath.listFiles();
		BufferedInputStream bis = null;
		ZipOutputStream zos = null;
		byte[] bufs = new byte[1024 * 10];
		FileInputStream fis = null;
		File zipedFile = new File(sourceFilePath.getAbsolutePath() + ".zip");
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipedFile));
			for (int i = 0; i < fs.length; i++) {
				String fName = fs[i].getName();
				logger.info("压缩：" + baseDir + fName);
				if (fs[i].isFile()) {
					ZipEntry zipEntry = new ZipEntry(baseDir + fName);
					zos.putNextEntry(zipEntry);
					// 读取待压缩的文件并写进压缩包里
					fis = new FileInputStream(fs[i]);
					bis = new BufferedInputStream(fis, 1024 * 10);
					int read = 0;
					while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
						zos.write(bufs, 0, read);
					}
					// 如果需要删除源文件，则需要执行下面2句
					// fis.close();
					// fs[i].delete();
				} else if (fs[i].isDirectory()) {
					// 如果需要对子文件夹进行压缩，再执行这条
					compress(fs[i], baseDir + fName + "/");
				}
			}
		} catch (IOException e) {
			logger.error("压缩文件异常！", e);
			throw new RuntimeException("压缩文件时发生异常" + e.getMessage());
		} finally {
			// 关闭流
			try {
				if (null != bis) {
					bis.close();
				}
				if (null != zos) {
					zos.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return zipedFile;

	}

	public static void main(String[] args) throws ParseException {
		String sourceFilePath = "D:\\程序猿\\bootstrap-3.3.7\\less";
		compress(new File(sourceFilePath), "");
	}

}
