package com.dt.module.base.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lank
 * @date: 2017年8月7日 上午7:58:00
 * @Description:
 */
@Service
public class LoginService extends BaseService {

    /**
     * @Description: 所有都登录最终统一转成user_id去判断
     * mobile_code 手机验证码
     * smallprogram 小程序
     */
    public static String LOGIN_TYPE_QQ = "qq";
    public static String LOGIN_TYPE_EMPL = "empl";
    public static String LOGIN_TYPE_USERNAME = "username";
    public static String LOGIN_TYPE_MAIL = "mail";
    public static String LOGIN_TYPE_MOBILE = "mobile";
    public static String LOGIN_TYPE_MOBILE_CODE = "mobile_code";
    public static String LOGIN_TYPE_WEIXIN = "weixin";
    public static String LOGIN_TYPE_ZFB = "zfb";
    public static String LOGIN_TYPE_VALID_MESSAGE = "不支持的登录类型";
    public static String CLIENT_TYPE_WEB = "web";
    public static String CLIENT_TYPE_WEIXIN = "weixin";
    public static String CLIENT_TYPE_SMALLPROGRAM = "smallprogram";
    public static String CLIENT_TYPE_APP = "app";
    public static String CLIENT_TYPE_VALID_MESSAGE = "不支持的客户端类型";

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    public static LoginService me() {
        return SpringContextUtil.getBean(LoginService.class);
    }

    /**
     *  验证客户端
     *  @param value
     */
    public R validClientType(String value) {
        if (ToolUtil.isEmpty(value)) {
            return R.FAILURE(CLIENT_TYPE_VALID_MESSAGE);
        }
        if (value.equals(CLIENT_TYPE_WEB) || value.equals(CLIENT_TYPE_WEB) || value.equals(CLIENT_TYPE_SMALLPROGRAM)
                || value.equals(CLIENT_TYPE_APP)) {
            return R.SUCCESS_OPER();
        } else {
            return R.FAILURE(CLIENT_TYPE_VALID_MESSAGE);
        }
    }

    /**
     *  查询支持的客户端类型
     */
    public R querySupportClientType() {
        return R.SUCCESS_OPER();
    }

    /**
     *  查询支持的登陆类型
     */
    public R querySupportLoginType() {
        return R.SUCCESS_OPER();
    }

    public void login() {
    }

    /**
     * @Description:判断登录方式是否有效,user_type中如果存在两条以上数据,则不允许登录,正确则返回唯一的user_id
     */
    public R validLoginType(String value, String login_type) {

        // 匹配login_type字段
        if (ToolUtil.isEmpty(login_type)) {
            return R.FAILURE(LOGIN_TYPE_VALID_MESSAGE);
        }

        if (login_type.equals(LOGIN_TYPE_QQ) || login_type.equals(LOGIN_TYPE_EMPL)
                || login_type.equals(LOGIN_TYPE_USERNAME) || login_type.equals(LOGIN_TYPE_MAIL)
                || login_type.equals(LOGIN_TYPE_MOBILE) || login_type.equals(LOGIN_TYPE_MOBILE_CODE)
                || login_type.equals(LOGIN_TYPE_WEIXIN) || login_type.equals(LOGIN_TYPE_ZFB)) {
        } else {
            return R.FAILURE(LOGIN_TYPE_VALID_MESSAGE);
        }

        // 校验login_type
        // 只校验了LOGIN_TYPE_EMPL,LOGIN_TYPE_USERNAME,LOGIN_TYPE_MOBILE,LOGIN_TYPE_MAIL
        R res = null;
        if (ToolUtil.isOneEmpty(value, login_type)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }

        if (login_type.equals(LOGIN_TYPE_EMPL)) {
            // 系统本身唯一
            SysUserInfo s = SysUserInfoServiceImpl.selectOneByEmpl(value);
            if (s == null) {
                return R.FAILURE("用户不存在");
            }
            if ("1".equals(s.getIslogoff())) {
                return R.FAILURE("用户已注销");
            }
            return R.SUCCESS_OPER(s.getUserId());

        } else if (login_type.equals(LOGIN_TYPE_USERNAME)) {
            res = SysUserInfoServiceImpl.queryUserIdByUserName(value);
            if (res.isFailed()) {
                return res;
            }
        } else if (login_type.equals(LOGIN_TYPE_MOBILE)) {
            // 系统本身可能不唯一
//			String[] userids = UserService.me().getUserIdFromMobile(value, login_type);
//			if (ToolUtil.isNotEmpty(userids)) {
//				if (userids.length == 1) {
//					res = R.SUCCESS_OPER(userids[0]);
//				} else if (userids.length == 0) {
//					res = R.FAILURE("用户不存在");
//				} else {
//					res = R.FAILURE("存在两条记录,无法匹配记录不允许登录");
//				}
//			} else {
//				res = R.FAILURE("用户不存在");
//			}
            return R.FAILURE("开发中");
        } else if (login_type.equals(LOGIN_TYPE_MAIL)) {
            R.FAILURE("功能未开发");
//			String[] userids = UserService.me().getUserIdFromMail(value, login_type);
//			if (ToolUtil.isNotEmpty(userids)) {
//				if (userids.length == 1) {
//					res = R.SUCCESS_OPER(userids[0]);
//				} else if (userids.length == 0) {
//					res = R.FAILURE("用户不存在");
//				} else {
//					res = R.FAILURE("存在两条记录,无法匹配记录不允许登录");
//				}
//			} else {
//				res = R.FAILURE("用户不存在");
//			}
        } else {
            res = R.FAILURE("未实现校验,暂不支持");
        }

        // 返回数据
        if (ToolUtil.isEmpty(res)) {
            res = R.FAILURE_NO_DATA();
        }
        return res;
    }

    /**
     * @Description:将所有登录方式转换成系统user_id的登录形式, 如果可以登录, 则返回一组用户数据 login_type
     * 如果是empl或username忽略user_type类型，
     * 因为empl和username全局唯一
     */
    public R validLogin(String value, String login_type, String client) {
        if (ToolUtil.isOneEmpty(value, login_type, client)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        // 判断loginclient
        R validLoginClient = validClientType(client);
        if (validLoginClient.isFailed()) {
            return validLoginClient;
        }

        // 判断及验证logintype
        R validLoginType = validLoginType(value, login_type);
        if (validLoginType.isFailed()) {
            return validLoginType;
        }
        return validLoginType;
    }

    /**
     * @Description: 退出登录
     */
    public R logout(String cookie) {
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 检查登录状态
     */
    public R loginCheck() {
        return R.SUCCESS_OPER();
    }

}
