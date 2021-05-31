package com.dt.module.base.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class ScheduleController extends BaseController {

    @Autowired
    private JobService jobService = null;

    /**
     * 查询任务
     */
    @RequestMapping("/schedule/queryJobs.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询任务")
    public Object queryJobs(String type) {
        return R.SUCCESS_OPER(jobService.queryJob(type, getUserId()));
    }

    /**
     * 删除任务
     */
    @RequestMapping("/schedule/removejob.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "删除任务")
    public R removejob() {
        return R.SUCCESS_OPER();
    }

    /**
     * 暂停任务
     */
    @RequestMapping("/schedule/pausejob.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "暂停任务")
    public R pausejob(String seq) {
        if (ToolUtil.isEmpty(seq)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        jobService.pausejob(seq);
        return R.SUCCESS_OPER();
    }

    /**
     * 挂起任务
     */
    @RequestMapping("/schedule/resumejob.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "挂起任务")
    public R resumejob(String seq) {
        if (ToolUtil.isEmpty(seq)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        jobService.resumejob(seq);
        return R.SUCCESS_OPER();
    }

    /**
     * 激活任务
     */
    @RequestMapping("/schedule/enablejob.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "激活任务")
    public R enablejob(String seq) {
        if (ToolUtil.isEmpty(seq)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        jobService.enableJob(seq);
        return R.SUCCESS_OPER();
    }

    /**
     * 不激活任务
     */
    @RequestMapping("/schedule/disablejob.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "不激活任务")
    public R disablejob(String seq) throws IOException {
        if (ToolUtil.isEmpty(seq)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        jobService.disabledJob(seq);
        return R.SUCCESS_OPER();
    }

    /**
     * 运行一次任务
     */
    @RequestMapping("/schedule/runonce.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY,info = "运行一次任务")
    public R runonce(String seq) throws IOException {
        if (ToolUtil.isEmpty(seq)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        jobService.runoncejob(seq);
        return R.SUCCESS_OPER();
    }
}
