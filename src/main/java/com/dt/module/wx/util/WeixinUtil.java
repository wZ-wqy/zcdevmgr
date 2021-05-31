package com.dt.module.wx.util;


import com.dt.module.wx.pojo.AccessToken;
import com.dt.module.wx.pojo.Menu;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 公众平台通用接口工具类
 */
public class WeixinUtil {

    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public final static String access_token_cor_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=corid&corpsecret=corsecrect";
    public static String corpId = "";
    //经营动态appSecret
    public static String appSecret = "";
    //消息提醒appSecret
    public static String xiaoxi_appSectet = "";
    //https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
    //public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static String menu_create_url = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=1";


    public static AccessToken getAccessToken(String appid, String appsecret) {

        AccessToken accessToken = null;
        String requestUrl = access_token_cor_url.replaceAll("corid", appid).replaceAll("corsecrect", appsecret);
        System.out.println("requestUrl=" + requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (Exception e) {

                accessToken = null;
                System.out.println("accessToken is null!");
                e.printStackTrace();
            }
        }
        return accessToken;
    }

    public static AccessToken getAccessToken1(String appid1, String appsecret1) {

        AccessToken accessToken = null;
        String requestUrl = access_token_cor_url.replaceAll("corid", appid1).replaceAll("corsecrect", appsecret1);


        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (Exception e) {

                accessToken = null;
                System.out.println("accessToken is null!");
                e.printStackTrace();
            }
        }
        return accessToken;
    }


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
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
            jsonObject = JSONObject.fromObject(buffer.toString());

        } catch (ConnectException ce) {
            //log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            //log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 创建菜单
     *
     * @param menu        菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                //log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    public static String urlEncodeUTF8(String sourceUrl) {
        String result = sourceUrl;

        try {
            result = java.net.URLEncoder.encode(sourceUrl, "utf-8");

        } catch (UnsupportedEncodingException e) {

            System.out.println("URL编码失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType))
            fileExt = ".jpg";
        else if ("audio/mpeg".equals(contentType))
            fileExt = ".mp3";
        else if ("audio/amr".equals(contentType))
            fileExt = ".amr";
        else if ("video/mp4".equals(contentType))
            fileExt = ".mp4";
        else if ("video/mpeg4".equals(contentType))
            fileExt = ".mp4";
        return fileExt;
    }

    public static String formatTime(String createTime) {
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(msgCreateTime));
    }

    /**
     * 数据提交与请求通用方法
     *
     * @param access_token 凭证
     * @param RequestMt    请求方式
     * @param RequestURL   请求地址
     * @param outstr       提交json数据
     */
    public static int PostMessage(String access_token, String RequestMt, String RequestURL, String outstr) {
        int result = 0;
        RequestURL = RequestURL.replace("ACCESS_TOKEN", access_token);
        System.out.println(RequestURL);
        JSONObject jsonobject = httpRequest(RequestURL, RequestMt, outstr);
        System.out.println(jsonobject);
        if (null != jsonobject) {
            if (0 != jsonobject.getInt("errcode")) {
                result = jsonobject.getInt("errcode");
                String error = String.format("操作失败 errcode:{} errmsg:{}", jsonobject.getInt("errcode"), jsonobject.getString("errmsg"));
                System.out.println(error);
            }
        }
        return result;
    }


}