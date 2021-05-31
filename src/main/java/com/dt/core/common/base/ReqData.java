package com.dt.core.common.base;

import java.util.Map;


/**
 * @author lank
 * @version 创建时间：2017年8月1日 上午8:33:12
 */
public class ReqData {
    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String toString() {
        return "{\"data\":" + data + "}";
    }
}
