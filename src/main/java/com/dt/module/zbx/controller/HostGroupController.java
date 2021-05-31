package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zbx.service.impl.HostGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/hostgroup")
public class HostGroupController extends BaseController {

    @Autowired
    HostGroupService hostGroupService;

    /**
     * @Description:获取所有主机组
     */
    @ResponseBody
    @Acl(info = "获取所有主机组", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAllHostGroups.do")
    public R getAllHostGroups(String groups) {
        return hostGroupService.getAllHostGroups(groups);
    }

    /**
     * @Description:获取所有主机组
     */
    @ResponseBody
    @Acl(info = "获取所有主机组", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAllHostGroupsList.do")
    public R getAllHostGroupsList(String groups) {
        return hostGroupService.getAllHostGroupsList(groups);
    }

    /**
     * @Description:根据树形格式获取主机组
     */
    @ResponseBody
    @Acl(info = "根据树形格式获取主机组", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAllHostGroupsListFormatTree.do")
    public R getAllHostGroupsListFormatTree(String groups) {
        return hostGroupService.getAllHostGroupsListFormatTree(groups);
    }

    /**
     * @Description:添加主机组
     */
    @ResponseBody
    @Acl(info = "添加主机组", value = Acl.ACL_USER)
    @RequestMapping(value = "/addHostGroup.do")
    public R addHostGroup(String name) {
        if (ToolUtil.isEmpty(name)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        return hostGroupService.addHostGroup(name);
    }

    /**
     * @Description:更新主机组
     */
    @ResponseBody
    @Acl(info = "更新主机组", value = Acl.ACL_USER)
    @RequestMapping(value = "/updateHostGroup.do")
    public R updateHostGroup(String name, String groupid) {
        if (ToolUtil.isOneEmpty(name, groupid)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        return hostGroupService.updateHostGroup(name, groupid);
    }

}
