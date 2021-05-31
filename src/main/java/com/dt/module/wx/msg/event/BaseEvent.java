package com.dt.module.wx.msg.event;

public class BaseEvent {

    private String ToUserName;
    private String FromUserName;
    private String MesgType;
    private String Event;
    private long CreateTime;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getMesgType() {
        return MesgType;
    }

    public void setMesgType(String mesgType) {
        MesgType = mesgType;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

}
