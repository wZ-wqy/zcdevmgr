package com.dt.module.zc.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.zc.entity.ResResidual;
import com.dt.module.zc.entity.ResResidualItem;
import com.dt.module.zc.entity.ResResidualStrategy;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResResidualItemService;
import com.dt.module.zc.service.IResResidualService;
import com.dt.module.zc.service.IResResidualStrategyService;
import com.dt.module.zc.service.impl.ResResidualService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-08-04
 */
@Controller
@RequestMapping("/api/zc/resResidual/ext")
public class ResResidualExtController extends BaseController {


    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;


    @Autowired
    IResResidualService ResResidualServiceImpl;

    @Autowired
    IResResidualStrategyService ResResidualStrategyServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;


    @Autowired
    ResResidualService resResidualService;

    @Autowired
    IResResidualItemService ResResidualItemServiceImpl;

    @Autowired
    IResService ResServiceImpl;


    /**
     * @Description:删除折旧单据
     */
    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResResidual obj = ResResidualServiceImpl.getById(id);
        if (ResResidualService.STATUS_SUCCESS.equals(obj.getStatus())) {
            return R.FAILURE("当前状态无法删除");
        } else {
            return R.SUCCESS_OPER(ResResidualServiceImpl.removeById(id));
        }
    }

    /**
     * @Description:查询折旧单据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResResidual> ew = new QueryWrapper<ResResidual>();
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResResidualServiceImpl.list(ew));
    }

    /**
     * @Description:生产折旧单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResResidual entity, String items) {

        JSONArray items_arr = JSONArray.parseArray(items);
        ResResidualStrategy stragegy = ResResidualStrategyServiceImpl.getById(entity.getStrategyid());
        entity.setDepreciationrate(stragegy.getDepreciationrate());
        entity.setResidualvaluerate(stragegy.getResidualvaluerate());
        entity.setCnt(new BigDecimal(items_arr.size()));
        entity.setCheckstatus(ResResidualService.CKSTATUS_INIT);
        entity.setStatus(ResResidualService.STATUS_WAIT);
        entity.setBusidate(new Date());
        String uuid = assetsOperService.createUuid(AssetsConstant.UUID_ZJ);
        entity.setUuid(uuid);
        ArrayList<ResResidualItem> list = new ArrayList<ResResidualItem>();
        for (int i = 0; i < items_arr.size(); i++) {
            ResResidualItem e = new ResResidualItem();
            e.setUuid(uuid);
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            String buyprice = items_arr.getJSONObject(i).getString("buy_price");
            String networth = items_arr.getJSONObject(i).getString("net_worth");
            //设置残值
            String residualvalue = items_arr.getJSONObject(i).getString("residualvalue");
            JSONObject dep = computingDepreciation(stragegy, residualvalue, buyprice, networth);
            e.setBuyprice(new BigDecimal(buyprice));
            e.setBnetworth(new BigDecimal(networth));
            e.setAnetworth(new BigDecimal(dep.getString("networth")));
            e.setLossprice(new BigDecimal(dep.getString("lossprice")));
            e.setCurresidualvalue(new BigDecimal(dep.getString("residualvalue")));
            e.setCheckstatus(ResResidualService.ITEMCHECKSTATUS_INIT);
            list.add(e);
        }
        ResResidualServiceImpl.save(entity);
        ResResidualItemServiceImpl.saveBatch(list);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:同步折旧数据
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/actionSysData.do")
    public R actionSysData(String id) {
        ResResidual obj = ResResidualServiceImpl.getById(id);
        if (ResResidualService.STATUS_WAIT.equals(obj.getStatus()) && ResResidualService.CKSTATUS_SUCCESS.equals(obj.getCheckstatus())) {
            resResidualService.confirm(obj.getUuid());
        } else {
            return R.FAILURE("当前状态异常,无法操作");
        }
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:检查资产折旧数据
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/checkDataById.do")
    public R checkDataById(String id) {
        ResResidual obj = ResResidualServiceImpl.getById(id);
        QueryWrapper<ResResidualItem> ew = new QueryWrapper<ResResidualItem>();
        ew.eq("uuid", obj.getUuid());
        List<ResResidualItem> list = ResResidualItemServiceImpl.list(ew);
        List<ResResidualItem> list2 = new ArrayList<ResResidualItem>();
        String ckstatus = ResResidualService.CKSTATUS_SUCCESS;
        for (int i = 0; i < list.size(); i++) {
            ResResidualItem e = list.get(i);
            if (e.getLossprice().compareTo(new BigDecimal("0")) >= 0 && e.getAnetworth().compareTo(new BigDecimal("0")) >= 0) {
                e.setCheckstatus(ResResidualService.ITEMCHECKSTATUS_SUCCESS);
            } else {
                e.setCheckstatus(ResResidualService.ITEMCHECKSTATUS_FAILED);
                ckstatus = ResResidualService.CKSTATUS_FAILED;
            }
            list2.add(e);
        }
        UpdateWrapper<ResResidual> ups = new UpdateWrapper<ResResidual>();
        ups.set("checkstatus", ckstatus);
        ups.eq("id", obj.getId());
        ResResidualServiceImpl.update(ups);
        ResResidualItemServiceImpl.saveOrUpdateBatch(list2);
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:计算折旧
     */
    public JSONObject computingDepreciation(ResResidualStrategy stragegy, String residualvalue, String buyprice, String bnetworth) {
        System.out.println(stragegy);
        System.out.println("residualvalue:" + residualvalue);
        System.out.println("buyprice:" + buyprice);
        System.out.println("bnetworth:" + bnetworth);
        BigDecimal residualvaluenumber = ConvertUtil.toBigDecimal(residualvalue, new BigDecimal("0"));
        BigDecimal bnetworthnumber = ConvertUtil.toBigDecimal(bnetworth, new BigDecimal("0"));
        BigDecimal buypricenumber = ConvertUtil.toBigDecimal(buyprice, new BigDecimal("0"));
        BigDecimal residualvaluerate = stragegy.getResidualvaluerate();
        BigDecimal depreciationrate = stragegy.getDepreciationrate();
        //
        if (residualvaluerate.compareTo(new BigDecimal("-1")) == 0) {
            //如果,residualvaluerate=-1,则使用设置的残值
        } else if (residualvaluerate.compareTo(new BigDecimal("0")) > 0) {
            //残值率>0,则采购价*残值率
            residualvaluenumber = buypricenumber.multiply(residualvaluerate);
        } else {
            residualvaluenumber = new BigDecimal("0");
        }

        BigDecimal lossprice = buypricenumber.subtract(residualvaluenumber).multiply(depreciationrate);

        JSONObject res = new JSONObject();
        res.put("residualvalue", residualvaluenumber);
        res.put("networth", bnetworthnumber.subtract(lossprice));
        res.put("lossprice", lossprice);
        return res;
    }


}

