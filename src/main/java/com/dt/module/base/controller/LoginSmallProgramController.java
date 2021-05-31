package com.dt.module.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseCodeMsgEnum;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.shiro.ShiroUser;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.service.impl.LoginService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lank
 * @date: 2017年11月23日 下午9:45:39
 * @Description:
 */
@Controller
@RequestMapping("/api")
@Configuration
@PropertySource(value = "classpath:config.properties")
public class LoginSmallProgramController extends BaseController {


    @Value("${wx.xcx_appId}")
    public String xcx_appId;

    @Value("${wx.xcx_secret}")
    public String xcx_secret;

    @Autowired
    LoginService loginService;

    /**
     *  根据Code返回OpenId
     *  @param code
     */
    private R getOpenIdStr(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> map = new HashMap<String, String>();
        if (ToolUtil.isOneEmpty(xcx_appId, xcx_secret)) {
            return R.FAILURE("配置信息有误");
        }
        map.put("appid", xcx_appId);
        map.put("secret", xcx_secret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String str = HttpKit.sendGet(url, map);
        JSONObject strobj = JSONObject.parseObject(str);
        // 判断是否获取open_id
        String openId = strobj.getString("openid");
        if (ToolUtil.isEmpty(openId)) {
            return R.FAILURE(BaseCodeMsgEnum.WX_FAILED_GET_OPENID.getMessage(),
                    BaseCodeMsgEnum.WX_FAILED_GET_OPENID.getCode(), null);
        }
        return R.SUCCESS(BaseCodeMsgEnum.SUCCESS_DEF_MSG.getMessage(), strobj);

    }
    /**
     * 小程序用户u登陆
     *  @param code
     */
    @RequestMapping(value = "/smallprogram/login.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "小程序用户登录")
    public R login(String code) {
        if (ToolUtil.isEmpty(code)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        R strres = getOpenIdStr(code);
        if (strres.isFailed()) {
            return strres;
        }
        String openId = strres.queryDataToJSONObject().getString("openid");
        // 判断用户是否存在
        // R userrs = wxUserService.queryUserByOpenId(openId);
        R userrs = new R();
        if (userrs.isFailed()) {
            return R.FAILURE_USER_NOT_EXISTED();
        }
        Subject currentUser = ShiroKit.getSubject();
        String user_id = userrs.queryDataToJSONObject().getString("user_id");
        String pwd = userrs.queryDataToJSONObject().getString("pwd");
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

        // 添加更新sys_session表
        String tid = super.getSession().getId();
        ShiroUser shiroUser = ShiroKit.getUser();
        JSONObject ret = new JSONObject();
        ret.put("cookie", tid);
        ret.put("token", tid);
        // 用户登录成功
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("user_id", shiroUser.id);
        super.getSession().setAttribute("open_id", openId);
        return R.SUCCESS(BaseCodeMsgEnum.USER_LOGIN_SUCCESS.getMessage(), ret);

    }

    /**
     * 小程序用户注册
     */
    @RequestMapping(value = "/smallprogram/register.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "小程序用户注册")
    public R register(String code, String avatarUrl, String city, String country, String nickName, String province) {
        R strres = getOpenIdStr(code);
        if (strres.isFailed()) {
            return strres;
        }
        String openId = strres.queryDataToJSONObject().getString("openid");
        if (ToolUtil.isOneEmpty(avatarUrl, nickName, openId)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        TypedHashMap<String, Object> ps = new TypedHashMap<String, Object>();
        ps.put("open_id", openId);
        ps.put("locked", "N");
        ps.put("nickname", nickName);
        ps.put("avatarurl", avatarUrl);
        ps.put("score", "0");
        //	R rs = userService.addUser(ps, UserService.USER_TYPE_XCX);
        R rs = new R();
        return rs;
    }

    /**
     * 小程序检查当前用户是否登陆
     */
    @RequestMapping("/smallprogram/checkLogin.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "检测登录")
    public R checkLogin() {
        String userId = this.getUserId();
        if (ToolUtil.isNotEmpty(userId) && userId.length() > 2) {
            return R.SUCCESS_OPER();
        } else {
            return R.FAILURE();
        }
    }

}
