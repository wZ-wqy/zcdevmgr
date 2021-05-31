package com.dt.module.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseCodeMsgEnum;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.shiro.ShiroUser;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysParams;
import com.dt.module.base.service.ISysMenusService;
import com.dt.module.base.service.ISysParamsService;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.base.service.impl.LoginService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class LoginController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService = null;

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    @Autowired
    ISysParamsService SysParamsServiceImpl;

    @Autowired
    ISysMenusService SysMenusServiceImpl;


    /**
     *  用户登陆接口
     *  @param user
     *  @param pwd
     *  @param type
     *  @param client
     */
    @Acl(value = Acl.ACL_ALLOW, info = "登录")
    @RequestMapping(value = "/user/login.do")
    @ResponseBody
    public R login(String user, String pwd, String type, String client) {

        // 验证账户是否有效
        R vlrs = loginService.validLogin(user, type, client);
        if (vlrs.isFailed()) {
            return vlrs;
        }
        String user_id = vlrs.getData().toString();
        // 登录操作
        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user_id, pwd == null ? null : pwd.toCharArray());
        token.setRememberMe(true);
        String error = "";
        try {
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            error = "账户不存在";
        } catch (IncorrectCredentialsException e) {
            error = "账号密码错误";
        } catch (ExcessiveAttemptsException e) {
            error = "登录失败多次，账户锁定10分钟";
        } catch (LockedAccountException e) {
            error = "账户已被锁定";
        } catch (AuthenticationException e) {
            error = "其他错误：" + e.getMessage();
        }
        if (ToolUtil.isNotEmpty(error)) {
            return R.FAILURE(error);
        }

        // 用户登录成功
        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("user_id", shiroUser.id);
        ShiroKit.getSession().setAttribute("sessionFlag", true);

        JSONObject r = new JSONObject();

        JSONObject u = JSONObject.parseObject(
                JSON.toJSONString(SysUserInfoServiceImpl.getById(user_id), SerializerFeature.WriteDateUseDateFormat));
        // 覆盖重要信息
        u.put("pwd", "********");
        r.put("user_info", u);
        SysParams parms_version = SysParamsServiceImpl.getById("version");
        if (parms_version != null) {
            r.put("dtversion", parms_version.getValue());
        }
        SysParams parms_app = SysParamsServiceImpl.getById("app");
        if (parms_app != null) {
            r.put("dtmsg", parms_app.getValue());
        }

        // 菜单列表
        JSONArray systems = null;
        if (BaseCommon.isSuperAdmin(shiroUser.id)) {
            QueryWrapper<SysMenus> q=new QueryWrapper<SysMenus>();
            q.orderByDesc("sort");
            systems = ConvertUtil.toJSONArryFromEntityList(SysMenusServiceImpl.list(q));
            r.put("systems", systems);
        } else {
            systems = ConvertUtil.toJSONArryFromEntityList(SysUserInfoServiceImpl.listMyMenus(this.getUserId()));
            r.put("systems", systems);
        }

        // 获取当前需要显示的菜单
        String cur_system = "";

        String tab_system = (u.getString("systemId") == null ? "" : u.getString("systemId"));
        if (systems.size() == 0) {
            cur_system = "";
        } else {
            for (int i = 0; i < systems.size(); i++) {
                //判断当前数据库中选择的system是否存在用户可选列表中
                if (systems.getJSONObject(i).getString("menuId").equals(tab_system)) {
                    cur_system = systems.getJSONObject(i).getString("menuId");
                    break;
                }
            }
            if (ToolUtil.isEmpty(cur_system) || cur_system.equals("")) {
                cur_system = systems.getJSONObject(0).getString("menuId");
            }

        }
        r.put("cur_system", cur_system);
        r.put("token", super.getSession().getId());
        log.info("login:" + r.toJSONString());
        return R.SUCCESS(BaseCodeMsgEnum.USER_LOGIN_SUCCESS.getMessage(), r);
    }

    @Acl(value = Acl.ACL_ALLOW, info = "登录")
    @RequestMapping(value = "/user/loginFast.do")
    @ResponseBody
    public R loginFast(String user, String pwd, String type, String client, HttpServletRequest request) {

        // 验证账户是否有效
        R vlrs = loginService.validLogin(user, type, client);
        if (vlrs.isFailed()) {
            return vlrs;
        }
        String user_id = vlrs.getData().toString();
        // 登录操作
        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user_id, pwd == null ? null : pwd.toCharArray());
        //    token.setRememberMe(true);
        String error = "";
        try {
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            error = "账户不存在";
        } catch (IncorrectCredentialsException e) {
            error = "账号密码错误";
        } catch (ExcessiveAttemptsException e) {
            error = "登录失败多次，账户锁定10分钟";
        } catch (LockedAccountException e) {
            error = "账户已被锁定";
        } catch (AuthenticationException e) {
            error = "其他错误：" + e.getMessage();
        }
        if (ToolUtil.isNotEmpty(error)) {
            return R.FAILURE(error);
        }

        // 用户登录成功
        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("user_id", shiroUser.id);
        ShiroKit.getSession().setAttribute("sessionFlag", true);

        JSONObject r = new JSONObject();
        JSONObject u = JSONObject.parseObject(
                JSON.toJSONString(SysUserInfoServiceImpl.getById(user_id), SerializerFeature.WriteDateUseDateFormat));
        // 覆盖重要信息
        u.put("pwd", "********");
        r.put("user_info", u);
        r.put("token", super.getSession().getId());
        log.info("login:" + r.toJSONString());
        return R.SUCCESS(BaseCodeMsgEnum.USER_LOGIN_SUCCESS.getMessage(), r);
    }

    @RequestMapping(value = "/user/checkLogin.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "检测登录")
    public R checkLogin() throws IOException {
        if (ShiroKit.isAuthenticated()) {
            return R.SUCCESS(BaseCodeMsgEnum.USER_ALREADY_LOGIN.getMessage());
        } else {
            return R.FAILURE_NOT_LOGIN();
        }
    }

    /**
     * @Description:
     */
    @RequestMapping(value = "/user/logout.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "退出")
    public R loginout() throws IOException {
        if (ShiroKit.isAuthenticated()) {
            ShiroKit.getSubject().logout();
        }
        return R.SUCCESS("成功退出");
    }

}
