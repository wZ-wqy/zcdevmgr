package com.dt.module.flow.base;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.node.Node;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.flow.service.impl.SysUfloProcessService;
import com.dt.module.zc.service.impl.AssetsFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AssetsEndEventHandler extends BaseNodeEventHandler {


    private static Logger log = LoggerFactory.getLogger(AssetsEndEventHandler.class);


    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {
        log.info("###AssetsEndEventHandler Enter Node###");
        QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
        qw.eq("busid", processInstance.getBusinessId());
        SysProcessData r = SysProcessDataServiceImpl.getOne(qw);
        System.out.println("sp" + r.toString());
    }


    @Autowired
    @Lazy
    AssetsFlowService assetsFlowService;

    @Autowired
    SysUfloProcessService sysUfloProcessService;

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {
        log.info("###AssetsEndEventHandler Leave Node###" + processInstance.getProcessId());
        String dtlstatus = "";
        Long id=processInstance.getId();
        Long pid=processInstance.getParentId();
        dtlstatus = context.getProcessService().getProcessVariable("pstatusdtl", processInstance.getId()).toString();
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        UpdateWrapper<SysProcessData> uw = new UpdateWrapper<SysProcessData>();
        uw.set("pstatus", FlowConstant.PSTATUS_FINISH);
        uw.set("pendtime", nowtime);

        if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(dtlstatus)) {
            log.info("调用流程正常结束");
            uw.set("pstatusdtl", FlowConstant.PSTATUS_DTL_SUCCESS);
        } else if (FlowConstant.PSTATUS_DTL_FAILED.equals(dtlstatus)) {
            log.info("调用流程-拒绝");
            uw.set("pstatusdtl", FlowConstant.PSTATUS_DTL_FAILED);
        } else {
            dtlstatus = "";
            log.info("调用流程-结束状态未知。" + processInstance.getId());
        }
        if (!"".equals(dtlstatus)) {
            SysProcessDataServiceImpl.update(uw);
            if("0".equals(Long.toString(pid))){
                assetsFlowService.finishFlow(Long.toString(id));
            }else{
                assetsFlowService.finishFlow(Long.toString(pid));
            }

        }
        log.info("流程结束\n\n");
    }


}
