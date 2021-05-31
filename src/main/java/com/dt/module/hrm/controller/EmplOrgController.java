package com.dt.module.hrm.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.hrm.service.impl.EmplOrgService;
import com.dt.module.hrm.service.impl.EmplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class EmplOrgController extends BaseController {

    @Autowired
    private EmplOrgService emplOrgService;

    @Autowired
    private EmplService emplService;

    /**
     * @Description:添加组织
     */
    @ResponseBody
    @Acl(info = "添加组织节点")
    @RequestMapping("/hrm/orgNodeDelete.do")
    public R orgNodeDelete(String node_id) {
        return emplOrgService.deleteEmplOrg(node_id);
    }

    /**
     * @Description:查询组织
     */
    @RequestMapping("/hrm/queryEmplByOrg.do")
    @ResponseBody
    @Acl(info = "查询组织人员")
    public R queryEmplByOrg(String node_id) throws IOException {
        return emplService.queryEmplByOrg(node_id);
    }

    /**
     * @Description:保存节点
     */
    @RequestMapping("/hrm/orgNodeSave.do")
    @ResponseBody
    @Acl(info = "保存节点")
    @Transactional
    public R orgNodeSave() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String id = ps.getString("node_id");
        if (ToolUtil.isEmpty(id)) {
            return emplOrgService.addEmplOrg(ps);
        } else {
            return emplOrgService.updateEmplOrg(ps);
        }
    }

    /**
     * @Description:根据节点查询组织节点
     */
    @RequestMapping("/hrm/orgNodeQuery.do")
    @ResponseBody
    @Acl(info = "查询组织节点")
    public R orgNodeQuery(String node_id) {
        return emplOrgService.queryEmplOrgById(node_id);
    }

    /**
     * @Description:根据组织ID查询所以节点
     */
    @RequestMapping("/hrm/orgNodeTreeQuery.do")
    @ResponseBody
    @Acl(info = "查询组织")
    public R orgNodeTreeQuery(String org_id) {
        return emplOrgService.queryEmplOrgNodeTree(org_id);
    }

    /**
     * @Description:查询组织
     */
    @RequestMapping("/hrm/orgQueryLevelList.do")
    @ResponseBody
    @Acl(info = "查询组织")
    public R orgQueryLevelList() {
        return emplOrgService.queryEmplOrgLevelList();
    }


    /**
     * @Description:查询组织
     */
    @RequestMapping("/hrm/orgQuery.do")
    @ResponseBody
    @Acl(info = "查询组织")
    public R orgQuery() {
        return emplOrgService.queryEmplOrg();
    }

    /**
     * @Description:查询公司
     */
    @RequestMapping("/hrm/orgQueryCompany.do")
    @ResponseBody
    @Acl(info = "查询公司", value = Acl.ACL_USER)
    public R orgQueryCompany() {
        return emplOrgService.orgQueryCompany();
    }

    /**
     * @Description:查询部门
     */
    @RequestMapping("/hrm/orgQueryPartByCompany.do")
    @ResponseBody
    @Acl(info = "查询部门", value = Acl.ACL_USER)
    public R orgQueryPartByCompany(String id) {
        return emplOrgService.orgQueryPartByCompany(id);
    }

}
