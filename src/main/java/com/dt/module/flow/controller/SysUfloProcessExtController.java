
package com.dt.module.flow.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.flow.service.impl.SysUfloProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lank
 * @date: Nov 30, 2019 8:24:46 AM
 * @Description:
 */

@Controller
@RequestMapping("/api")
public class SysUfloProcessExtController extends BaseController {


    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    @Autowired
    SysUfloProcessService sysUfloProcessService;


    /**
     * @Description:启动流程
     * @param key
     * @param type
     */
    @RequestMapping("/flow/startProcess.do")
    @ResponseBody
    @Acl(info = "启动流程", value = Acl.ACL_USER)
    public R startProcess(String key, String type) {
        return sysUfloProcessService.startProcess(key, type);
    }

    /**
     * @Description:完成流程
     * @param opinion
     * @param taskId
     */
    @RequestMapping("/flow/completeTask.do")
    @ResponseBody
    @Acl(info = "完成流程", value = Acl.ACL_USER)
    public R computeTask(String taskId, String opinion) {
        return sysUfloProcessService.completeTask(taskId, opinion);
    }

    /**
     * @Description:取消流程
     * @param opinion
     * @param taskId
     */
    @RequestMapping("/flow/cancelTask.do")
    @ResponseBody
    @Acl(info = "取消流程", value = Acl.ACL_USER)
    public R cancelTask(String taskId, String opinion) {
        return sysUfloProcessService.cancelTask(taskId, opinion);
    }

    /**
     * @Description:加载流程数据
     * @param processInstanceId
     */
    @RequestMapping("/flow/loadProcessInstanceData.do")
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    public R loadProcessInstanceData(String processInstanceId) {
        return sysUfloProcessService.loadProcessTaskinfo(processInstanceId);
    }

    /**
     * @Description:加载流程任务
     * @param processInstanceId
     */
    @RequestMapping("/flow/loadProcessTaskinfo.do")
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    public R loadProcessTaskinfo(String processInstanceId) {
        return sysUfloProcessService.loadProcessTaskinfo(processInstanceId);
    }


}
