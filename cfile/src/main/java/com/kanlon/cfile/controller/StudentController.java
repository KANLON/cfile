package com.kanlon.cfile.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.kanlon.cfile.dao.mapper.TaskMapper;
import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.domain.po.TaskPO;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.domain.vo.StudentSubmitFileVO;
import com.kanlon.cfile.domain.vo.StudentTaskInfoVO;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.IpUtil;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.TimeUtil;

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

	@Autowired
	private TaskMapper taskMapper;

	@Autowired
	private TeacherUserMapper userMapper;

	/**
	 * 学生提交文件
	 *
	 * @param submitVO
	 *            提交的信息
	 * @param uid
	 *            用户id
	 * @param tid
	 *            任务id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submit/{uid}/{tid}", method = { RequestMethod.POST })
	public JsonResult<String> submitFile(@ModelAttribute StudentSubmitFileVO submitVO,
			@PathVariable(value = "uid") Integer uid, @PathVariable(value = "tid") Integer tid,
			HttpServletRequest request) {
		JsonResult<String> result = new JsonResult<>();
		if (submitVO.getFile() == null || StringUtils.isEmptyOrWhitespace(submitVO.getName())
				|| StringUtils.isEmptyOrWhitespace(submitVO.getStudentId())) {
			result.setStateCode(Constant.REQUEST_ERROR, "学号或文件或姓名为null");
			return result;
		}
		// 新的文件名，学号姓名.后缀名
		String fileNewName = submitVO.getStudentId() + submitVO.getName() + submitVO.getFile().getOriginalFilename()
				.substring(submitVO.getFile().getOriginalFilename().lastIndexOf("."));
		File fileStorePath = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + uid + "/" + tid);
		if (!fileStorePath.exists()) {
			fileStorePath.mkdirs();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(new File(fileStorePath + "/" + fileNewName)));
			logger.warn("文件上传到" + fileStorePath + "/" + fileNewName + "了！" + "发送者IP地址为：" + IpUtil.getRealIP(request));
			out.write(submitVO.getFile().getBytes());
			out.flush();
			out.close();
			// 增加提交人数
			taskMapper.updateSubmitingNumByTid(tid);
		} catch (IOException e) {
			result.setStateCode(Constant.RESPONSE_ERROR, "存储文件时发生错误！" + e.getMessage());
			logger.error("存储学生上传文件时发生错误！", e);
			return result;
		}
		return result;
	}

	/**
	 * 得到任务信息
	 *
	 * @param uid
	 * @param tid
	 * @return
	 */
	@GetMapping("/task/{tid}")
	public JsonResult<StudentTaskInfoVO> getTaskInfo(@PathVariable(value = "tid") @NotNull Integer tid) {
		JsonResult<StudentTaskInfoVO> result = new JsonResult<>();
		TaskPO task = taskMapper.getOne(tid);
		if (task == null) {
			result.setStateCode(Constant.REQUEST_ERROR, "所请求的任务不存在");
			return result;
		}
		StudentTaskInfoVO taskInfo = new StudentTaskInfoVO();
		taskInfo.setAuthentication(task.getAuthentication());
		taskInfo.setDendlineStr(TimeUtil.getSimpleDateTimeByDate(task.getDendline()));
		taskInfo.setFileType(task.getFileType());
		taskInfo.setRemark(task.getRemark());
		taskInfo.setSubmitNum(task.getSubmitNum());
		taskInfo.setTaskName(task.getTaskName());
		// 获取昵称
		TeacherUserPO userPO = userMapper.getOne(task.getUid());
		taskInfo.setPublisher(StringUtils.isEmpty(userPO.getNickname()) ? "默认用户昵称" : userPO.getNickname());
		result.setData(taskInfo);
		return result;
	}

	/**
	 * 得到已经提交了人的学号
	 *
	 * @param uid
	 *            用户id
	 * @param tid
	 *            任务id
	 * @return
	 */
	@GetMapping("/task/list/{uid}/{tid}")
	public JsonResult<List<String>> getSubmitingList(@PathVariable @NotNull Integer uid,
			@PathVariable @NotNull Integer tid) {
		JsonResult<List<String>> result = new JsonResult<>();
		List<String> submitList = new ArrayList<>();
		result.setData(submitList);
		File submitFile = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + uid + "/" + tid);
		if (!submitFile.exists()) {
			return result;
		}
		File[] submitFiles = submitFile.listFiles();
		// 遍历所有提交的文件，得到文件名，从而获取名单
		for (int i = 0; i < submitFiles.length; i++) {
			File tempFile = submitFiles[i];
			if (tempFile.isFile()) {
				String filename = tempFile.getName().substring(tempFile.getName().indexOf(File.pathSeparator));
				submitList.add(filename);
			}
		}
		return result;
	}

}
