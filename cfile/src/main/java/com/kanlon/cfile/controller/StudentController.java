package com.kanlon.cfile.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.kanlon.cfile.domain.vo.StudentSubmitFileVO;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;

/**
 * 学生的提交的controller
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@RestController
@RequestMapping("/student")
public class StudentController {

	private Logger logger = LoggerFactory.getLogger(StudentController.class);

	@RequestMapping(value = "/info/{uid}/{taskId}", method = { RequestMethod.POST, RequestMethod.GET })
	public JsonResult<String> submitFile(@ModelAttribute StudentSubmitFileVO submitVO,
			@PathVariable(value = "uid") Integer uid, @PathVariable(value = "taskId") Integer taskId) {
		JsonResult<String> result = new JsonResult<>();
		if (submitVO.getFile() == null || StringUtils.isEmptyOrWhitespace(submitVO.getName())
				|| StringUtils.isEmptyOrWhitespace(submitVO.getStudentId())) {
			result.setStateCode(Constant.REQUEST_ERROR, "学号，文件，姓名，或任务id为null");
			return result;
		}
		// 新的文件名，学号姓名.后缀名
		String fileNewName = submitVO.getStudentId() + submitVO.getName() + submitVO.getFile().getOriginalFilename()
				.substring(submitVO.getFile().getOriginalFilename().lastIndexOf("."));
		File fileStorePath = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + uid + "/" + taskId);
		if (!fileStorePath.exists()) {
			fileStorePath.mkdirs();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(new File(fileStorePath + "/" + fileNewName)));
			logger.info("文件上传到" + fileStorePath + "/" + fileNewName + "了！");
			out.write(submitVO.getFile().getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			result.setStateCode(Constant.RESPONSE_ERROR, "存储文件时发生错误！" + e.getMessage());
			logger.error("存储学生上传文件时发生错误！", e);
			return result;
		}
		return result;
	}
}
