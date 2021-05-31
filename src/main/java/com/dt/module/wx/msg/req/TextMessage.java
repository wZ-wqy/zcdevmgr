package com.dt.module.wx.msg.req;

/**
 * 文本消息 (请求消息)
 */
public class TextMessage extends BaseMessage {

    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
