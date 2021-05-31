package com.dt.core.common.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: lank
 * @date: 2017年11月15日 上午8:19:08
 * @Description:
 */
public enum BaseCodeMsgEnum {

    SUCCESS_DEF_MSG(0, "成功"),
    SUCCESS_OPER_MSG(0, "操作成功"),
    SUCCESS_SAVE_MSG(0, "保存成功"),

    FAILED_DEF_MSG(1, "失败"),
    FAILED_OPER_MSG(2, "操作失败"),
    FAILED_SAVE_MSG(3, "保存失败"),
    FAILED_NO_DATA_MSG(4, "无数据"),

    USER_LOGIN_SUCCESS(0, "用户登录成功"),
    USER_ALREADY_LOGIN(290, "用户已登录"),
    USER_NOT_LOGIN(299, "用户未登录"),
    USER_ALREADY_REG(297, "该用户已经注册"),
    USER_NOT_EXISTED(295, "没有此用户"),
    USER_ACCOUNT_FREEZED(294, "账号被冻结"),
    USER_OLD_PWD_NOT_RIGHT(
            293, "原密码不正确"),
    USER_TWO_PWD_NOT_MATCH(292, "两次输入密码不一致"),
    USER_QUERY_FAILED(291, "获取用户失败"),

    SYSTEM_CONF_ERROR(2000, "系统配置错误"),
    SYSTEM_RUNNING_ERROR(2001, "系统运行异常"),
    SYSTEM_UNKNOW_ERROR(2222, "系统未知错误"),

    NO_PERMITION(5000, "无权限"),
    REQ_PARAM_ERROR(5001, "请求参数不正确"),
    WX_FAILED_GET_OPENID(10001, "微信未获取Openid");

    private int friendlyCode;

    private String friendlyMsg;

    private String urlPath;

    BaseCodeMsgEnum(int code, String message) {
        this.friendlyCode = code;
        this.friendlyMsg = message;
    }

    BaseCodeMsgEnum(int code, String message, String urlPath) {
        this.friendlyCode = code;
        this.friendlyMsg = message;
        this.urlPath = urlPath;
    }

    public static JSONObject queryAll() {
        JSONObject res = new JSONObject();
        for (BaseCodeMsgEnum e : BaseCodeMsgEnum.values()) {
            res.put(e.friendlyCode + "", e.friendlyMsg);
        }
        res.put("0", "操作成功");
        return res;

    }

    public JSONArray printAll() {
        JSONArray res = new JSONArray();
        for (BaseCodeMsgEnum e : BaseCodeMsgEnum.values()) {
            JSONObject obj = new JSONObject();
            obj.put(e.friendlyCode + "", e.friendlyMsg);
            res.add(obj);
        }
        return res;
    }

    public int getCode() {
        return friendlyCode;
    }

    public void setCode(int code) {
        this.friendlyCode = code;
    }

    public String getMessage() {
        return friendlyMsg;
    }

    public void setMessage(String message) {
        this.friendlyMsg = message;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

}
