package com.dt.module.om.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WebSshUtil {

    public static String getLastUrl(HttpServletRequest request) {
        String lastUrl = "/";
        if (request != null && request.getHeader("Referer") != null) {
            lastUrl = request.getHeader("Referer")
                    .replaceAll("http://" + request.getHeader("Host") + request.getContextPath(), "");
        }
        return lastUrl;
    }

    public static InputStream toJsonMessage(Object message) {
        InputStream is = null;
        try {
            try {
                is = new ByteArrayInputStream(JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                is = new ByteArrayInputStream("{\"code\":-1,\"msg\":\"system error.\"}".getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
        }
        return is;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
