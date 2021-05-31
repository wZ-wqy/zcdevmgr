package com.dt.module.om.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: lank
 * @date: 2017年12月25日 下午9:58:55
 * @Description:
 */
public class RemoteShellResult {

    private static Logger log = LoggerFactory.getLogger(RemoteShellResult.class);
    public int code;
    public StringBuffer result = new StringBuffer();

    public static RemoteShellResult setData(int code, StringBuffer result) {
        RemoteShellResult res = new RemoteShellResult();
        res.result = result;
        res.code = code;
        return res;
    }

    public void print() {
        log.info("code:\n" + code);
        log.info("result:\n" + result);
    }

}
