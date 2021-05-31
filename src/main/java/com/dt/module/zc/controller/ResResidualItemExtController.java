package com.dt.module.zc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zc.entity.ResResidual;
import com.dt.module.zc.entity.ResResidualItem;
import com.dt.module.zc.service.IResResidualItemService;
import com.dt.module.zc.service.IResResidualService;
import com.dt.module.zc.service.impl.ResResidualService;
import com.dt.module.zc.service.impl.AssetsConstant;
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
 * @since 2020-08-03
 */
@Controller
@RequestMapping("/api/zc/resResidualItem/ext")
public class ResResidualItemExtController extends BaseController {


    @Autowired
    IResResidualItemService ResResidualItemServiceImpl;

    @Autowired
    IResResidualService ResResidualServiceImpl;

    /**
     * @Description:删除单条折旧资产
     */
    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResResidualItem item = ResResidualItemServiceImpl.getById(id);
        QueryWrapper<ResResidual> ew = new QueryWrapper<ResResidual>();
        ew.and(i -> i.eq("uuid", item.getUuid()));
        ResResidual obj = ResResidualServiceImpl.getOne(ew);
        if (ResResidualService.STATUS_SUCCESS.equals(obj.getStatus())) {
            return R.FAILURE("当前状态已完成,不允许删除");
        }
        //删除
        ResResidualItemServiceImpl.removeById(id);
        //更新主数据
        UpdateWrapper<ResResidual> ups = new UpdateWrapper<ResResidual>();
        ups.setSql("cnt=cnt-1");
        ups.eq("id", obj.getId());
        ResResidualServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:根据业务ID获取数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListByUuid.do")
    public R selectListByUuid(String uuid) {
        String sql = "select " + AssetsConstant.resSqlbody + " t.* , item.*,t.uuid zcuuid from res t,res_residual_item item where item.dr='0' and t.id=item.resid and item.uuid=?";
        return R.SUCCESS_OPER(db.query(sql, uuid).toJsonArrayWithJsonObject());

    }


}

