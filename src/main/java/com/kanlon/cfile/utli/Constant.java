package com.kanlon.cfile.utli;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

    /**
     * session中的存放用户信息的常量
     */
    public static final String SESSION_USER = "user";

    /**
     * session中存放忘记密码的的邮箱验证码的key
     */
    public static final String SESSION_FORGET_PASSWORD_EMAIL_CAPTCHA = "ForgetPasswordEmailCaptcha";

    /**
     * 忘记密码时，发送邮箱验证码，在session存放该用户的uid
     */
    public static final String SESSION_FORGET_PASSWORD_UID = "ForgetPasswordUid";

    /**
     * session中存放修改邮箱验证码的key
     */
    public static final String SESSION_MODIFY_EMAIL_CAPTCHA = "ModifyEmailCaptcha";

    /**
     * session中存放注册验证码的key
     */
    public static final String SESSION_REG_CAPTCHA = "regCaptcha";

    /**
     * session中存放登录验证的key
     */
    public static final String SESSION_LOGIN_CAPTCHA = "loginCaptcha";

}
