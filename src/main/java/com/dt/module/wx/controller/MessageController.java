package com.dt.module.wx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.wx.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    /**
     * @Description:删除消息
     */
    @RequestMapping("/wx/deleteMessage.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "删除消息")
    public R deleteMessage(String id) {
        return messageService.deleteMessage(id);
    }

    /**
     * @Description:更新消息
     */
    @RequestMapping("/wx/saveMessage.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "更新消息")
    public R saveMessage() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        if (ToolUtil.isEmpty(ps.getString("id"))) {
            return messageService.addMessage(ps);
        } else {
            return messageService.updateMessage(ps);
        }
    }

    /**
     * @Description:根据ID查询消息
     */
    @RequestMapping("/wx/queryMessageById.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询消息")
    public R queryMessageById(String id) {
        return messageService.queryMessageById(id);
    }

    /**
     * @Description:根据类型查询消息
     */
    @RequestMapping("/wx/queryMessages.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询消息")
    public R queryMessages(String funtype) {
        return messageService.queryMessages(funtype);
    }

    /**
     * @Description:根据图片消息
     */
    @RequestMapping("/wx/queryImageTextMessagesGroup.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询图文消息")
    public R queryImageTextMessagesGroup() {
        return messageService.queryImageTextMessagesGroup();
    }

    /**
     * @Description:查询图文消息
     */
    @RequestMapping("/wx/queryImageTextMessages.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询图文消息")
    public R queryImageTextMessages(String id) {
        return messageService.queryImageTextMessages(id);
    }

    /**
     * @Description:删除图文消息
     */
    @RequestMapping("/wx/deleteImageTextMessage.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "删除图文消息")
    public R deleteImageTextMessage(String id) {
        return messageService.deleteImageTextMessage(id);
    }


    /**
     * @Description:更新图片消息
     */
    @RequestMapping("/wx/saveImageTextMessage.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "更新图文消息")
    public R saveImageTextMessage() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        if (ToolUtil.isEmpty(ps.getString("id"))) {
            return messageService.addImageTextMessage(ps);
        } else {
            return messageService.updateImageTextMessage(ps);
        }
    }

    /**
     * @Description:查询图文消息
     */
    @RequestMapping("/wx/queryImageTextMessageById.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询图文消息")
    public R queryImageTextMessageById(String id) {
        return messageService.queryImageTextMessageById(id);
    }

    /**
     * @Description:添加素材
     */
    @RequestMapping("/wx/addSc.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "添加素材")
    public R addSc(String id) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return messageService.addSc(ps);
    }

    /**
     * @Description:查询素材资源
     */
    @RequestMapping("/wx/queryScs.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "查询素材")
    public R queryScs() {
        return messageService.queryScs();
    }
}
