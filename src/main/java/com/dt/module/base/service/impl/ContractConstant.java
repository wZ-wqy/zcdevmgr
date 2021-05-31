package com.dt.module.base.service.impl;

import com.dt.core.common.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContractConstant extends BaseService {

    //已取消
    public static String STATUS_CANCEL = "cancel";
    //已完结
    public static String STATUS_FINISH = "finish";
    //等待审核
    public static String STATUS_WIATREVIEW = "Waitreview";
    //等待履约
    public static String STATUS_WAITPERFORMANCE = "Waitperformance";
    //履约中
    public static String STATUS_INPERFORMANCE = "Inperformance";
    //已解除
    public static String STATUS_RELEASED = "released";

}
