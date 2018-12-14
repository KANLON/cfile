package com.kanlon.cfile.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;

/**
 * 全局异常处理类
 *
 * @author zhangcanlong
 * @date 2018年12月11日
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public JsonResult<String> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		JsonResult<String> result = new JsonResult<>();
		result.setStateCode(Constant.RESPONSE_ERROR, e.getMessage());
		logger.error("内部服务器错误！", e);
		return result;
	}
}
