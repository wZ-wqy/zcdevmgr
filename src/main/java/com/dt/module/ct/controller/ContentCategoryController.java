package com.dt.module.ct.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.ct.service.impl.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lank
 * @date: 2017年8月11日 上午11:29:31
 * @Description:
 */
@Controller
@RequestMapping(value = "/api")
public class ContentCategoryController extends BaseController {

    @Autowired
    ContentCategoryService contentCategoryService;

    /**
     * @Description:根据Id查询根类目节点
     */
    @ResponseBody
    @Acl(info = "根据Id查询根类目节点")
    @RequestMapping(value = "/ctCategroy/queryCategoryById.do")
    public R queryCategoryById(String id) {
        return contentCategoryService.queryCategoryById(id);
    }

    /**
     * @Description:删除类目
     */
    @ResponseBody
    @Acl(info = "删除类目")
    @RequestMapping(value = "/ctCategroy/deleteCategory.do")
    public R deleteCategory(String id) {
        return contentCategoryService.deleteCategory(id);
    }

    /**
     * @Description:添加类目
     */
    @ResponseBody
    @Acl(info = "添加类目")
    @RequestMapping(value = "/ctCategroy/addCategory.do")
    public R addCategory() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return contentCategoryService.addCategory(ps);
    }

    /**
     * @Description:更新类目
     */
    @ResponseBody
    @Acl(info = "更新类目")
    @RequestMapping(value = "/ctCategroy/updateCategory.do")
    public R updateCategory() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return contentCategoryService.updateCategory(ps);
    }

    @ResponseBody
    @Acl(info = "查询类目")
    @RequestMapping(value = "/ctCategroy/queryCategory.do")
    public R queryCategory(String root) {
        return contentCategoryService.queryCategory(root);
    }

    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "显示类目所有子节点数据")
    @RequestMapping(value = "/ctCategroy/queryCategoryChildren.do")
    public R queryCategoryChildren() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String parentId = ps.getString("parent_id");
        String isAction = ps.getString("is_action");
        return contentCategoryService.queryCategoryChildren(parentId, isAction);
    }

    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "显示类目子节点数据")
    @RequestMapping(value = "/ctCategroy/queryCategoryFirstFloor.do")
    public R queryCategoryFirstFloor() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String rootId = ps.getString("root");
        String isAction = ps.getString("is_action");
        return contentCategoryService.queryCategoryFirstFloor(rootId, isAction);
    }

    @ResponseBody
    @Acl(info = "显示类目节点,后端angular显示内容")
    @RequestMapping(value = "/ctCategroy/queryCategoryTreeList.do")
    public R queryCategoryTreeList(String root) {
        return contentCategoryService.queryCategoryTreeList(root);
    }
}
