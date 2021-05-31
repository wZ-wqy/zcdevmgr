package com.dt.module.wx.pojo;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: lank
 * @date: 2020年3月22日 上午9:57:11
 * @Description:
 */

public class AccessTicket {

    // 获取到的凭证
    private String ticket;
    // 凭证有效时间，单位：秒
    private int expiresIn;
    private long ctime;

    public String getTicket() {

        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        JSONObject e = new JSONObject();
        e.put("ticket", ticket);
        e.put("expiresIn", expiresIn);
        e.put("ctime", ctime);
        return e.toJSONString();
    }

    public JSONObject toJsonObject() {
        JSONObject e = new JSONObject();
        e.put("ticket", ticket);
        e.put("expiresIn", expiresIn);
        e.put("ctime", ctime);
        return e;
    }

}
