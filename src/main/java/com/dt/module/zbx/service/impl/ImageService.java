package com.dt.module.zbx.service.impl;


import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:未使用
     */
    public R getOne(String itemid, String start, String end) {
        return R.SUCCESS_OPER();
    }
}
