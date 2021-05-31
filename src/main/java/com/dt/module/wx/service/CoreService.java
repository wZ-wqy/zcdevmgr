package com.dt.module.wx.service;

import com.dt.core.common.base.BaseService;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.wx.msg.resp.Article;
import com.dt.module.wx.msg.resp.NewsMessage;
import com.dt.module.wx.msg.resp.TextMessage;
import com.dt.module.wx.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Configuration
@PropertySource(value = "classpath:config.properties")
public class CoreService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(CoreService.class);

    @Value("${imgdownurl}")
    public String imgdownurl;

    @Autowired
    WxService wxService;

    public static void main(String[] args) {

    }

    /**
     * @Description:处理微信消息数据
     */
    public String processMsg(String fromUserName, String toUserName, String msgType, String ct, String ctdef) {
        String res = "";
        if (MessageUtil.RESP_MESSAGE_TYPE_TEXT.equals(msgType)) {
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            if (ct == null) {
                ct = ctdef;
            }
            textMessage.setContent(ct);
            res = MessageUtil.textMessageToXml(textMessage);
        }

        return res;
    }

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return xml
     */
    public String processRequest(HttpServletRequest request) {
        log.info("sessionId:" + request.getSession().getId());
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent = "";
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号
            String fromUserName = requestMap.get("FromUserName");
            // 开发者微信号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            String content = requestMap.get("Content");
            // 网页授权1,登录
            wxService.baseToLogin(fromUserName, "1");
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            if (ToolUtil.isNotEmpty(toUserName)) {
                HttpKit.getRequest().getSession().setAttribute("open_id", fromUserName);
            }

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                respContent = "您发送的是文本消息！";
                log.info("文本消息");
                respContent = processMsg(fromUserName, toUserName, msgType, content, "您发送的是文本消息!");
                return queryeventBykey(content, fromUserName, toUserName);
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
            }
            // 语音消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！";
            }
            // 视频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 关注
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "";
                    return respContent;
                }
                // 取消关注
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    //  取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                }
                // 扫描带参数二维码
                else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
                    //  处理扫描带参数二维码事件
                    respContent = "";
                    return respContent;

                }
                // 上报地理位置
                else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
                    //  处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    //  处理菜单点击事件
                    respContent = queryeventBykey(requestMap.get("EventKey"), fromUserName, toUserName);
                    return respContent;
                }
            }
            // 设置文本消息的内容
            textMessage.setContent(respContent);
            // 将文本消息对象转换成xml
            respXml = MessageUtil.textMessageToXml(textMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }

    /**
     * @Description:查询使用存在该key数据
     */
    public String queryeventBykey(String key, String fromUserName, String toUserName) {

        log.info("eventbykey,key:" + key);
        Rcd keyrcd = db.uniqueRecord("select * from wx_msg_def where dr=0 and code=?", key);
        // 单图文的时候会有描述信息
        // 多图文的时候第一张将放大，描述信息隐藏
        if (keyrcd == null) {
            log.info("未匹配到" + key);
            return "";
        }
        String msgtype = keyrcd.getString("msgtype") == null ? "-none" : keyrcd.getString("msgtype");
        if ("6".equals(msgtype)) {
            // 为图文消息
            RcdSet set = db.query("select * from wx_msg_imgitem t where group_id=? and dr=0 order by rn",
                    keyrcd.getString("group_id"));
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setArticleCount(set.size());
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setCreateTime(new Date().getTime());
            List<Article> list = new ArrayList<Article>();
            log.info("find cnt" + set.size());
            if (set.size() == 0) {
                return null;
            }
            for (int i = 0; i < set.size(); i++) {
                Article art = new Article();
                art.setTitle(set.getRcd(i).getString("title") == null ? "" : set.getRcd(i).getString("title"));
                art.setDescription(
                        set.getRcd(i).getString("msgdesc") == null ? "" : set.getRcd(i).getString("msgdesc"));
                String imgurl = set.getRcd(i).getString("imgurl") == null ? "" : set.getRcd(i).getString("imgurl");
                art.setPicUrl(imgurl.startsWith("http") ? imgurl : imgdownurl + "?id=" + imgurl);
                art.setUrl(set.getRcd(i).getString("docurl") == null ? "" : set.getRcd(i).getString("docurl"));
                list.add(art);
            }
            newsMessage.setArticles(list);
            return MessageUtil.newsMessageToXml(newsMessage);

        } else if ("text".equals(msgtype)) {
            // 文本
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setContent(keyrcd.getString("value"));
            return MessageUtil.textMessageToXml(textMessage);
        }

        return null;

    }
}