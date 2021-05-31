package com.dt.module.wx.msg.resp;

/**
 * 文本消息 (响应消息)
 */
public class TextMessage extends BaseMessage {

    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
