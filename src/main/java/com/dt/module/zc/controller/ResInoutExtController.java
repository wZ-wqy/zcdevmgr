package com.dt.module.zc.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.zc.entity.ResInout;
import com.dt.module.zc.entity.ResInoutItem;
import com.dt.module.zc.service.IResInoutItemService;
import com.dt.module.zc.service.IResInoutService;
import com.dt.module.zc.service.impl.ResInoutService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-05-25
 */
@Controller
@RequestMapping("/api/zc/resInout/ext")
public class ResInoutExtController extends BaseController {


    @Autowired
    IResInoutService ResInoutServiceImpl;

    @Autowired
    ResInoutService resInoutService;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    IResInoutItemService ResInoutItemServiceImpl;


    /**
     * @Description:出入库单据
     */
    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResInout entity, String busitimestr, String items) throws ParseException {
        ArrayList<Res> cols = new ArrayList<Res>();
        ArrayList<ResInoutItem> cols2 = new ArrayList<ResInoutItem>();
        String uuid = assetsOperService.createUuid(entity.getAction());
        entity.setUuid(uuid);
        entity.setStatus("none");
        if (ToolUtil.isEmpty(entity.getOperuserid())) {
            entity.setOperuserid(this.getUserId());
        }
        if (ToolUtil.isEmpty(entity.getOperusername())) {
            entity.setOperusername(this.getUserName());
        }
        JSONArray items_arr = JSONArray.parseArray(items);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(busitimestr);
        entity.setBusidate(date);
        if (AssetsConstant.UUID_HCRK.equals(entity.getAction())) {

            for (int i = 0; i < items_arr.size(); i++) {
                //当前无审批,入库
                Res e = new Res();
                e.setUuid(uuid);
                e.setBatchno(items_arr.getJSONObject(i).getString("batchno"));
                e.setCrkstatus("none");
                e.setRecycle(AssetsRecycleEnum.RECYCLE_IDLE.getValue());
                e.setZcCnt(new BigDecimal(items_arr.getJSONObject(i).getString("zc_cnt")));
                e.setBuyPrice(new BigDecimal(items_arr.getJSONObject(i).getString("buy_price")));
                e.setLoc(items_arr.getJSONObject(i).getString("loc"));
                e.setWarehouse(items_arr.getJSONObject(i).getString("warehouse"));
                e.setSupplier(items_arr.getJSONObject(i).getString("supplier"));
                e.setClassId(items_arr.getJSONObject(i).getString("class_id"));
                e.setCategory(items_arr.getJSONObject(i).getString("category"));
                e.setBelongCompanyId(items_arr.getJSONObject(i).getString("belong_company_id"));
                cols.add(e);

                //单据
                ResInoutItem e2 = new ResInoutItem();
                e2.setUuid(uuid);
                e2.setBatchno(items_arr.getJSONObject(i).getString("batchno"));
                e2.setCrkstatus("none");
                e2.setZcCnt(new BigDecimal(items_arr.getJSONObject(i).getString("zc_cnt")));
                e2.setBuyPrice(new BigDecimal(items_arr.getJSONObject(i).getString("buy_price")));
                e2.setLoc(items_arr.getJSONObject(i).getString("loc"));
                e2.setSupplier(items_arr.getJSONObject(i).getString("supplier"));
                e2.setWarehouse(items_arr.getJSONObject(i).getString("warehouse"));
                e2.setClassId(items_arr.getJSONObject(i).getString("class_id"));
                e2.setCategory(items_arr.getJSONObject(i).getString("category"));
                e2.setBelongCompanyId(items_arr.getJSONObject(i).getString("belong_company_id"));
                e2.setBuyTime(date);
                cols2.add(e2);
            }


        } else if (AssetsConstant.UUID_HCCK.equals(entity.getAction())) {
            for (int i = 0; i < items_arr.size(); i++) {
                //当前无审批,减值
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.setSql("zc_cnt=zc_cnt-" + items_arr.getJSONObject(i).getString("zc_cnt"));
                ups.eq("id", items_arr.getJSONObject(i).getString("id"));
                ResServiceImpl.update(ups);

                //单据明细
                ResInoutItem e2 = new ResInoutItem();
                e2.setCrkstatus("none");
                e2.setUuid(uuid);
                e2.setResid(items_arr.getJSONObject(i).getString("id"));
                e2.setZcCnt(new BigDecimal(items_arr.getJSONObject(i).getString("zc_cnt")));
                e2.setBuyPrice(new BigDecimal(items_arr.getJSONObject(i).getString("buy_price")));
                e2.setLoc(items_arr.getJSONObject(i).getString("loc"));
                e2.setSupplier(items_arr.getJSONObject(i).getString("supplier"));
                e2.setWarehouse(items_arr.getJSONObject(i).getString("warehouse"));
                e2.setClassId(items_arr.getJSONObject(i).getString("class_id"));
                e2.setCategory(items_arr.getJSONObject(i).getString("category"));
                e2.setBelongCompanyId(items_arr.getJSONObject(i).getString("belong_company_id"));
                e2.setBuyTime(date);
                cols2.add(e2);
            }

        } else if (AssetsConstant.UUID_HCDB.equals(entity.getAction())) {
            for (int i = 0; i < items_arr.size(); i++) {

                //出库
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.setSql("zc_cnt=zc_cnt-" + items_arr.getJSONObject(i).getString("zc_cnt"));
                ups.eq("id", items_arr.getJSONObject(i).getString("id"));
                ResServiceImpl.update(ups);

                //进库
                Res e = new Res();
                e.setUuid(uuid);
                e.setBatchno(items_arr.getJSONObject(i).getString("batchno"));
                e.setCrkstatus("none");
                e.setRecycle(AssetsRecycleEnum.RECYCLE_IDLE.getValue());
                e.setZcCnt(new BigDecimal(items_arr.getJSONObject(i).getString("zc_cnt")));
                e.setBuyPrice(new BigDecimal(items_arr.getJSONObject(i).getString("buy_price")));
                e.setSupplier(items_arr.getJSONObject(i).getString("supplier"));
                e.setClassId(items_arr.getJSONObject(i).getString("class_id"));
                e.setCategory(items_arr.getJSONObject(i).getString("category"));
                e.setLoc(entity.getInloc());
                e.setWarehouse(entity.getInwarehouse());
                e.setBelongCompanyId(entity.getBelongcompid());
                cols.add(e);


                //单据明细
                ResInoutItem e2 = new ResInoutItem();
                e2.setUuid(uuid);
                e2.setResid(items_arr.getJSONObject(i).getString("id"));
                e2.setCrkstatus("none");
                e2.setClassId(items_arr.getJSONObject(i).getString("class_id"));
                e2.setCategory(items_arr.getJSONObject(i).getString("category"));
                e2.setZcCnt(new BigDecimal(items_arr.getJSONObject(i).getString("zc_cnt")));
                e2.setBelongCompanyId(entity.getCompid());
                e2.setLoc(entity.getLoc());
                e2.setWarehouse(entity.getWarehouse());
                cols2.add(e2);
            }

        }
        entity.setCnt(new BigDecimal(items_arr.size()));
        System.out.println(entity);
        ResInoutServiceImpl.saveOrUpdate(entity);
        if (cols.size() > 0) {
            ResServiceImpl.saveOrUpdateBatch(cols);
        }
        if (cols2.size() > 0) {
            ResInoutItemServiceImpl.saveOrUpdateBatch(cols2);
        }

        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询耗材统计
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectHcTj.do")
    public R selectHcTj(String loc) {
        return resInoutService.selectHcTj(loc);
    }


    /**
     * @Description:查询耗材数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList(String type, String action) {
        if (AssetsConstant.UUID_HCCK.equals(action) || AssetsConstant.UUID_HCDB.equals(action)) {
            return resInoutService.selectHcCk(action);
        } else if (AssetsConstant.UUID_HCRK.equals(action)) {
            QueryWrapper<ResInout> qw = new QueryWrapper<ResInout>();
            qw.and(i -> i.eq("type", type));
            qw.and(i -> i.eq("action", action));
            qw.orderByDesc("create_time");
            return R.SUCCESS_OPER(ResInoutServiceImpl.list(qw));
        }

        return R.SUCCESS_OPER();
    }


    /**
     * @Description:根据ID查询耗材
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(String id) {
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:根据耗材安全库存
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectSafetyStore.do")
    public R selectSafetyStore() {
        return resInoutService.selectSafetyStore();
    }


    /**
     * @Description:根据耗材入库
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectHcInDataById.do")
    public R selectHcInDataById(String id) {
        return resInoutService.selectHcInDataById(id);
    }

    /**
     * @Description:根据耗材出库
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectHcOutDataById.do")
    public R selectHcOutDataById(String id) {
        return resInoutService.selectHcOutDataById(id);
    }

    /**
     * @Description:根据耗材调拨
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectHcDbDataById.do")
    public R selectHcDbDataById(String id) {
        return resInoutService.selectHcDbDataById(id);
    }


}

