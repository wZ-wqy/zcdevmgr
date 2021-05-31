package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.module.cmdb.service.IResActionItemService;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.zc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetsFlowService extends BaseService {


    @Autowired
    ResAllocateService resAllocateService;

    @Autowired
    ResLoanreturnService resLoanreturnService;

    @Autowired
    ResCollectionreturnService resCollectionreturnService;

    @Autowired
    IResAllocateItemService ResAllocateItemServiceImpl;

    @Autowired
    IResActionItemService ResActionItemServiceImpl;

    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResScrapeItemService ResScrapeItemServiceImpl;

    @Autowired
    IResAllocateService ResAllocateServiceImpl;

    @Autowired
    IResCBasicinformationService ResCBasicinformationServiceImpl;

    @Autowired
    IResCBasicinformationItemService ResCBasicinformationItemServiceImpl;

    @Autowired
    IResCMaintenanceService ResCMaintenanceServiceImpl;

    @Autowired
    IResCMaintenanceItemService ResCMaintenanceItemServiceImpl;

    @Autowired
    IResCFinanceService ResCFinanceServiceImpl;

    @Autowired
    IResCFinanceItemService ResCFinanceItemServiceImpl;

    @Autowired
    IResCollectionreturnItemService ResCollectionreturnItemServiceImpl;

    @Autowired
    IResLoanreturnItemService ResLoanreturnItemServiceImpl;

    @Autowired
    IResResidualItemService ResResidualItemServiceImpl;

    @Autowired
    IResResidualService ResResidualServiceImpl;

    @Autowired
    ResCMaintenanceService resCMaintenanceService;

    @Autowired
    ResCFinanceService resCFinanceService;

    @Autowired
    ResCBasicinformationService resCBasicinformationService;

    @Autowired
    ResScrapeService resScrapeService;

    @Autowired
    ResPurchaseService resPurchaseService;

    @Autowired
    ResTranferService ResTranferService;

    /**
     * @Description:启动流程
     */
    public R startFlow(String pinst, String uuid, String type, String ifsp, JSONObject data) {
        if (type.equals(AssetsConstant.ASSETS_BUS_TYPE_LY)) {
            return resCollectionreturnService.startLyFlow(pinst, uuid, ifsp);
        } else if (type.equals(AssetsConstant.ASSETS_BUS_TYPE_TK)) {
            return resCollectionreturnService.startTkFlow(pinst, uuid, ifsp);
        } else if (type.equals(AssetsConstant.ASSETS_BUS_TYPE_JY)) {
            return resLoanreturnService.startLyFlow(pinst, uuid, ifsp);
        } else if (type.equals(AssetsConstant.ASSETS_BUS_TYPE_BF)) {
            return resScrapeService.startFlow(pinst, uuid, ifsp);
        } else if (type.equals(AssetsConstant.ASSETS_BUS_TYPE_DB)) {
            return resAllocateService.startFlow(pinst, uuid, ifsp);
        }else if(type.equals(AssetsConstant.ASSETS_BUS_TYPE_RES_PURCHASE)){
            return resPurchaseService.startFlow(pinst, uuid, ifsp);
        }else if(type.equals(AssetsConstant.ASSETS_BUS_TYPE_ZY)){
            return ResTranferService.startFlow(pinst, uuid, ifsp);
        }
        return R.SUCCESS();
    }

    /**
     * @Description:结束流程
     */
    public R finishFlow(String instid) {
        QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
        qw.eq("processinstanceid", instid);
        SysProcessData sd = SysProcessDataServiceImpl.getOne(qw);
        if (AssetsConstant.ASSETS_BUS_TYPE_LY.equals(sd.getPtype())) {
            return resCollectionreturnService.finishLyFlow(sd.getBusid(), sd.getPstatusdtl());
        } else if (AssetsConstant.ASSETS_BUS_TYPE_TK.equals(sd.getPtype())) {
            return resCollectionreturnService.finishTkFlow(sd.getBusid(), sd.getPstatusdtl());
        } else if (AssetsConstant.ASSETS_BUS_TYPE_JY.equals(sd.getPtype())) {
            return resLoanreturnService.finishJyFlow(sd.getBusid(), sd.getPstatusdtl());
        } else if (AssetsConstant.ASSETS_BUS_TYPE_BF.equals(sd.getPtype())) {
            return resScrapeService.finishFlow(sd.getBusid(), sd.getPstatusdtl());
        } else if (AssetsConstant.ASSETS_BUS_TYPE_DB.equals(sd.getPtype())) {
            return resAllocateService.finishFlow(sd.getBusid(), sd.getPstatusdtl());
        }else if(AssetsConstant.ASSETS_BUS_TYPE_RES_PURCHASE.equals(sd.getPtype())){
            return resPurchaseService.finishFlow(sd.getBusid(), sd.getPstatusdtl());
        }else if(AssetsConstant.ASSETS_BUS_TYPE_ZY.equals(sd.getPtype())){
            return ResTranferService.finishFlow(sd.getBusid(), sd.getPstatusdtl());
        }
        return R.SUCCESS();
    }
}
