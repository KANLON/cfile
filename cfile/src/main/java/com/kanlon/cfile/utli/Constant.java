package com.kanlon.cfile.utli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.util.ResourceUtils;

/**
 * 保存常用的常量，例如，响应码，项目路径等
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public final class Constant {
	// 获取根目录，解决异常
	static {
		try {
			// 获取项目根目录并转码
			temp_path = new File(URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "UTF-8")).getPath();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 临时存储根目录
	 */
	private static String temp_path;

	/**
	 * 项目根目录
	 */
	public static final String WEB_ROOT = temp_path;

	/**
	 * 上传的目录
	 */
	// 测试可以使用这个
	public static final String UPLOAD_FILE_PATH = WEB_ROOT + "/upload";
	// 部署的正式需要使用这个
	// public static final String UPLOAD_FILE_PATH = "/opt/cfile" + "/upload";

	/**
	 * 学生提交上传的目录
	 */
	public static final String UPLOAD_FILE_STUDENT_PATH = UPLOAD_FILE_PATH + "/student";

	/**
	 * 学生提交重复文件的文件夹的文件名常量
	 */
	public static final String UPLOAD_FILE_STUDENT_REPEAT_FOLDER = "重复提交的文件";

	/**
	 * 正常响应的状态码
	 */
	public static final int SUCCESS_CODE = 0;
	/**
	 * 前端请求错误的状态码
	 */
	public static final int REQUEST_ERROR = 1;
	/**
	 * 服务端错误的状态码
	 */
	public static final int RESPONSE_ERROR = 2;
	/**
	 * 未认证
	 */
	public static final int UNAUTHENTICATION = 0;

	/**
	 * 已认证
	 */
	public static final int AUTHENTICATION = 1;
	/**
	 * 认证中
	 */
	public static final int AUTHENTICATING = 2;

}
