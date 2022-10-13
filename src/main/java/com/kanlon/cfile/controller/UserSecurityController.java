package com.kanlon.cfile.controller;

import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.MailUtil;
import com.kanlon.cfile.utli.TimeUtil;
import com.kanlon.cfile.utli.captcha.Captcha;
import com.kanlon.cfile.utli.captcha.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 用户的安全控制类
 *
 * @author zhangcanlong
 * @date 2018年12月27日
 */
@Slf4j
@Controller
@RequestMapping("/security")
public class UserSecurityController {

    @Resource
    private TeacherUserMapper userMapper;
    @Resource
    private HttpSession session;
    @Resource
    private MailUtil mailUtil;

    /**
     * 发送给新邮箱验证码
     *
     * @param newEmail 新邮箱
     * @return 是否成功
     */
    @GetMapping(value = "/new/email/captcha")
    public JsonResult<String> sendNewEmailCaptcha(String newEmail) {
        JsonResult<String> result = new JsonResult<>();
        Captcha captcha = CaptchaUtil.create();
        String code = captcha.getCode().toLowerCase();
        log.info(code);
        // 十分钟有效,存放，code#currenttime
        session.setAttribute(Constant.SESSION_MODIFY_EMAIL_CAPTCHA, code + "#" + System.currentTimeMillis());
        // 发送邮箱
        try {
            mailUtil.sendHtmlMail(newEmail, "修改邮箱验证码", "修改邮箱验证码为(十分钟内有效):<br/>" + code);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStateCode(Constant.RESPONSE_ERROR, "发送邮件时发生异常");
            return result;
        }
        return result;
    }

    /**
     * 修改邮箱
     *
     * @param email   邮箱地址
     * @param captcha 验证码信息
     * @return 是否成功
     */
    @PutMapping(value = "/new/email")
    public JsonResult<String> modifyEmail(String email, String captcha) {
        JsonResult<String> result = new JsonResult<>();
        try {
            String codeAndTime = (String) session.getAttribute(Constant.SESSION_MODIFY_EMAIL_CAPTCHA);
            if (codeAndTime == null) {
                result.setStateCode(Constant.REQUEST_ERROR, "还没发送验证码");
                return result;
            }
            String sessionCaptcha = codeAndTime.split("#")[0];
            long createCaptchaTime = Long.parseLong(codeAndTime.split("#")[1]);
            if (!sessionCaptcha.equals(captcha)) {
                result.setStateCode(Constant.REQUEST_ERROR, "验证码错误");
                return result;
            } else if (createCaptchaTime + TimeUtil.TEN_MINUTE < System.currentTimeMillis()) {
                result.setStateCode(Constant.REQUEST_ERROR, "验证码过期了");
                return result;
            }
            TeacherUserPO oldUser = (TeacherUserPO) session.getAttribute(Constant.SESSION_USER);
            TeacherUserPO emailModifyUser = new TeacherUserPO();
            emailModifyUser.setEmail(email);
            emailModifyUser.setUid(oldUser.getUid());
            userMapper.updateUserOneByKey(emailModifyUser);
        } catch (Exception e) {
            log.error("修改邮箱异常", e);
            result.setStateCode(Constant.RESPONSE_ERROR, "修改邮箱错误!" + e.getMessage());
        }
        return result;
    }

    /**
     * 修改密码
     *
     * @param newPassword 新的密码
     * @return 是否成功
     */
    @PutMapping(value = "/new/password")
    public JsonResult<String> modifyPassword(String newPassword) {
        JsonResult<String> result = new JsonResult<>();
        TeacherUserPO oldUser = (TeacherUserPO) session.getAttribute(Constant.SESSION_USER);
        TeacherUserPO passwordModifyUser = new TeacherUserPO();
        passwordModifyUser.setEmail(newPassword);
        passwordModifyUser.setUid(oldUser.getUid());
        userMapper.updateUserOneByKey(passwordModifyUser);
        return result;
    }

}
