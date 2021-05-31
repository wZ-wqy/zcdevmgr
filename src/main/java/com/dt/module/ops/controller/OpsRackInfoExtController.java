package com.dt.module.ops.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.entity.SysDictItem;
import com.dt.module.base.service.ISysDictItemService;
import com.dt.module.ops.entity.OpsRackInfo;
import com.dt.module.ops.service.IOpsRackInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/ops/opsRackInfo/ext")
public class OpsRackInfoExtController extends BaseController {

    @Autowired
    IOpsRackInfoService OpsRackInfoServiceImpl;

    @Autowired
    ISysDictItemService SysDictItemServiceImpl;

    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        QueryWrapper<SysDictItem> u=new QueryWrapper<>();
        u.eq("dict_id","devrack");
        u.eq("dict_item_id",id);
        SysDictItemServiceImpl.remove(u);
        OpsRackInfoServiceImpl.removeById(id);
        return R.SUCCESS_OPER();
    }


    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(OpsRackInfo entity) {
        String id=entity.getId();
        SysDictItem item=new SysDictItem();
        item.setCode(entity.getCode());
        item.setName(entity.getName());
        item.setDictId("devrack");
        if(ToolUtil.isEmpty(id)){
            //新增
            String iid=ToolUtil.getUUID();
            item.setDictItemId(iid);
            entity.setId(iid);
        }else{
            //保存
            item.setDictItemId(entity.getId());
        }
        SysDictItemServiceImpl.saveOrUpdate(item);
        OpsRackInfoServiceImpl.saveOrUpdate(entity);
        return R.SUCCESS_OPER();
    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        QueryWrapper<OpsRackInfo> q=new QueryWrapper<>();
        q.like(ToolUtil.isNotEmpty(search),"name",search);
        q.orderByDesc("name");
        return R.SUCCESS_OPER(OpsRackInfoServiceImpl.list(q));
    }

}
