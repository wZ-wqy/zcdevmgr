package com.dt.module.wx.msg.resp;

/**
 * 音乐消息 (响应消息)
 */
public class MusicMessage extends BaseMessage {
    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
