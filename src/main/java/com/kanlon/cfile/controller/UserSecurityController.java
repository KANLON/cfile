package com.kanlon.cfile.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kanlon.cfile.dao.mapper.TaskMapper;
import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.MailUtil;
import com.kanlon.cfile.utli.captcha.Captcha;
import com.kanlon.cfile.utli.captcha.CaptchaUtil;

/**
 * 用户的安全控制类
 *
 * @author zhangcanlong
 * @date 2018年12月27日
 */
@Controller
@RequestMapping("/security")
public class UserSecurityController {
	private static Logger logger = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TaskMapper taskMapper;

	@Autowired
	private TeacherUserMapper userMapper;

	@Autowired
	private HttpSession session;

	@Autowired
	private MailUtil mailUtil;

	/**
	 * 发送给新邮箱验证码
	 *
	 * @param newEmail
	 *            新邮箱
	 * @return
	 */
	@GetMapping(value = "/new/email/captcha")
	public JsonResult<String> sendNewEmailCaptcha(String newEmail) {
		JsonResult<String> result = new JsonResult<>();
		Captcha captcha = CaptchaUtil.create();
		String code = captcha.getCode().toLowerCase();
		logger.info(code);
		// 十分钟有效
		session.setAttribute(Constant.SESSION_MODIFY_EMAIL_CAPTCHA, code);
		// 发送邮箱
		try {
			mailUtil.sendHtmlMail(newEmail, "修改邮箱验证码", "修改邮箱验证码为(十分钟内有效):<br/>" + code);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStateCode(Constant.RESPONSE_ERROR, "发送邮件时发生异常");
			return result;
		}
		return result;
	}

	/**
	 * 修改邮箱
	 *
	 * @param email
	 * @param captcha
	 * @return
	 */
	@PutMapping(value = "/new/email")
	public JsonResult<String> modifyEmail(String email, String captcha) {
		JsonResult<String> result = new JsonResult<>();
		return result;
	}

	/**
	 * 修改密码
	 *
	 * @param newPassword
	 * @return
	 */
	@PutMapping(value = "/new/password")
	public JsonResult<String> modifyPassword(String newPassword) {
		JsonResult<String> result = new JsonResult<>();
		return result;
	}

}
