package com.dt.module.wx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.wx.service.CoreService;
import com.dt.module.wx.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api")
public class WxApiController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(WxApiController.class);

    @Autowired
    private WxService wxService;

    @Autowired
    private CoreService coreService;


    /**
     * @Description:验证签名
     */
    @Acl(info = "验证签名", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/core.do", method = RequestMethod.GET)
    public void coreGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (wxService.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.close();
    }

    /**
     * @Description:消息响应
     */
    @Acl(info = "消息响应", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/core.do", method = RequestMethod.POST)
    public void corePost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 消息的接收、处理、响应
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 调用核心业务类接收消息、处理消息
        String respXml = coreService.processRequest(request);
        // 响应消息

        if (respXml != null) {
            log.info("return:\n" + respXml);
            PrintWriter out = response.getWriter();
            out.print(respXml);
            out.close();
        }
    }


    /**
     * @Description:创建菜单
     */
    @ResponseBody
    @Acl(info = "创建菜单", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/createMenuToWx.do")
    public R createMenuToWx(String id) {
        return wxService.createMenuToWx(id);
    }


    /**
     * @Description:根据ID查询WxApp
     */
    @ResponseBody
    @Acl(info = "查询WxApp", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/queryWxAppById.do")
    public R queryWxAppById(String id) {
        return wxService.queryWxAppById(id);
    }

    /**
     * @Description:同步微信公众号菜单
     */
    @ResponseBody
    @Acl(info = "同步菜单", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/sysncMenu.do")
    public R sysncMenu(String id) {
        return wxService.syncMenuFromWxWithId(id);
    }

    /**
     * @Description:查询微信应用
     */
    @ResponseBody
    @Acl(info = "查询微信Apps", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/queryWxApps.do")
    public R queryWxApps() {
        return wxService.queryWxApps();
    }

    /**
     * @Description:查询用户信息
     */
    @ResponseBody
    @Acl(info = "查询用户信息", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/wx/queryUserInfo.do")
    public R queryUserInfo(String open_id) {
        return wxService.queryUserInfo(open_id);
    }

    /**
     * @Description:修改微信应用
     */
    @ResponseBody
    @Acl(info = "修改WxApp", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/saveWxApp.do")
    public R saveWxApp() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return wxService.saveWxApp(ps);
    }

    /**
     * @Description:删除微信应用
     */
    @ResponseBody
    @Acl(info = "删除WxApp", value = Acl.ACL_DENY)
    @RequestMapping(value = "/wx/delWxapp.do")
    public R delWxapp(String id) {
        return wxService.deleteWxapp(id);
    }




}
