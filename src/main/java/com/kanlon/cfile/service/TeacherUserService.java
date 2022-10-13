package com.kanlon.cfile.service;

import com.kanlon.cfile.configure.ProjectConfigProperty;
import com.kanlon.cfile.dao.mapper.TaskMapper;
import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.domain.po.TaskPO;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.domain.vo.StudentSubmitFileVO;
import com.kanlon.cfile.domain.vo.StudentTaskInfoVO;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 学生的service
 *
 * @author zhangcanlong
 * @since 2022/10/13 11:30
 **/
@Slf4j
@Service
public class TeacherUserService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TeacherUserMapper userMapper;
    @Resource
    private AsyncMailService asyncMailService;
    @Resource
    private ProjectConfigProperty projectConfigProperty;

    private static final String DOT = ".";

    /**
     * 学生提交文件
     *
     * @param submitVO 提交的信息
     * @param uid      用户id
     * @param tid      任务id
     * @param clientIp 客户端ip
     * @return 提交后的文件路径
     */
    public JsonResult<String> submitFile(StudentSubmitFileVO submitVO, Integer uid, Integer tid, String clientIp) {
        JsonResult<String> result = new JsonResult<>();
        // 标志是否已经是提交了重复的文件
        boolean flag = false;
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
        if (StringUtils.isEmpty(originalFilename)) {
            result.setStateCode(Constant.REQUEST_ERROR, "上传文件名为空，不能提交");
            return result;
        }

        if (originalFilename.lastIndexOf(DOT) != -1 && originalFilename.charAt(originalFilename.length() - 1) != DOT.charAt(0)) {
            // 新的文件名，学号姓名.后缀名
            suffix = submitVO.getFile().getOriginalFilename().substring(submitVO.getFile().getOriginalFilename().lastIndexOf(DOT));
        }
        String studentIdAndName = submitVO.getStudentId() + submitVO.getName();
        String fileNewName = studentIdAndName + suffix;

        File fileStorePath = new File(projectConfigProperty.getUpdateFileBasePath() + "/" + uid + "/" + tid);
        if (!fileStorePath.exists()) {
            if (!fileStorePath.mkdirs()) {
                result.setStateCode(Constant.RESPONSE_ERROR, "创建文件路径！路径为：" + fileStorePath.getAbsolutePath());
                return result;
            }
        }
        try {
            // 遍历当前文件下所有文件，查看是否存在该学号和姓名，如果存在则将其移动到“重复提交的文件”文件夹并将在其文件名后加时间后，然后存储新的文件，如果不存在，直接存储新文件
            File[] tempFiles = Optional.ofNullable(fileStorePath.listFiles()).orElse(new File[0]);
            for (File tempFile : tempFiles) {
                String tempFileName = tempFile.getName();
                // 如果遍历到文件是文件夹或者是没有.号的文件，则直接跳过
                if (tempFile.isDirectory() || !tempFileName.contains(DOT)) {
                    continue;
                }
                if (tempFileName.substring(0, tempFileName.indexOf(DOT)).equals(studentIdAndName)) {
                    String repeatFilePath = fileStorePath + "/" + Constant.UPLOAD_FILE_STUDENT_REPEAT_FOLDER;
                    // 如果重复提交的文件夹不存在，则创建
                    File repeatPath = new File(repeatFilePath);
                    if (!repeatPath.exists()) {
                        if (!repeatPath.mkdirs()) {
                            result.setStateCode(Constant.RESPONSE_ERROR, "创建文件路径！路径为：" + repeatPath);
                            return result;
                        }
                    }
                    String oldSuffix = tempFile.getName().substring(tempFile.getName().indexOf(DOT));
                    File repeatFile = new File(repeatFilePath + "/" + studentIdAndName + TimeUtil.getLocalDateTimeByDate(new Date()) + oldSuffix);
                    if (!repeatFile.createNewFile()) {
                        result.setStateCode(Constant.RESPONSE_ERROR, "创建文件失败！文件或路径为：" + repeatFile.getAbsolutePath());
                        return result;
                    }
                    // 这里复制文件名之后会热部署，把之前的request清空
                    FileCopyUtils.copy(tempFile, repeatFile);
                    if (!tempFile.delete()) {
                        result.setStateCode(Constant.RESPONSE_ERROR, "删除文件失败！文件为：" + tempFile.getAbsolutePath());
                        return result;
                    }
                    flag = true;
                }
            }

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(fileStorePath + "/" + fileNewName)));
            log.info("文件上传到" + fileStorePath + "/" + fileNewName + "了！" + "发送者IP地址为：" + clientIp);
            out.write(submitVO.getFile().getBytes());
            out.flush();
            out.close();
            // 如果不是重复提交，则 增加提交人数
            if (!flag) {
                taskMapper.updateSubmitingNumByTid(tid);
            }
            // 异步发送邮件，进行备份
            asyncMailService.sentEmail(fileStorePath, fileNewName, clientIp);
        } catch (IOException e) {
            result.setStateCode(Constant.RESPONSE_ERROR, "存储文件时发生错误！" + e.getMessage());
            log.error("存储学生上传文件时发生错误！", e);
            return result;
        }
        return result;
    }

    /**
     * 得到任务信息
     *
     * @param tid 任务id
     * @return 学生信息
     */
    public JsonResult<StudentTaskInfoVO> getTaskInfo(Integer tid) {
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
        TeacherUserPO userPo = userMapper.getOne(task.getUid());
        taskInfo.setPublisher(StringUtils.isEmpty(userPo.getNickname()) ? "默认用户昵称" : userPo.getNickname());
        result.setData(taskInfo);
        return result;
    }

    /**
     * 得到已经提交了人的学号
     *
     * @param uid 用户id
     * @param tid 任务id
     * @return 学号列表
     */
    public JsonResult<List<String>> getSubmittingList(Integer uid, Integer tid) {
        JsonResult<List<String>> result = new JsonResult<>();
        List<String> submitList = new ArrayList<>();
        result.setData(submitList);
        File submitFile = new File(projectConfigProperty.getUpdateFileBasePath() + "/" + uid + "/" + tid);
        if (!submitFile.exists()) {
            return result;
        }
        File[] submitFiles = Optional.ofNullable(submitFile.listFiles()).orElse(new File[0]);
        // 遍历所有提交的文件，得到文件名，从而获取名单
        for (File tempFile : submitFiles) {
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


}
