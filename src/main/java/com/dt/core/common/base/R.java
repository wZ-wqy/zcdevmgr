package com.dt.core.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dt.core.tool.util.ConvertUtil;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @author lank
 * @version 创建时间：2020年8月1日 上午8:33:12
 */
public class R implements Serializable {

    private static final long serialVersionUID = 3168255189576251355L;
    public static String TYPE_JSON = "json";
    public String type = R.TYPE_JSON;
    private Boolean clearAttach = false;
    private int code = BaseCodeMsgEnum.SUCCESS_OPER_MSG.getCode();
    private String message = BaseCodeMsgEnum.SUCCESS_OPER_MSG.getMessage();
    private boolean success = true;
    private Object data;

    public R(){}

    public R(boolean success, int code, String msg, Object data) {
        this.success = success;
        this.code = code;
        this.message = msg;

        if (data instanceof List<?>) {
            JSONArray r = JSONArray.parseArray(JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
            this.data = r;
        } else if (data instanceof T) {
            JSONObject r = JSONObject.parseObject(JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat));
            this.data = r;
        } else if (data instanceof org.json.JSONArray) {
            this.data = ConvertUtil.OtherJSONObjectToFastJSONArray(data);
        } else if (data instanceof org.json.JSONObject) {
            this.data = ConvertUtil.OtherJSONObjectToFastJSONObject(data);
        } else {
            this.data = data;
        }
    }

    public static R clearAttachDirect(Object data) {
        R r = new R();
        r.setClearAttach(true);
        r.setData(data);
        return r;
    }

    /************************* 操作成功 ***************************/
    public static R SUCCESS(String message, Object data) {
        return new R(true, 0, message, data);
    }

    public static R SUCCESS(String message) {
        return new R(true, 0, message, null);
    }

    public static R SUCCESS(String message, int code, Object data) {
        return new R(true, code, message, data);
    }

    public static R SUCCESS() {
        return new R(true, BaseCodeMsgEnum.SUCCESS_DEF_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_DEF_MSG.getMessage(),
                null);
    }

    public static R SUCCESS_OPER(Object data) {
        return new R(true, BaseCodeMsgEnum.SUCCESS_OPER_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_OPER_MSG.getMessage(),
                data);
    }

    public static R SUCCESS_OPER() {
        return new R(true, BaseCodeMsgEnum.SUCCESS_OPER_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_OPER_MSG.getMessage(),
                null);
    }

    public static R SUCCESS_SAVE() {
        return new R(true, BaseCodeMsgEnum.SUCCESS_SAVE_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_SAVE_MSG.getMessage(),
                null);
    }

    public static R SUCCESS_SAVE(Object data) {
        return new R(true, BaseCodeMsgEnum.SUCCESS_SAVE_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_SAVE_MSG.getMessage(),
                data);
    }

    public static R SUCCESS_NO_DATA() {
        return new R(true, BaseCodeMsgEnum.SUCCESS_OPER_MSG.getCode(), BaseCodeMsgEnum.SUCCESS_OPER_MSG.getMessage(),
                null);
    }

    /************************* 操作失败 ***************************/
    public static R FAILURE(String message, Object data) {
        return new R(false, BaseCodeMsgEnum.FAILED_DEF_MSG.getCode(), message, data);
    }

    public static R FAILURE(String message) {
        return new R(false, BaseCodeMsgEnum.FAILED_DEF_MSG.getCode(), message, null);
    }

    public static R FAILURE(String message, int code, Object data) {
        return new R(false, code, message, data);
    }

    public static R FAILURE() {
        return new R(false, BaseCodeMsgEnum.FAILED_DEF_MSG.getCode(), BaseCodeMsgEnum.FAILED_DEF_MSG.getMessage(),
                null);
    }

    public static R FAILURE_OPER() {
        return new R(false, BaseCodeMsgEnum.FAILED_OPER_MSG.getCode(), BaseCodeMsgEnum.FAILED_OPER_MSG.getMessage(),
                null);
    }

    public static R FAILURE_USER_QUERY() {
        return new R(false, BaseCodeMsgEnum.USER_QUERY_FAILED.getCode(), BaseCodeMsgEnum.USER_QUERY_FAILED.getMessage(),
                null);
    }

    public static R FAILURE_USER_NOT_EXISTED() {
        return new R(false, BaseCodeMsgEnum.USER_NOT_EXISTED.getCode(), BaseCodeMsgEnum.USER_NOT_EXISTED.getMessage(),
                null);
    }

    public static R FAILURE_NO_DATA() {
        return new R(false, BaseCodeMsgEnum.FAILED_NO_DATA_MSG.getCode(),
                BaseCodeMsgEnum.FAILED_NO_DATA_MSG.getMessage(), null);
    }

    public static R FAILURE_REQ_PARAM_ERROR() {
        return new R(false, BaseCodeMsgEnum.REQ_PARAM_ERROR.getCode(), BaseCodeMsgEnum.REQ_PARAM_ERROR.getMessage(),
                null);
    }

    public static R FAILURE_NO_PERMITION() {
        return new R(false, BaseCodeMsgEnum.NO_PERMITION.getCode(), BaseCodeMsgEnum.NO_PERMITION.getMessage(), null);
    }

    public static R FAILURE_SYS_PARAMS() {
        return new R(false, BaseCodeMsgEnum.SYSTEM_CONF_ERROR.getCode(), BaseCodeMsgEnum.SYSTEM_CONF_ERROR.getMessage(),
                null);
    }

    public static R FAILURE_OPER(Object data) {
        return new R(false, BaseCodeMsgEnum.FAILED_OPER_MSG.getCode(), BaseCodeMsgEnum.FAILED_OPER_MSG.getMessage(),
                null);
    }

    public static R FAILURE_SAVE() {
        return new R(false, BaseCodeMsgEnum.FAILED_SAVE_MSG.getCode(), BaseCodeMsgEnum.FAILED_SAVE_MSG.getMessage(),
                null);
    }

    public static R FAILURE_SAVE(Object data) {
        return new R(false, BaseCodeMsgEnum.FAILED_SAVE_MSG.getCode(), BaseCodeMsgEnum.FAILED_SAVE_MSG.getMessage(),
                data);
    }

    public static R FAILURE_NOT_LOGIN() {
        return new R(false, BaseCodeMsgEnum.USER_NOT_LOGIN.getCode(), BaseCodeMsgEnum.USER_NOT_LOGIN.getMessage(),
                null);
    }

    public static R FAILURE_UNKNOW_ERROR() {
        return new R(false, BaseCodeMsgEnum.SYSTEM_UNKNOW_ERROR.getCode(),
                BaseCodeMsgEnum.SYSTEM_UNKNOW_ERROR.getMessage(), null);
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setClearAttach(Boolean clearAttach) {
        this.clearAttach = clearAttach;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFailed() {
        return !success;
    }

    public String getMessage() {
        return this.message;
    }

    public R setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data instanceof List<?>) {
            JSONArray r = JSONArray.parseArray(JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
            this.data = r;
        } else if (data instanceof T) {
            JSONObject r = JSONObject.parseObject(JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat));
            this.data = r;
        } else if (data instanceof org.json.JSONArray) {
            this.data = ConvertUtil.OtherJSONObjectToFastJSONArray(data);
        } else if (data instanceof org.json.JSONObject) {
            this.data = ConvertUtil.OtherJSONObjectToFastJSONObject(data);
        } else {
            this.data = data;
        }
    }

    public JSONArray queryDataToJSONArray() {
        if (data instanceof org.json.JSONArray) {
            return JSONArray.parseArray(data.toString());
        } else if (data instanceof JSONArray) {
            return (JSONArray) (data);
        } else {
            return new JSONArray();
        }
    }

    public JSONObject queryDataToJSONObject() {
        if (data instanceof org.json.JSONObject) {
            return JSONObject.parseObject(data.toString());
        } else if (data instanceof JSONObject) {
            return (JSONObject) (data);
        } else {
            return new JSONObject();
        }
    }

    public String queryDataToString() {
        if (data instanceof org.json.JSONArray) {
            return data.toString();
        } else if (data instanceof org.json.JSONObject) {
            return data.toString();
        } else if (data instanceof JSONObject) {
            return ((JSONObject) (data)).toJSONString();
        } else if (data instanceof JSONArray) {
            return ((JSONArray) (data)).toJSONString();
        } else {
            return data.toString();
        }
    }

    public String asJsonStr() {
        Object obj = asJson();
        if (obj instanceof JSONObject) {
            return ((JSONObject) (obj)).toJSONString();
        } else if (obj instanceof JSONArray) {
            return ((JSONArray) (obj)).toJSONString();
        }
        return obj.toString();
    }

    /**
     */
    public Object asJson() {
        if (clearAttach) {
            if (data instanceof org.json.JSONArray) {
                return JSONArray.parseArray(data.toString());
            } else if (data instanceof org.json.JSONObject) {
                return JSONObject.parseObject(data.toString());
            } else if (data instanceof JSONObject || data instanceof JSONArray) {
                return data;
            } else {
                return data;
            }
        } else {
            JSONObject json = new JSONObject();
            json.put("code", code);
            json.put("success", success);
            json.put("message", message);
            if (data instanceof org.json.JSONObject) {
                json.put("data", JSONObject.parseObject(data.toString()));
            } else if (data instanceof org.json.JSONArray) {
                json.put("data", JSONArray.parseArray(data.toString()));
            } else {
                json.put("data", data);
            }
            return json;
        }
    }

    /**
     */
    public JSONObject asJsonObject() {
        Object obj = asJson();
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else {
            // ob肯定是JSONArray
            JSONObject json = new JSONObject();
            json.put("code", code);
            json.put("success", success);
            json.put("message", message);
            if (obj instanceof JSONArray) {
                json.put("data", obj);
            } else {
                // 未识别,则将原来的数据放回去
                json.put("data", data);
            }
            return json;
        }
    }

    /**
     */
    public JSONArray asJsonArray() {
        return queryDataToJSONArray();
    }

    @Override
    public String toString() {
        if (TYPE_JSON.equals(type)) {
            return asJsonStr();
        } else {
            return asJsonStr();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
