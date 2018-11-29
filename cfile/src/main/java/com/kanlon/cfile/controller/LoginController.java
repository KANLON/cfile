package com.kanlon.cfile.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.domain.vo.RegisterInfoVO;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.MD5Util;
import com.kanlon.cfile.utli.RandomUtil;
import com.kanlon.cfile.utli.captcha.Captcha;
import com.kanlon.cfile.utli.captcha.CaptchaUtil;

/**
 * 教师或班委登录的controller
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@RequestMapping("/teacher")
@RestController
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private TeacherUserMapper teacherUserMapper;

	/**
	 * 老师或班委注册
	 *
	 * @param registerVO
	 *            注册信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JsonResult<String> teachRegister(@ModelAttribute RegisterInfoVO registerVO, HttpServletRequest request) {
		JsonResult<String> result = new JsonResult<>();
		if (StringUtils.isEmptyOrWhitespace(registerVO.getPassword())
				|| StringUtils.isEmptyOrWhitespace(registerVO.getUsername())) {
			result.setStateCode(Constant.REQUEST_ERROR, "用户名或密码不能为null或全是空白字符");
			return result;
		}
		HttpSession session = request.getSession(true);
		String sessionCode = (String) session.getAttribute("regCaptcha");
		if (!registerVO.getImgCaptcha().equals(sessionCode)) {
			result.setStateCode(Constant.REQUEST_ERROR, "验证码错误！");
			session.removeAttribute("regCaptcha");
			return result;
		}
		session.removeAttribute("regCaptcha");
		// 检查用户名是否已经存在了
		int userNum = teacherUserMapper.selectTeacherByUsername(registerVO.getUsername());
		if (userNum >= 1) {
			result.setStateCode(Constant.REQUEST_ERROR, "该用户名已存在");
			return result;
		}
		// 检查邮箱是否重复
		if (teacherUserMapper.selectTeacherByEmail(registerVO.getEmail()) >= 1) {
			result.setStateCode(Constant.REQUEST_ERROR, "该邮箱已存在");
			return result;
		}

		String salt = RandomUtil.createSalt();
		String md5Password = MD5Util.md5(registerVO.getPassword() + salt);
		// 将邮箱，用户名，密码，盐存入数据库
		TeacherUserPO userPO = new TeacherUserPO();
		userPO.setEmail(registerVO.getEmail());
		userPO.setUsername(registerVO.getUsername());
		userPO.setPassword(md5Password);
		userPO.setSalt(salt);
		teacherUserMapper.insert(userPO);
		return result;
	}

	/**
	 * 获取注册验证码
	 *
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/register/captcha", method = RequestMethod.GET)
	public void getRegisterCaptcha(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			Captcha captcha = CaptchaUtil.create();
			captcha.createCaptchaImg(out);
			String code = captcha.getCode();
			// 将验证码放入session中
			HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(30 * 60);
			session.setAttribute("regCaptcha", code);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获取验证码错误！", e);
		}
	}

	/**
	 * 老师或班委注册
	 *
	 * @param registerVO
	 *            注册信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JsonResult<String> teachLogin(@ModelAttribute RegisterInfoVO registerVO, HttpServletRequest request) {
		JsonResult<String> result = new JsonResult<>();
		if (StringUtils.isEmptyOrWhitespace(registerVO.getPassword())
				|| StringUtils.isEmptyOrWhitespace(registerVO.getUsername())) {
			result.setStateCode(Constant.REQUEST_ERROR, "用户名或密码不能为null或全是空白字符");
			return result;
		}
		HttpSession session = request.getSession(true);
		String sessionCode = (String) session.getAttribute("regCaptcha");
		if (!registerVO.getImgCaptcha().equals(sessionCode)) {
			result.setStateCode(Constant.REQUEST_ERROR, "验证码错误！");
			session.removeAttribute("regCaptcha");
			return result;
		}
		session.removeAttribute("regCaptcha");
		// 检查用户名是否已经存在了
		int userNum = teacherUserMapper.selectTeacherByUsername(registerVO.getUsername());
		if (userNum >= 1) {
			result.setStateCode(Constant.REQUEST_ERROR, "该用户名已存在");
			return result;
		}
		// 检查邮箱是否重复
		if (teacherUserMapper.selectTeacherByEmail(registerVO.getEmail()) >= 1) {
			result.setStateCode(Constant.REQUEST_ERROR, "该邮箱已存在");
			return result;
		}

		String salt = RandomUtil.createSalt();
		String md5Password = MD5Util.md5(registerVO.getPassword() + salt);
		// 将邮箱，用户名，密码，盐存入数据库
		TeacherUserPO userPO = new TeacherUserPO();
		userPO.setEmail(registerVO.getEmail());
		userPO.setUsername(registerVO.getUsername());
		userPO.setPassword(md5Password);
		userPO.setSalt(salt);
		teacherUserMapper.insert(userPO);
		return result;
	}

	/**
	 * 获取登陆验证码
	 *
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/register/captcha", method = RequestMethod.GET)
	public void getLoginCaptcha(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			Captcha captcha = CaptchaUtil.create();
			captcha.createCaptchaImg(out);
			String code = captcha.getCode();
			// 将验证码放入session中
			HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(30 * 60);
			session.setAttribute("loginCaptcha", code);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获取验证码错误！", e);
		}
	}
}
