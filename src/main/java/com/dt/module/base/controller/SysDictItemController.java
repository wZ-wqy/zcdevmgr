package com.dt.module.base.controller;


import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysDictItem;
import com.dt.module.base.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
@Controller
@RequestMapping("/api/sysDictItem")
public class SysDictItemController extends BaseController {


    @Autowired
    ISysDictItemService SysDictItemServiceImpl;


    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_DENY)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.removeById(id));
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_DENY)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.getById(id));
    }

    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_DENY)
    @RequestMapping(value = "/insert.do")
    public R insert(SysDictItem entity) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.save(entity));
    }

    @ResponseBody
    @Acl(info = "根据Id更新", value = Acl.ACL_DENY)
    @RequestMapping(value = "/updateById.do")
    public R updateById(SysDictItem entity) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.updateById(entity));
    }

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_DENY)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(SysDictItem entity) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.saveOrUpdate(entity));
    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_DENY)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.list(null));
    }

    @ResponseBody
    @Acl(info = "根据Dict查询", value = Acl.ACL_DENY)
    @RequestMapping(value = "/selectDictItemByDict.do")
    public R selectDictItemByDict(String dictId) {
        return R.SUCCESS_OPER(SysDictItemServiceImpl.selectDictItemByDict(dictId));
    }


}

