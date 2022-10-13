package com.kanlon.cfile.controller;

import com.kanlon.cfile.domain.vo.StudentSubmitFileVO;
import com.kanlon.cfile.domain.vo.StudentTaskInfoVO;
import com.kanlon.cfile.service.TeacherUserService;
import com.kanlon.cfile.utli.IpUtil;
import com.kanlon.cfile.utli.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 学生的提交的controller
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private TeacherUserService teacherUserService;
    @Resource
    private HttpServletRequest httpServletRequest;

    /**
     * 学生提交文件
     *
     * @param submitVO 提交的信息
     * @param uid      用户id
     * @param tid      任务id
     * @return 上传文件的路径
     */
    @RequestMapping(value = "/submit/{uid}/{tid}", method = {RequestMethod.POST})
    public JsonResult<String> submitFile(@ModelAttribute StudentSubmitFileVO submitVO, @PathVariable(value = "uid") Integer uid, @PathVariable(value = "tid") Integer tid) {
        return teacherUserService.submitFile(submitVO, uid, tid, IpUtil.getRealIP(httpServletRequest));
    }

    /**
     * 得到任务信息
     *
     * @param tid 任务id
     * @return 学生信息
     */
    @GetMapping("/task/{tid}")
    public JsonResult<StudentTaskInfoVO> getTaskInfo(@PathVariable(value = "tid") @NotNull Integer tid) {
        return teacherUserService.getTaskInfo(tid);
    }

    /**
     * 得到已经提交了人的学号
     *
     * @param uid 用户id
     * @param tid 任务id
     * @return 学号列表
     */
    @GetMapping("/task/list/{uid}/{tid}")
    public JsonResult<List<String>> getSubmittingList(@PathVariable @NotNull Integer uid, @PathVariable @NotNull Integer tid) {
        return teacherUserService.getSubmittingList(uid, tid);
    }

}
