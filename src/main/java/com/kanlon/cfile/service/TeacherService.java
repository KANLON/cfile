package com.kanlon.cfile.service;

import com.kanlon.cfile.configure.ProjectConfigProperty;
import com.kanlon.cfile.dao.mapper.TaskMapper;
import com.kanlon.cfile.dao.mapper.TeacherUserMapper;
import com.kanlon.cfile.domain.po.TaskPO;
import com.kanlon.cfile.domain.po.TeacherUserPO;
import com.kanlon.cfile.domain.vo.TaskInfoListsVO;
import com.kanlon.cfile.domain.vo.TaskVO;
import com.kanlon.cfile.domain.vo.TeacherCenterVO;
import com.kanlon.cfile.domain.vo.TeacherTaskInfo;
import com.kanlon.cfile.utli.Constant;
import com.kanlon.cfile.utli.JsonResult;
import com.kanlon.cfile.utli.TimeUtil;
import com.kanlon.cfile.utli.ZipFilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 教师 的操作相关service
 *
 * @author zhangcanlong
 * @since 2022/10/13 11:31
 **/
@Slf4j
@Service
public class TeacherService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TeacherUserMapper userMapper;
    @Resource
    private HttpSession session;
    @Resource
    private AsyncRemoveFileService asyncRemoveFileService;
    @Resource
    private ProjectConfigProperty projectConfigProperty;

    /**
     * 创建提交任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    public JsonResult<Void> createTask(TaskVO task) {
        JsonResult<Void> result = new JsonResult<>();
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

        TaskPO taskPo = new TaskPO();
        TeacherUserPO userPo = (TeacherUserPO) session.getAttribute("user");
        Integer uid = userPo.getUid();
        // 判断是否有重复的任务名称
        if (taskMapper.selectTaskNameByUid(uid, task.getTaskName()) >= 1) {
            result.setStateCode(Constant.REQUEST_ERROR, "任务名重复");
            return result;
        }
        taskPo.setUid(userPo.getUid());
        taskPo.setTaskName(task.getTaskName());
        taskPo.setCtime(new Date());
        taskPo.setFileType(task.getFileType());
        taskPo.setRemark(task.getRemark());
        taskPo.setDendline(TimeUtil.getDateBySimpleDateStr(task.getDendlineStr()));
        taskPo.setSubmitNum(task.getSubmitNum());
        taskMapper.insertOne(taskPo);

        return result;
    }

    /**
     * 更新任务
     *
     * @param task 任务信息
     * @return 更新的任务id
     */
    public JsonResult<Integer> modifyTask(TaskVO task, Integer tid) {
        JsonResult<Integer> result = new JsonResult<>();
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

        TaskPO taskPo = taskMapper.getOne(tid);
        TeacherUserPO userPo = (TeacherUserPO) session.getAttribute("user");
        Integer uid = userPo.getUid();
        // 如果两个用户id不相等，则表示该用户没有权限修改该任务
        if (!taskPo.getUid().equals(uid)) {
            result.setStateCode(Constant.REQUEST_ERROR, "你没有权限修改该任务！");
            return result;
        }
        // 判断是否有重复的任务名称()
        if (taskMapper.selectTaskNameByUid(uid, task.getTaskName()) >= 1 && !task.getTaskName().equals(taskPo.getTaskName())) {
            result.setStateCode(Constant.REQUEST_ERROR, "任务名重复");
            return result;
        }
        taskPo.setTid(tid);
        taskPo.setUid(userPo.getUid());
        taskPo.setTaskName(task.getTaskName());
        taskPo.setMtime(new Date());
        taskPo.setFileType(task.getFileType());
        taskPo.setRemark(task.getRemark());
        taskPo.setDendline(TimeUtil.getDateBySimpleDateStr(task.getDendlineStr()));
        taskPo.setSubmitNum(task.getSubmitNum());
        taskMapper.updateByKey(taskPo);

        result.setData(taskPo.getTid());
        return result;
    }

    /**
     * 得到个人信息
     *
     * @return 返回教师或班委个人信息类
     */
    public JsonResult<TeacherCenterVO> getCenterInfo() {
        JsonResult<TeacherCenterVO> result = new JsonResult<>();
        TeacherUserPO user = (TeacherUserPO) session.getAttribute(Constant.SESSION_USER);
        TeacherCenterVO userVO = new TeacherCenterVO();
        userVO.setAuthentication(0);
        userVO.setEmail(user.getEmail());
        userVO.setNickname(user.getNickname());
        userVO.setUsername(user.getUsername());
        result.setData(userVO);
        return result;
    }

    /**
     * 修改个人中心信息(暂时只能修改昵称)
     *
     * @param newUserInfo 新的用户信息
     * @return 是否成功
     */
    public JsonResult<String> modifyCenterInfo(TeacherCenterVO newUserInfo) {
        JsonResult<String> result = new JsonResult<>();
        TeacherUserPO user = (TeacherUserPO) session.getAttribute(Constant.SESSION_USER);
        int uid = user.getUid();
        TeacherUserPO userPo = new TeacherUserPO();
        userPo.setUid(uid);
        userPo.setNickname(StringUtils.isEmpty(newUserInfo.getNickname()) ? null : newUserInfo.getNickname());
        try {
            userMapper.updateUserOneByKey(userPo);
            // 修改完后，更新session里面的用户信息
            user = userMapper.getOne(uid);
            session.setAttribute(Constant.SESSION_USER, user);
        } catch (Exception e) {
            log.error("更新用户信息时错误！", e);
            result.setStateCode(Constant.RESPONSE_ERROR, "修改失败!" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取所有发布的任务列表
     *
     * @param request 请求
     * @return 任务列表信息
     */
    public JsonResult<List<TaskInfoListsVO>> getAllTasks(HttpServletRequest request) {
        JsonResult<List<TaskInfoListsVO>> result = new JsonResult<>();
        List<TaskInfoListsVO> tasks = new ArrayList<>();
        HttpSession session = request.getSession();
        TeacherUserPO user = (TeacherUserPO) session.getAttribute("user");
        List<TaskPO> taskPoList = taskMapper.getAll(user.getUid());

        if (taskPoList == null || taskPoList.isEmpty()) {
            return result;
        }
        for (TaskPO po : taskPoList) {
            TaskInfoListsVO taskInfoList = new TaskInfoListsVO();
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
     * @param tid 任务id
     * @return 单个任务信息
     */
    public JsonResult<TeacherTaskInfo> getTaskInfo(Integer tid) {
        JsonResult<TeacherTaskInfo> result = new JsonResult<>();
        TaskPO task = taskMapper.getOne(tid);
        if (task == null) {
            result.setStateCode(Constant.REQUEST_ERROR, "所请求的任务不存在");
            return result;
        }
        TeacherUserPO userPo = (TeacherUserPO) session.getAttribute("user");
        Integer uid = userPo.getUid();
        // 如果两个用户id不相等，则表示该用户没有权限修改该任务
        if (!task.getUid().equals(uid)) {
            result.setStateCode(Constant.REQUEST_ERROR, "你没有权限修改该任务！");
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
     * @param tid 任务id
     * @return 人员名单列表
     */
    public JsonResult<List<String>> getSubmitList(Integer tid) {
        JsonResult<List<String>> result = new JsonResult<>();
        List<String> submitList = new ArrayList<>();
        TeacherUserPO user = (TeacherUserPO) session.getAttribute("user");
        result.setData(submitList);
        File submitFile = new File(projectConfigProperty.getUpdateFileBasePath() + "/" + user.getUid() + "/" + tid);
        if (!submitFile.exists()) {
            return result;
        }
        File[] submitFiles = Optional.ofNullable(submitFile.listFiles()).orElse(new File[0]);
        // 遍历所有提交的文件，得到文件名，从而获取名单
        for (File tempFile : submitFiles) {
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
     * @param response 响应
     * @param tid      任务id
     */
    public void getSubmitFileZip(HttpServletResponse response, Integer tid) {
        FileInputStream fis = null;
        TeacherUserPO userPo = (TeacherUserPO) session.getAttribute("user");
        File files = new File(projectConfigProperty.getUpdateFileBasePath() + "/" + userPo.getUid() + "/" + tid);

        try {
            if (!files.exists()) {
                response.setContentType("text/html");
                response.sendRedirect("/404.html");
                return;
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            File file = ZipFilesUtil.compress(files, "");
            if (file == null) {
                throw new RuntimeException("压缩文件获取到的文件为null");
            }
            // 压缩之后可以立刻异步执行删除操作
            // 如果不新建一条线程来删除文件，则一旦发生异常，不会继续继续执行删除操作（常见点击下载后，不下载文件）
            String tempFileName = projectConfigProperty.getUpdateFileBasePath() + "/" + userPo.getUid() + "/" + tid + "/" + Constant.UPLOAD_FILE_STUDENT_REPEAT_FOLDER + ".zip";
            asyncRemoveFileService.removeOneFile(tempFileName);
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("获取文件时发生异常!", e);
            throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("获取文件时发生异常!", e);
                }
            }
        }

    }

    /**
     * 根据 任务id和提交的学号姓名得到该文件
     *
     * @param response 响应
     * @param tid      任务id
     * @param filename 文件名 ，学号姓名
     */
    public void getSubmitFile(HttpServletResponse response, Integer tid, String filename) {
        TeacherUserPO userPo = (TeacherUserPO) session.getAttribute("user");
        TaskPO taskPo = taskMapper.getOne(tid);
        if (taskPo == null || !Objects.equals(taskPo.getUid(), userPo.getUid())) {
            throw new RuntimeException("你没有该任务的下载权限！任务id为：" + tid);
        }

        // 存储对应任务的文件目录
        File parentFileDir = new File(projectConfigProperty.getUpdateFileBasePath() + "/" + userPo.getUid() + "/" + tid);
        if (!parentFileDir.exists()) {
            throw new RuntimeException("对应任务没上传的文件！");
        }
        File[] theTaskAllUploadFiles = Optional.ofNullable(parentFileDir.listFiles()).orElse(new File[0]);
        File file = null;
        for (File tempFile : theTaskAllUploadFiles) {
            if (Objects.equals(tempFile.getName(), filename)) {
                file = tempFile;
            }
        }

        if (file == null) {
            throw new RuntimeException("要下载的文件不存在！");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            if (!file.exists()) {
                response.setContentType("text/html");
                response.sendRedirect("/404.html");
                return;
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            // 解决显示不了下载文件时，中文文件名问题，大体的原因就是header中只支持ASCII，所以我们传输的文件名必须是ASCII
            String fileName = new String(file.getName().getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("获取文件时发生异常!", e);
            throw new RuntimeException("获取文件时发生异常！" + e.getMessage());
        }

    }

    /**
     * 得到某个项目的链接
     *
     * @param tid 项目id
     * @return 项目链接
     */
    public JsonResult<String> getTaskLink(Integer tid) {
        JsonResult<String> result = new JsonResult<>();
        if (taskMapper.getOne(tid) == null) {
            result.setStateCode(Constant.REQUEST_ERROR, "该项目不存在！");
            return result;
        }
        TeacherUserPO userpo = (TeacherUserPO) session.getAttribute("user");
        Integer uid = userpo.getUid();
        // 构造链接
        result.setData("/student.html?" + "uid=" + uid + "&tid=" + tid);
        return result;

    }

}
