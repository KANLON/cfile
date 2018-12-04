package com.kanlon.cfile.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 权限认证的过滤器
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@Component
public class AuthenFilter implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(AuthenFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		// 获取到公开的地址
		if (url.equalsIgnoreCase("/")) {
			return true;
		}
		List<String> open_urls = new ArrayList<>();
		open_urls.add("/index.html");
		open_urls.add("/404.html");
		open_urls.add("/login");
		open_urls.add("/images");
		open_urls.add("/component");
		open_urls.add("/javascripts");
		open_urls.add("/js");
		open_urls.add("/stylesheets");
		open_urls.add("/css");
		open_urls.add("/reg");
		open_urls.add("/index");
		open_urls.add("/hello");
		open_urls.add("/student");

		// 如果是公开地址，则放行
		for (String open_url : open_urls) {
			if (url.startsWith(open_url)) {
				return true;
			}
		}
		// 判断用户是否已经登录
		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.info("session为空,用户没有登录，准备跳转");
			if (isAjax(request)) {
				printCNJSON("还没有登陆", response);
			} else {
				response.sendRedirect("/index.html");
			}
			return false;
		} else {
			if (session.getAttribute("user") != null) {
				logger.info("用户已经登录");
				return true;
			}
		}
		// 执行到这里跳转到登录页面，用户进行身份认证
		if (isAjax(request)) {
			printCNJSON("还没有登陆", response);
		} else {
			response.sendRedirect("/index.html");
		}
		return false;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * 判断请求是不是Ajax请求
	 *
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString());
	}

	/**
	 * 向浏览器输出请求错误的JSON数据
	 *
	 * @param result
	 * @param response
	 */
	private void printCNJSON(String msg, HttpServletResponse response) {
		String result = "{\"code\":1,\"msg\":\"" + msg + "\"}";
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("发送ajax请求时错误！" + e);
			e.printStackTrace();
		}
	}
}
