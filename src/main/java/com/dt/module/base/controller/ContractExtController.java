package com.dt.module.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.Contract;
import com.dt.module.base.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping("/api/base/contract/ext")
public class ContractExtController extends BaseController {

    @Autowired
    IContractService ContractServiceImpl;

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<Contract> ew = new QueryWrapper<Contract>();
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ContractServiceImpl.list(ew));
    }

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(Contract entity) {
        if (ToolUtil.isEmpty(entity.getId())) {
            entity.setBusuuid("HT" + UUID.randomUUID().toString().substring(9, 23).toUpperCase());
        }
        return R.SUCCESS_OPER(ContractServiceImpl.saveOrUpdate(entity));
    }


}
