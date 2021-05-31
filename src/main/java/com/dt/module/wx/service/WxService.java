package com.dt.module.wx.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Delete;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.UserTypeEnum;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.wx.pojo.AccessTicket;
import com.dt.module.wx.pojo.WeixinUserInfo;
import com.dt.module.wx.pojo.WxAccessToken;
import com.dt.module.wx.util.AdvancedUtil;
import com.dt.module.wx.util.WeiXX509TrustManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author: lank
 * @date: 2020年3月29日 上午11:51:41
 * @Description:
 */
@Service
@Configuration
@PropertySource(value = "classpath:config.properties")
public class WxService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(WxService.class);
    /**
     * @Description:微信公众号获取配置
     */
    private static Map<String, WxAccessToken> tokens = new HashMap<String, WxAccessToken>();
    private static Map<String, AccessTicket> tickets = new HashMap<String, AccessTicket>();
    @Value("${wx.appId}")
    public String appIdconf;
    @Value("${wx.secret}")
    public String secretconf;
    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;

    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }


    /**
     * @Description:http请求
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new WeiXX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes(StandardCharsets.UTF_8));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            // log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            // log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        WxService s = new WxService();
        s.syncMenuFromWx("wx8fc3340c90ec5d53", "f6cea94ef73b19db97320a36b3fb36b4");

    }

    /**
     * @Description:查询token
     */
    public R queryAccessToken() {
        return queryWxToken(appIdconf, secretconf, false);
    }


    /**
     * @Description:查询token
     */
    public R queryWxToken(String appId, String secret, boolean ifnew) {
        JSONObject r = new JSONObject();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret="
                + secret;
        String token = "";
        // 无效
        if (!ifnew) {
            // 从缓存中获取
            WxAccessToken at = tokens.get(appId);
            if (at == null || (System.currentTimeMillis() - at.getCtime()) / 1000 > at.getExpiresIn() * 0.9) {
                // 无效,后续重新请求
                ifnew = true;
            } else {
                token = at.getToken();
                log.info("access_token(mem):" + at.getToken());
                if (ToolUtil.isEmpty(token)) {
                    return R.FAILURE_NO_DATA();
                }
                r.put("access_token", token);
                return R.SUCCESS_OPER(r);
            }
        }

        if (ifnew) {
            JSONObject json = httpRequest(url, "GET", null);
            token = json.getString("access_token");
            WxAccessToken t = new WxAccessToken();
            t.setCtime(System.currentTimeMillis());
            t.setExpiresIn(json.getIntValue("expires_in"));
            t.setToken(token);
            tokens.put(appId, t);
            log.info("result:" + json.toJSONString());
            log.info("access_token(new):" + token);
        }
        if (ToolUtil.isEmpty(token)) {
            return R.FAILURE_NO_DATA();
        }
        r.put("access_token", token);
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询Tickets
     */
    public R queryMapTickets() {
        JSONArray res = new JSONArray();
        Set<String> set = tickets.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            AccessTicket value = tickets.get(key);
            JSONObject e = new JSONObject();
            e.put("access_token", key);
            e.put("data", value.toJsonObject());
            res.add(e);
        }
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询Tickets
     */
    public R queryWxTicket(String access_token, boolean ifnew) {
        JSONObject r = new JSONObject();
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        String ticket = "";
        // 无效
        if (!ifnew) {
            // 从缓存中获取
            AccessTicket at = tickets.get(access_token);
            if (at == null || (System.currentTimeMillis() - at.getCtime()) / 1000 > at.getExpiresIn() * 0.9) {
                // 无效,后续重新请求
                ifnew = true;
            } else {
                ticket = at.getTicket();
                log.info("access_ticket(mem):" + at.getTicket());
                r.put("access_ticket", ticket);
                return R.SUCCESS_OPER(r);
            }
        }

        if (ifnew) {
            JSONObject json = httpRequest(url, "GET", null);
            log.info("httpRequest:" + json.toJSONString());
            ticket = json.getString("ticket");
            AccessTicket t = new AccessTicket();
            t.setCtime(System.currentTimeMillis());
            t.setExpiresIn(json.getIntValue("expires_in"));
            t.setTicket(ticket);
            tickets.put(access_token, t);
            log.info("access_ticket(new):" + ticket);
        }

        r.put("access_ticket", ticket);
        return R.SUCCESS_OPER(r);
    }


    /**
     * @Description:查询签名
     */
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        R tr = queryAccessToken();
        if (tr.isFailed()) {
            return false;
        }
        String token = "weixin";
        log.info("token:" + token);
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("right value:" + tmpStr);
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /**
     * @Description:查询菜单
     */
    public R queryMenu() {
        R trs = queryAccessToken();
        if (trs.isFailed()) {
            return R.FAILURE();
        }
        String token = trs.queryDataToJSONObject().getString("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + token;
        JSONObject json = httpRequest(url, "GET", null);
        return R.SUCCESS_OPER(json);

    }

    /**
     * @Description:查询应用
     */
    public R queryWxApps() {
        return R.SUCCESS_OPER(db.query("select * from wx_apps where dr=0").toJsonArrayWithJsonObject());
    }

    /**
     * @Description:查删除
     */
    public R deleteWxapp(String id) {
        return R.SUCCESS_OPER(db.execute("delete from wx_apps where id=?", id));
    }

    /**
     * @Description:根据open_id查询用户数据
     */
    public R queryUserInfo(String open_id) {
        R trs = queryAccessToken();
        if (trs.isFailed()) {
            return R.FAILURE();
        }
        String token = trs.queryDataToJSONObject().getString("access_token");
        String url = " https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + open_id;
        JSONObject json = httpRequest(url, "GET", null);
        return R.SUCCESS_OPER(json);

    }

    /**
     * @Description:保存应用
     */
    public R saveWxApp(TypedHashMap<String, Object> ps) {
        Update me = new Update("wx_apps");
        me.set("dr", 0);
        me.setIf("name", ps.getString("name"));
        me.setIf("mark", ps.getString("mark"));
        me.setIf("app_id", ps.getString("app_id"));
        me.setIf("menu", ps.getString("menu"));
        me.where().and("id=?", ps.getString("id"));
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:删除应用
     */
    public R deleteWxAppById(String id) {
        Delete me = new Delete("wx_apps");
        me.where().and("id=?", id);
        db.execute(me);
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:查询应用
     */
    public R queryWxAppById(String id) {
        Rcd rs = db.uniqueRecord("select * from wx_apps where id=? and dr=0", id);
        if (rs != null) {
            return R.SUCCESS_OPER(rs.toJsonObject());
        }
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:查询应用
     */
    public R queryWxAppByAppId(String appid) {
        Rcd rs = db.uniqueRecord("select * from wx_apps where app_id=? and dr=0", appid);
        if (rs != null) {
            return R.SUCCESS_OPER(rs.toJsonObject());
        }
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:同步微信菜单
     * 当前模式只支持配置一个Appid
     */
    public R syncMenuFromWxWithConf() {
        return syncMenuFromWx(appIdconf, secretconf);
    }

    /**
     * @Description:同步微信菜单
     */
    public R syncMenuFromWxWithId(String id) {
        Rcd rs = db.uniqueRecord("select * from wx_apps where id=?", id);
        if (rs == null || rs.getString("app_id") == null) {
            return R.FAILURE_NO_DATA();
        }
        String appid = rs.getString("app_id");
        if (appid.equals(appIdconf)) {
            return syncMenuFromWx(appIdconf, secretconf);
        } else {
            return R.FAILURE("appid配置不匹配");
        }

    }


    /**
     * @Description:直接查询token
     */
    public R queryWxTokenDirect(boolean flag) {
        return queryWxToken(appIdconf, secretconf, flag);
    }


    /**
     * @Description:同步微信菜单
     */
    public R syncMenuFromWx(String appid, String sec) {
        R tr = queryWxToken(appid, sec, false);
        if (tr.isFailed()) {
            return tr;
        }
        String token = tr.queryDataToJSONObject().getString("access_token");
        log.info("token:" + token);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + token;
        JSONObject json = httpRequest(url, "GET", null);
        JSONObject menu = json.getJSONObject("menu");
        if (ToolUtil.isEmpty(menu)) {
            return R.FAILURE_NO_DATA();
        }
        log.info("menu:" + menu.toJSONString());
        Update me = new Update("wx_apps");
        me.set("menu", menu.toJSONString());
        me.where().and("app_id=?", appid);
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:微信新增菜单
     */
    public R createMenuToWx(String id) {

        Rcd rs = db.uniqueRecord("select * from wx_apps where id=?", id);

        if (rs == null || rs.getString("app_id") == null) {
            return R.FAILURE_NO_DATA();
        }
        String appid = rs.getString("app_id");
        if (!appid.equals(appIdconf)) {
            return R.FAILURE("appid配置不匹配");
        }

        String menu = rs.getString("menu");
        if (ToolUtil.isEmpty(menu)) {
            return R.FAILURE_NO_DATA();
        }

        JSONObject mobj = JSONObject.parseObject(menu);
        if (ToolUtil.isEmpty(mobj)) {
            return R.FAILURE_NO_DATA();
        }
        R trs = queryAccessToken();
        if (trs.isFailed()) {
            return R.FAILURE();
        }
        String token = trs.queryDataToJSONObject().getString("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        log.info("to create menu,url:" + url + ",body:" + menu);
        JSONObject json = httpRequest(url, "POST", menu);
        log.info("result:" + json.toJSONString());
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:网页授权
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_3h_1h, key = "'baseWebOauth2Process'+#state")
    public JSONObject baseWebOauth2Process(String state) {
        // 获取跳转地址
        JSONObject r = new JSONObject();
        Rcd rs = db.uniqueRecord("select * from wx_web_auth where dr=0 and state=?", state);
        String url = "blank";
        String login = "0";
        if (ToolUtil.isNotEmpty(rs)) {
            url = rs.getString("value") == null ? "blank" : rs.getString("value");
            login = rs.getString("login") == null ? "0" : rs.getString("login");
        }
        r.put("url", url);
        r.put("login", login);
        return r;

    }


    /**
     * @Description:网页授权登陆
     */
    public R baseToLogin(String open_id, String login) {
        R ur = null;
        // 处理登录信息
        if ("1".equals(login)) {
            ur = SysUserInfoServiceImpl.selectUserInfoByOpenId(open_id);
            // 用户不存在
            if (ur.isFailed()) {
                // 新建用户
                log.info("###################create user action,open_id:" + open_id + "######################");
                TypedHashMap<String, Object> ps = new TypedHashMap<String, Object>();
                ps.put("open_id", open_id);
                ps.put("locked", "N");
                ps.put("pwd", MD5Util.encrypt(ToolUtil.getUUID()));
                R gtoken = queryWxTokenDirect(false);
                String access_token = gtoken.queryDataToJSONObject().getString("access_token");
                WeixinUserInfo weixinUserInfo = AdvancedUtil.getUerInfo(access_token, open_id);
                log.info("nickname:" + weixinUserInfo.getNickName());
                ps.put("nickname", weixinUserInfo.getNickName());
                ps.put("avatarurl", weixinUserInfo.getHeadImgUrl());
                ps.put("name", weixinUserInfo.getNickName());
                ps.put("sex", weixinUserInfo.getSex());
                SysUserInfo user = new SysUserInfo();
                user.setNickname(weixinUserInfo.getNickName());
                user.setAvatarurl(weixinUserInfo.getHeadImgUrl());
                user.setName(weixinUserInfo.getNickName());
                user.setUserType(UserTypeEnum.WX.getValue().toString());
                user.setOpenId(open_id);
                user.setLocked("N");
                user.setPwd(MD5Util.encrypt(ToolUtil.getUUID()));
                SysUserInfoServiceImpl.save(user);
                // 判断用户是否新建成
                ur = SysUserInfoServiceImpl.selectUserInfoByOpenId(open_id);
                if (ur.isFailed()) {
                    log.info("###################create user failed######################");
                    return R.FAILURE("create user failed.");
                }
            }
            log.info("###################login action,open_id:" + open_id + "######################");
            Subject currentUser = ShiroKit.getSubject();
            String user_id = ur.queryDataToJSONObject().getString("userId");
            String pwd = ur.queryDataToJSONObject().getString("pwd");
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
            if (error.length() > 0) {
                log.info("##################login failed,result:" + error + "######################");
                return R.FAILURE(error);
            }
        }
        log.info("###################login success.######################");
        return R.SUCCESS_OPER(ur.queryDataToJSONObject());
    }

}
