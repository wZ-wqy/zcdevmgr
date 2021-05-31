package com.dt.module.zc.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResPurchase;
import com.dt.module.zc.service.IResPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dt.core.common.base.R;
@Service
public class ResPurchaseService extends BaseService {


    @Autowired
    IResPurchaseService ResPurchaseServiceImpl;

    /**
     * @Description:开启采购流程
     */
    public R startFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResPurchase> ups = new UpdateWrapper<ResPurchase>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("busid", uuid);
            ResPurchaseServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResPurchase> ups = new UpdateWrapper<ResPurchase>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("busid", uuid);
            ResPurchaseServiceImpl.update(ups);
            confirm(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消采购
     */
    public R cancel(String busid, String status) {
        UpdateWrapper<ResPurchase> ups = new UpdateWrapper<ResPurchase>();
        ups.set("status", status);
        ups.eq("busid", busid);
        ResPurchaseServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:采购确认
     */
    public R confirm(String busid, String status) {
        UpdateWrapper<ResPurchase> ups = new UpdateWrapper<ResPurchase>();
        ups.set("status", status);
        ups.eq("busid", busid);
        ResPurchaseServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:结束采购流程
     */
    public R finishFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancel(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirm(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }


}
