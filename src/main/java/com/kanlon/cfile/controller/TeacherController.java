package com.kanlon.cfile.controller;

import com.kanlon.cfile.domain.vo.TaskInfoListsVO;
import com.kanlon.cfile.domain.vo.TaskVO;
import com.kanlon.cfile.domain.vo.TeacherCenterVO;
import com.kanlon.cfile.domain.vo.TeacherTaskInfo;
import com.kanlon.cfile.service.TeacherService;
import com.kanlon.cfile.utli.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 老师或班委的控制类
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    /**
     * 创建提交任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    @PostMapping(value = "/task")
    public JsonResult<Void> createTask(@RequestBody TaskVO task) {
        return teacherService.createTask(task);
    }

    /**
     * 更新任务
     *
     * @param task 任务信息
     * @return 任务id
     */
    @PutMapping(value = "/task/{tid}")
    public JsonResult<Integer> modifyTask(@RequestBody TaskVO task, @PathVariable(value = "tid") Integer tid) {
        return teacherService.modifyTask(task, tid);
    }

    /**
     * 得到个人信息
     *
     * @return 返回教师或班委个人信息类
     */
    @GetMapping(value = "/center/info")
    public JsonResult<TeacherCenterVO> getCenterInfo() {
        return teacherService.getCenterInfo();
    }

    /**
     * 修改个人中心信息(暂时只能修改昵称)
     *
     * @param newUserInfo 新的用户信息
     * @return 是否成功
     */
    @PutMapping(value = "/center/info")
    public JsonResult<String> modifyCenterInfo(@RequestBody TeacherCenterVO newUserInfo) {
        return teacherService.modifyCenterInfo(newUserInfo);
    }

    /**
     * 获取所有发布的任务列表
     *
     * @param request 请求
     * @return 任务列表
     */
    @GetMapping(value = "/all/tasks")
    public JsonResult<List<TaskInfoListsVO>> getAllTasks(HttpServletRequest request) {
        return teacherService.getAllTasks(request);
    }

    /**
     * 得到任务信息
     *
     * @param tid 任务id
     * @return 任务信息
     */
    @GetMapping("/task/{tid}")
    public JsonResult<TeacherTaskInfo> getTaskInfo(@PathVariable(value = "tid") @NotNull Integer tid) {
        return teacherService.getTaskInfo(tid);
    }

    /**
     * 得到某个任务已经提交的人员的名单(即是文件名，在前端再取相应字段，文件名后端下载需要用到)
     *
     * @param tid 任务id
     * @return 名单列表
     */
    @GetMapping("/task/list/{tid}")
    public JsonResult<List<String>> getSubmitList(@PathVariable(value = "tid") @NotNull Integer tid) {
        return teacherService.getSubmitList(tid);
    }

    /**
     * 根据 任务id获取所有提交的文件的压缩包
     *
     * @param response 响应
     * @param tid      任务id
     */
    @GetMapping("/files/{tid}")
    public void getSubmitFileZip(HttpServletResponse response, @PathVariable Integer tid) {
        teacherService.getSubmitFileZip(response, tid);
    }

    /**
     * 根据 任务id和提交的学号姓名得到该文件
     *
     * @param response 响应
     * @param tid      任务id
     * @param filename 文件名 ，学号姓名
     */
    @GetMapping("/file/{tid}")
    public void getSubmitFile(HttpServletResponse response, @PathVariable(value = "tid") Integer tid,
                              @PathParam(value = "filename") String filename) {
        teacherService.getSubmitFile(response, tid, filename);
    }

    /**
     * 得到某个项目的链接
     *
     * @param tid 项目id
     * @return 项目链接
     */
    @GetMapping("/task/link/{tid}")
    public JsonResult<String> getTaskLink(@PathVariable(value = "tid") Integer tid) {
        return teacherService.getTaskLink(tid);
    }
}
