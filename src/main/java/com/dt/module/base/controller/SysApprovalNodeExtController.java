package com.dt.module.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.entity.SysApprovalNode;
import com.dt.module.base.service.ISysApprovalNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.tools.Tool;
import java.util.List;

@Controller
@RequestMapping("/api/base/sysApprovalNode/ext")
public class SysApprovalNodeExtController extends BaseController {
    @Autowired
    ISysApprovalNodeService SysApprovalNodeServiceImpl;

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")

    public R insertOrUpdate(SysApprovalNode entity) {
        String code=entity.getCode();
        if(ToolUtil.isEmpty(code)){
            return R.FAILURE("编码不能为空");
        }
        QueryWrapper<SysApprovalNode> q=new QueryWrapper<SysApprovalNode>();
        q.eq("code",code);
        List<SysApprovalNode> codelist=SysApprovalNodeServiceImpl.list(q);
        if(ToolUtil.isEmpty(entity.getId())){
            //新插
            if(codelist.size()!=0){
                return R.FAILURE("当前编码重复");
            }
        }else{
            //更新
            if(!entity.getCode().equals(SysApprovalNodeServiceImpl.getById(entity.getId()).getCode())){
            }
        }
        return R.SUCCESS_OPER(SysApprovalNodeServiceImpl.saveOrUpdate(entity));
    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<SysApprovalNode> q=new QueryWrapper<>();
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        q.like(ToolUtil.isNotEmpty(search),"node",search);
        return R.SUCCESS_OPER(SysApprovalNodeServiceImpl.list(q));
    }



}
