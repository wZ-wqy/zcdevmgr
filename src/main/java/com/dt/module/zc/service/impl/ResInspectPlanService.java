package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.module.zc.entity.ResInspection;
import com.dt.module.zc.entity.ResInspectionPitem;
import com.dt.module.zc.entity.ResInspectionPlan;
import com.dt.module.zc.entity.ResInspectionUser;
import com.dt.module.zc.service.IResInspectionPitemService;
import com.dt.module.zc.service.IResInspectionPlanService;
import com.dt.module.zc.service.IResInspectionService;
import com.dt.module.zc.service.IResInspectionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResInspectPlanService extends BaseService {

    @Autowired
    IResInspectionService ResInspectionServiceImpl;

    @Autowired
    IResInspectionPlanService ResInspectionPlanServiceImpl;

    @Autowired
    IResInspectionPitemService ResInspectionPitemServiceImpl;

    @Autowired
    IResInspectionUserService ResInspectionUserServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;


    /**
     * @Description:创建巡检单据
     */
    public R createInspection(String busid) {

        String newbusid = assetsOperService.createUuid(AssetsConstant.UUID_XJ);
        QueryWrapper<ResInspectionPlan> qw = new QueryWrapper<ResInspectionPlan>();
        qw.eq("busid", busid);
        ResInspectionPlan obj = ResInspectionPlanServiceImpl.getOne(qw);

        //处理巡检人员
        JSONArray userarr = JSONArray.parseArray(obj.getActionusers());
        if (userarr == null || userarr.size() == 0) {
            return R.FAILURE("没有分配巡检用户");
        }
        List<ResInspectionUser> list2 = new ArrayList<ResInspectionUser>();
        for (int j = 0; j < userarr.size(); j++) {
            ResInspectionUser e = new ResInspectionUser();
            e.setBusid(newbusid);
            e.setUserid(userarr.getJSONObject(j).getString("user_id"));
            list2.add(e);
        }


        //获取原有对资产数据
        QueryWrapper<ResInspectionPitem> qw2 = new QueryWrapper<ResInspectionPitem>();
        qw2.eq("busid", busid);
        List<ResInspectionPitem> list = ResInspectionPitemServiceImpl.list(qw2);

        List<ResInspectionPitem> newlist = new ArrayList<ResInspectionPitem>();
        for (int i = 0; i < list.size(); i++) {
            ResInspectionPitem e = list.get(i);
            e.setId(null);
            e.setBusid(newbusid);
            e.setStatus("wait");
            e.setResid(e.getResid());
            e.setType("instance");
            newlist.add(e);
        }
        ResInspection inspection = new ResInspection();
        inspection.setMethod(obj.getMethod());
        inspection.setBusid(newbusid);
        inspection.setActionusers(obj.getActionusers());
        inspection.setMark(obj.getMark());
        inspection.setName(obj.getName());

        inspection.setStatus("wait");
        inspection.setCnt(new BigDecimal(newlist.size()));
        inspection.setActingcnt(new BigDecimal("0"));
        inspection.setNormalcnt(new BigDecimal("0"));
        inspection.setFaultcnt(new BigDecimal("0"));
        ResInspectionServiceImpl.save(inspection);
        ResInspectionUserServiceImpl.saveBatch(list2);
        if(newlist.size()>0&&"fix".equals(obj.getMethod())){
            ResInspectionPitemServiceImpl.saveBatch(newlist);
        }
        return R.SUCCESS_OPER();
    }
}
