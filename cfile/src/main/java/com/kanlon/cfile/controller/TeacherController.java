package com.kanlon.cfile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanlon.cfile.dao.mapper.TaskMapper;
import com.kanlon.cfile.domain.po.TaskPO;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.domain.vo.TaskInfoListsVO;
import com.kanlon.cfile.domain.vo.TaskVO;
import com.kanlon.cfile.domain.vo.TeacherTaskInfo;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.TimeUtil;
import com.kanlon.cfile.utli.ZipFilesUtil;

/**
 * 老师或班委的控制类
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

	private static Logger logger = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TaskMapper taskMapper;

	/**
	 * 创建提交任务
	 *
	 * @param task
	 *            任务信息
	 * @param session
	 * @return
	 */
	@PostMapping(value = "/task")
	public JsonResult<String> createTask(@RequestBody TaskVO task, HttpSession session) {
		JsonResult<String> result = new JsonResult<>();
		// 判断任务名是否为null
		if (StringUtils.isEmpty(task.getTaskName())) {
			result.setStateCode(Constant.REQUEST_ERROR, "任务名为null");
			return result;
		}
		// 转化文件格式(如果传入的文件格式是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getFileType())) {
			task.setFileType(null);
		}
		// 转化截止时间(如果传入的截止时间是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getDendlineStr())) {
			task.setDendlineStr(null);
		}
		// 转化预提交数量(如果传入的截止时间是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getSubmitNum())) {
			task.setSubmitNum(null);
		}

		TaskPO taskPO = new TaskPO();
		TeacherUserPO userPO = (TeacherUserPO) session.getAttribute("user");
		Integer uid = userPO.getUid();
		// 判断是否有重复的任务名称
		if (taskMapper.selectTaskNameByUid(uid, task.getTaskName()) >= 1) {
			result.setStateCode(Constant.REQUEST_ERROR, "任务名重复");
			return result;
		}
		taskPO.setUid(userPO.getUid());
		taskPO.setTaskName(task.getTaskName());
		taskPO.setCtime(new Date());
		taskPO.setFileType(task.getFileType());
		taskPO.setRemark(task.getRemark());
		taskPO.setDendline(TimeUtil.getDateBySimpleDateStr(task.getDendlineStr()));
		taskPO.setSubmitNum(task.getSubmitNum());
		taskMapper.insertOne(taskPO);

		return result;
	}

	/**
	 * 更新任务
	 *
	 * @param task
	 *            任务信息
	 * @param session
	 * @return
	 */
	@PutMapping(value = "/task/{tid}")
	public JsonResult<String> modifyTask(@RequestBody TaskVO task, @PathVariable(value = "tid") Integer tid,
			HttpSession session) {
		JsonResult<String> result = new JsonResult<>();
		// 判断任务名是否为null
		if (StringUtils.isEmpty(task.getTaskName())) {
			result.setStateCode(Constant.REQUEST_ERROR, "任务名为null");
			return result;
		}
		// 转化文件格式(如果传入的文件格式是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getFileType())) {
			task.setFileType(null);
		}
		// 转化截止时间(如果传入的截止时间是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getDendlineStr())) {
			task.setDendlineStr(null);
		}
		// 转化预提交数量(如果传入的截止时间是null或""，则将其转化为null)
		if (StringUtils.isEmpty(task.getSubmitNum())) {
			task.setSubmitNum(null);
		}

		TaskPO taskPO = taskMapper.getOne(tid);
		TeacherUserPO userPO = (TeacherUserPO) session.getAttribute("user");
		Integer uid = userPO.getUid();
		// 如果两个用户id不相等，则表示该用户没有权限修改该任务
		if (taskPO.getUid() != uid) {
			result.setStateCode(Constant.REQUEST_ERROR, "你没有权限修改该任务！");
			return result;
		}
		// 判断是否有重复的任务名称()
		if (taskMapper.selectTaskNameByUid(uid, task.getTaskName()) >= 1
				&& !task.getTaskName().equals(taskPO.getTaskName())) {
			result.setStateCode(Constant.REQUEST_ERROR, "任务名重复");
			return result;
		}
		taskPO.setTid(tid);
		taskPO.setUid(userPO.getUid());
		taskPO.setTaskName(task.getTaskName());
		taskPO.setMtime(new Date());
		taskPO.setFileType(task.getFileType());
		taskPO.setRemark(task.getRemark());
		taskPO.setDendline(TimeUtil.getDateBySimpleDateStr(task.getDendlineStr()));
		taskPO.setSubmitNum(task.getSubmitNum());
		taskMapper.updateByKey(taskPO);

		return result;
	}

	/**
	 * 获取所有发布的任务列表
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/all/tasks")
	public JsonResult<List<TaskInfoListsVO>> getAllTasks(HttpServletRequest request) throws Exception {
		JsonResult<List<TaskInfoListsVO>> result = new JsonResult<>();
		List<TaskInfoListsVO> tasks = new ArrayList<>();
		List<TaskPO> taskPOs = taskMapper.getAll();
		if (taskPOs == null || taskPOs.isEmpty()) {
			return result;
		}
		for (int i = 0; i < taskPOs.size(); i++) {
			TaskInfoListsVO taskInfoList = new TaskInfoListsVO();
			TaskPO po = taskPOs.get(i);
			taskInfoList.setAuthencation(po.getAuthentication());
			taskInfoList.setDendlineStr(TimeUtil.getSimpleDateTimeByDate(po.getDendline()));
			taskInfoList.setSubmitingNum(po.getSubmitingNum());
			taskInfoList.setSubmitNum(po.getSubmitNum());
			taskInfoList.setTaskName(po.getTaskName());
			taskInfoList.setTid(po.getTid());
			taskInfoList.setFileType(po.getFileType());
			taskInfoList.setRemark(po.getRemark());
			tasks.add(taskInfoList);
		}
		result.setData(tasks);
		return result;
	}

	/**
	 * 得到任务信息
	 *
	 * @param tid
	 * @return
	 */
	@GetMapping("/task/{tid}")
	public JsonResult<TeacherTaskInfo> getTaskInfo(@PathVariable(value = "tid") @NotNull Integer tid) {
		JsonResult<TeacherTaskInfo> result = new JsonResult<>();
		TaskPO task = taskMapper.getOne(tid);
		if (task == null) {
			result.setStateCode(Constant.REQUEST_ERROR, "所请求的任务不存在");
			return result;
		}
		TeacherTaskInfo taskInfo = new TeacherTaskInfo();
		taskInfo.setTid(tid);
		taskInfo.setTaskName(task.getTaskName());
		taskInfo.setDendlineStr(TimeUtil.getSimpleDateTimeByDate(task.getDendline()));
		taskInfo.setFileType(task.getFileType());
		taskInfo.setSubmitNum(task.getSubmitNum());
		taskInfo.setSubmitingNum(task.getSubmitingNum());
		taskInfo.setRemark(task.getRemark());
		taskInfo.setAuthentication(task.getAuthentication());
		taskInfo.setSubmitingList(task.getSubmitingList());
		taskInfo.setCtime(task.getCtime());
		taskInfo.setMtime(task.getMtime());
		result.setData(taskInfo);
		return result;
	}

	/**
	 * 得到某个任务已经提交的人员的名单(即是文件名，在前端再取相应字段，文件名后端下载需要用到)
	 *
	 * @param uid
	 *            用户id
	 * @param tid
	 *            任务id
	 * @return
	 */
	@GetMapping("/task/list/{tid}")
	public JsonResult<List<String>> getSubmitList(@PathVariable(value = "tid") @NotNull Integer tid,
			HttpServletRequest request) {
		JsonResult<List<String>> result = new JsonResult<>();
		List<String> submitList = new ArrayList<>();
		HttpSession session = request.getSession(false);
		TeacherUserPO user = (TeacherUserPO) session.getAttribute("user");
		result.setData(submitList);
		File submitFile = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + user.getUid() + "/" + tid);
		if (!submitFile.exists()) {
			return result;
		}
		File[] submitFiles = submitFile.listFiles();
		// 遍历所有提交的文件，得到文件名，从而获取名单
		for (int i = 0; i < submitFiles.length; i++) {
			File tempFile = submitFiles[i];
			if (tempFile.isFile()) {
				String filename = tempFile.getName();
				submitList.add(filename);
			}
		}
		return result;
	}

	/**
	 * 根据 任务id获取所有提交的文件的压缩包
	 *
	 * @param request
	 * @param response
	 * @param tid
	 *            任务id
	 */
	@GetMapping("/files/{tid}")
	public void getSubmitFileZip(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tid) {
		FileInputStream fis = null;
		HttpSession session = request.getSession();
		TeacherUserPO userPO = (TeacherUserPO) session.getAttribute("user");
		File files = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + userPO.getUid() + "/" + tid);

		try {
			if (!files.exists()) {
				response.setContentType("text/html");
				response.sendRedirect("/404.html");
				return;
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			File file = ZipFilesUtil.compress(files, "");
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error("获取文件时发生异常!", e);
			throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("获取文件时发生异常!", e);
					throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
				}
			}
		}

	}

	/**
	 * 根据 任务id和提交的学号姓名得到该文件
	 *
	 * @param request
	 * @param response
	 * @param tid
	 *            任务id
	 * @param filename
	 *            文件名 ，学号姓名
	 */
	@GetMapping("/file/{tid}")
	public void getSubmitFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "tid") Integer tid, @PathParam(value = "filename") String filename) {
		FileInputStream fis = null;
		HttpSession session = request.getSession();
		TeacherUserPO userPO = (TeacherUserPO) session.getAttribute("user");
		File file = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + userPO.getUid() + "/" + tid + "/" + filename);

		try {
			if (!file.exists()) {
				response.setContentType("text/html");
				response.sendRedirect("/404.html");
				return;
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error("获取文件时发生异常!", e);
			throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("获取文件时发生异常!", e);
					throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
				}
			}
		}

	}

	/**
	 * 得到某个项目的链接
	 *
	 * @param tid
	 *            项目id
	 * @param session
	 * @return
	 */
	@GetMapping("/task/link/{tid}")
	public JsonResult<String> getTaskLink(@PathVariable(value = "tid") Integer tid, HttpSession session) {
		JsonResult<String> result = new JsonResult<>();
		if (taskMapper.getOne(tid) == null) {
			result.setStateCode(Constant.REQUEST_ERROR, "该项目不存在！");
			return result;
		}
		TeacherUserPO userpo = (TeacherUserPO) session.getAttribute("user");
		Integer uid = userpo.getUid();
		// 构造链接
		StringBuilder taskLink = new StringBuilder("http://localhost:8080/student.html?");
		taskLink.append("uid=" + uid + "&tid=" + tid);
		result.setData(taskLink.toString());
		return result;

	}
}
