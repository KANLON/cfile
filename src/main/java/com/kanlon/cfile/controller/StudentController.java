package com.kanlon.cfile.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import com.kanlon.cfile.service.AsyncMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileCopyUtils;
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
    @Resource
    private AsyncMailService asyncMailService;

    /**
     * 学生提交文件
     *
     * @param submitVO 提交的信息
     * @param uid      用户id
     * @param tid      任务id
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit/{uid}/{tid}", method = {RequestMethod.POST})
    public JsonResult<String> submitFile(@ModelAttribute StudentSubmitFileVO submitVO, @PathVariable(value = "uid") Integer uid, @PathVariable(value = "tid") Integer tid, HttpServletRequest request) {
        JsonResult<String> result = new JsonResult<>();
        // 标志是否已经是提交了重复的文件
        boolean flag = false;
        // 获取ip客户ip地址
        String clientIP = IpUtil.getRealIP(request);
        if (submitVO.getFile() == null || StringUtils.isEmptyOrWhitespace(submitVO.getName()) || StringUtils.isEmptyOrWhitespace(submitVO.getStudentId())) {
            result.setStateCode(Constant.REQUEST_ERROR, "学号或文件或姓名为null");
            return result;
        }
        TaskPO task = taskMapper.getOne(tid);
        if (task == null) {
            result.setStateCode(Constant.REQUEST_ERROR, "所请求的任务不存在");
            return result;
        }
        // 检查是否已经过了提交时间
        if (new Date().after(task.getDendline())) {
            result.setStateCode(Constant.REQUEST_ERROR, "已经过了截止时间，不能提交了");
            return result;
        }
        String suffix = ".txt";

        String originalFilename = submitVO.getFile().getOriginalFilename();
        if (originalFilename.lastIndexOf(".") != -1 && originalFilename.charAt(originalFilename.length() - 1) != '.') {
            // 新的文件名，学号姓名.后缀名
            suffix = submitVO.getFile().getOriginalFilename().substring(submitVO.getFile().getOriginalFilename().lastIndexOf("."));
        }
        String studentIdAndName = submitVO.getStudentId() + submitVO.getName();
        String fileNewName = studentIdAndName + suffix;

        File fileStorePath = new File(Constant.UPLOAD_FILE_STUDENT_PATH + "/" + uid + "/" + tid);
        if (!fileStorePath.exists()) {
            fileStorePath.mkdirs();
        }
        try {
            // 遍历当前文件下所有文件，查看是否存在该学号和姓名，如果存在则将其移动到“重复提交的文件”文件夹并将在其文件名后加时间后，然后存储新的文件，如果不存在，直接存储新文件
            File[] tempFiles = fileStorePath.listFiles();
            for (int i = 0; i < tempFiles.length; i++) {
                String tempFileName = tempFiles[i].getName();
                // 如果遍历到文件是文件夹或者是没有.号的文件，则直接跳过
                if (tempFiles[i].isDirectory() || tempFileName.indexOf(".") == -1) {
                    continue;
                }
                if (tempFileName.substring(0, tempFileName.indexOf(".")).equals(studentIdAndName)) {
                    String repeatFilePath = fileStorePath + "/" + Constant.UPLOAD_FILE_STUDENT_REPEAT_FOLDER;
                    // 如果重复提交的文件夹不存在，则创建
                    File repeatPath = new File(repeatFilePath);
                    if (!repeatPath.exists()) {
                        repeatPath.mkdirs();
                    }
                    String oldSuffix = tempFiles[i].getName().substring(tempFiles[i].getName().indexOf("."));
                    File repeatFile = new File(repeatFilePath + "/" + studentIdAndName + TimeUtil.getLocalDateTimeByDate(new Date()) + oldSuffix);
                    repeatFile.createNewFile();
                    // 这里复制文件名之后会热部署，把之前的request清空
                    logger.debug("复制前输出request：" + request);
                    FileCopyUtils.copy(tempFiles[i], repeatFile);
                    logger.debug("复制后输出request：" + request);
                    tempFiles[i].delete();
                    flag = true;
                }
            }

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(fileStorePath + "/" + fileNewName)));
            logger.warn("文件上传到" + fileStorePath + "/" + fileNewName + "了！" + "发送者IP地址为：" + clientIP);
            out.write(submitVO.getFile().getBytes());
            out.flush();
            out.close();
            // 如果不是重复提交，则 增加提交人数
            if (!flag) {
                taskMapper.updateSubmitingNumByTid(tid);
            }
            // 异步发送邮件，进行备份
            asyncMailService.sentEmail(fileStorePath,fileNewName,clientIP);
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
     * @param tid 任务id
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
     * @param uid 用户id
     * @param tid 任务id
     * @return
     */
    @GetMapping("/task/list/{uid}/{tid}")
    public JsonResult<List<String>> getSubmitingList(@PathVariable @NotNull Integer uid, @PathVariable @NotNull Integer tid) {
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
            // 如果是文件
            if (tempFile.isFile()) {
                // 这里限定了前面学号是9位，只截止前面学号
                // String filename = cutByRegex(tempFile.getName(),
                // "(\\d+){0,9}\\D+.*");
                String filename = tempFile.getName().substring(0, 9);
                submitList.add(filename);
            }
        }
        return result;
    }

    /**
     * 根据表达式，从某字符串中提取自己想要的子字符串 例如：从“151612220张三三.xls”提取，数字，则regex为"(\\d+).*"
     *
     * @param str
     * @param regex
     * @return
     */
    private String cutByRegex(String str, String regex) {

        String reg = regex;
        String s = str;
        Pattern p2 = Pattern.compile(reg);
        Matcher m2 = p2.matcher(s);
        if (m2.find()) {
            String subStr = m2.group(1);
            // 组提取字符串
            return subStr;
        }
        return null;

    }
}
