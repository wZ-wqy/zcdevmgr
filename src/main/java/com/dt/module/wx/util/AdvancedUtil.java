package com.dt.module.wx.util;

import com.dt.module.wx.pojo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdvancedUtil {

    private static Logger log = LoggerFactory.getLogger(AdvancedUtil.class);

    /**
     * 上传媒体文件
     *
     * @param accessToken  接口访问凭证
     * @param type         媒体文件类型（image、voice、video和thumb）
     * @param mediaFileUrl 媒体文件的url
     */
    public static WeixinMedia uploadMedia(String accessToken, String type, String mediaFileUrl) {
        WeixinMedia weixinMedia = null;
        // 拼装请求地址
        String uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

        // 定义数据分隔符
        String boundary = "------------7da2e536604c8";
        try {
            URL uploadUrl = new URL(uploadMediaUrl);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");
            // 设置请求头Content-Type
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 获取媒体文件上传的输出流（往微信服务器写数据）
            OutputStream outputStream = uploadConn.getOutputStream();

            URL mediaUrl = new URL(mediaFileUrl);
            HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
            meidaConn.setDoOutput(true);
            meidaConn.setRequestMethod("GET");

            // 从请求头中获取内容类型
            String contentType = meidaConn.getHeaderField("Content-Type");
            // 根据内容类型判断文件扩展名
            String fileExt = WeixinUtil.getFileExt(contentType);
            // 请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(
                    String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt)
                            .getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

            // 获取媒体文件的输入流（读取文件）
            BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                // 将媒体文件写到输出流（往微信服务器写数据）
                outputStream.write(buf, 0, size);
            }
            // 请求体结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            bis.close();
            meidaConn.disconnect();

            // 获取媒体文件上传的输入流（从微信服务器读数据）
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();

            // 使用JSON-lib解析返回结果
            JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
            weixinMedia = new WeixinMedia();
            weixinMedia.setType(jsonObject.getString("type"));
            // type等于thumb时的返回结果和其它类型不一样
            if ("thumb".equals(type))
                weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
            else
                weixinMedia.setMediaId(jsonObject.getString("media_id"));
            weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));
        } catch (Exception e) {
            weixinMedia = null;
            System.out.println("上传媒体文件失败！" + e);
        }
        return weixinMedia;
    }

    /**
     * 下载媒体文件
     *
     * @param accessToken 接口访问凭证
     * @param mediaId     媒体文件标识
     * @param savePath    文件在服务器上的存储路径
     * @return
     */
    public static String getMedia(String accessToken, String mediaId, String savePath) {
        String filePath = null;
        // 拼接请求地址
        String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        System.out.println(requestUrl);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            if (!savePath.endsWith("/")) {
                savePath += "/";
            }
            // 根据内容类型获取扩展名
            String fileExt = WeixinUtil.getFileExt(conn.getHeaderField("Content-Type"));
            // 将mediaId作为文件名
            filePath = savePath + mediaId + fileExt;

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1)
                fos.write(buf, 0, size);
            fos.close();
            bis.close();

            conn.disconnect();
            System.out.println("下载媒体文件成功，filePath=" + filePath);
        } catch (Exception e) {
            filePath = null;
            System.out.println("下载媒体文件失败：{}");
        }
        return filePath;
    }

    /**
     * 查询分组
     *
     * @param accessToken 调用接口凭证
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public static List<WeixinGroup> getGroups(String accessToken) {
        List<WeixinGroup> weixinGroupList = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {

                weixinGroupList = JSONArray.toList(jsonObject.getJSONArray("groups"), WeixinGroup.class);

            } catch (Exception e) {

                System.out.println("获取用户分组失败！");
                e.printStackTrace();
            }

        }
        return weixinGroupList;
    }

    /**
     * 创建分组
     *
     * @param accessToken 接口访问凭证
     * @param groupName   分组名称
     * @return
     */
    public static WeixinGroup createGroup(String accessToken, String groupName) {
        WeixinGroup weixinGroup = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        String jsonMsg = "{\"group\":{\"name\":\"%s\"}}";
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", String.format(jsonMsg, groupName));
        if (null != jsonObject) {
            try {
                weixinGroup = new WeixinGroup();
                weixinGroup.setId(jsonObject.getJSONObject("group").getInt("id"));
                weixinGroup.setName(jsonObject.getJSONObject("group").getString("name"));
            } catch (Exception e) {

                System.out.println("创建分组失败！");
                e.printStackTrace();
            }

        }
        return weixinGroup;
    }

    /**
     * 修改分组名
     *
     * @param accessToken 接口访问凭证
     * @param groupId     分组id
     * @param groupName   修改后的分组名
     * @return true | false
     */
    public static boolean updateGroup(String accessToken, int groupId, String groupName) {
        boolean result = false;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        String jsonMsg = "{\"group\":{\"id\":%d,\"name\":\"%s\"}}";
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", String.format(jsonMsg, groupId, groupName));

        if (null != jsonObject) {
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {
                result = true;
            } else {
                System.out.println("修改分组失败" + errorMsg);
                result = false;
            }
        }
        return result;
    }

    /**
     * 移动用户分组
     *
     * @param accessToken 接口访问凭证
     * @param openId      用户标识
     * @param groupId     分组id
     * @return true | false
     */
    public static boolean updateMemberGroup(String accessToken, String openId, int groupId) {
        boolean result = false;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        String jsonMsg = "{\"openid\":\"%s\",\"to_groupid\":%d}";
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", String.format(jsonMsg, openId, groupId));
        if (null != jsonObject) {
            try {
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                if (0 == errorCode) {
                    result = true;
                } else {
                    System.out.println("移动分组失败" + errorMsg);
                    result = false;
                }
            } catch (Exception e) {

                System.out.println("移动分组失败");
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 获取用户信息
     *
     * @param accessToken 接口访问凭证
     * @param openId      用户标识
     * @return WeixinUserInfo
     */
    public static WeixinUserInfo getUerInfo(String accessToken, String openId) {
        WeixinUserInfo weixinUserInfo = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);

        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        log.info("open_id:" + openId + ",res:" + jsonObject.toString());
        if (null != jsonObject) {
            try {
                weixinUserInfo = new WeixinUserInfo();
                weixinUserInfo.setCity(jsonObject.getString("city"));
                weixinUserInfo.setCountry(jsonObject.getString("country"));
                weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                weixinUserInfo.setLanguage(jsonObject.getString("language"));
                weixinUserInfo.setNickName(jsonObject.getString("nickname"));
                weixinUserInfo.setOpenId(jsonObject.getString("openid"));
                weixinUserInfo.setProvince(jsonObject.getString("province"));
                weixinUserInfo.setSex(jsonObject.getInt("sex"));
                weixinUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
                weixinUserInfo.setSubscribeTime(jsonObject.getInt("subscribe_time"));
            } catch (Exception e) {

                if (0 == weixinUserInfo.getSubscribe()) {
                    System.out.println(weixinUserInfo.getOpenId() + ":用户已经取消关注");
                } else {
                    System.out.println("获取用户信息失败！");
                }
                e.printStackTrace();
            }
        }
        return weixinUserInfo;

    }

    /**
     * 获取网页授权凭证
     *
     * @param appId     公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {

        WeixinOauth2Token wat = null;
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);

        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        log.info("result:" + jsonObject.toString());
        try {
            if (null != jsonObject) {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            }
        } catch (Exception e) {

            wat = null;
            int errorcode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            System.out.println("获取access_token出错,errorcode:" + errorcode + ",errorMsg:" + errorMsg);
            e.printStackTrace();
        }

        return wat;
    }

    /**
     * 刷新网页授权凭证
     *
     * @param appId        公众账号的唯一标识
     * @param refreshToken
     * @return WeixinOauth2Token
     */
    public static WeixinOauth2Token refreshOauth2AccessToken(String appId, String refreshToken) {
        WeixinOauth2Token wat = null;
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("REFRESH_TOKEN", refreshToken);

        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        log.info("result:" + jsonObject.toString());
        try {
            if (null != jsonObject) {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            }
        } catch (Exception e) {

            wat = null;
            int errorcode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            System.out.println("刷新网页授权凭证失败！errorcode:" + errorcode + ",errorMsg:" + errorMsg);
            e.printStackTrace();
        }
        return wat;
    }

    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId      用户标识
     * @return UserInfo
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public static UserInfo getUserInfo(String accessToken, String openId) {
        UserInfo userInfo = null;
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        log.info("result:" + jsonObject.toString());
        try {
            if (null != jsonObject) {
                userInfo = new UserInfo();
                userInfo.setOpenId(jsonObject.getString("openid"));
                userInfo.setNickname(jsonObject.getString("nickname"));
                userInfo.setSex(jsonObject.getInt("sex"));
                userInfo.setCountry(jsonObject.getString("country"));
                userInfo.setProvince(jsonObject.getString("province"));
                userInfo.setCity(jsonObject.getString("city"));
                userInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                userInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
            }
        } catch (Exception e) {

            userInfo = null;
            int errorcode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            System.out.println("获取用户信息失败,errorcode:" + errorcode + ",errorMsg:" + errorMsg);
            e.printStackTrace();
        }
        return userInfo;

    }

    /**
     * 创建临时带参二维码
     *
     * @param accessToken   接口访问凭证
     * @param expireSeconds 二维码有效时间，单位为秒，最大不超过1800
     * @param sceneId       场景ID
     * @return WeixinQRCode
     */
    public static WeixinQRCode createTemporaryQRCode(String accessToken, int expireSeconds, int sceneId) {
        WeixinQRCode weixinQRCode = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        String jsonMsg = "{\"expire_seconds\": %d, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\":%d}}}";
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST",
                String.format(jsonMsg, expireSeconds, sceneId));

        try {
            if (null != jsonObject) {
                weixinQRCode = new WeixinQRCode();
                weixinQRCode.setTicket(jsonObject.getString("ticket"));
                weixinQRCode.setExpireSecond(jsonObject.getInt("expire_seconds"));
                System.out.println("创建二维码成功！");
            }
        } catch (Exception e) {

            System.out.println("创建二维码失败！");
            e.printStackTrace();
        }
        return weixinQRCode;
    }

    /**
     * 创建永久带参二维码
     *
     * @param accessToken 接口访问凭证
     * @param sceneId     场景ID
     * @return ticket
     */
    public static String createPermanentQRCode(String accessToken, int sceneId) {
        String ticket = null;
        String jsonMsg = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", String.format(jsonMsg, sceneId));

        try {
            if (null != jsonObject) {
                ticket = jsonObject.getString("ticket");
                System.out.println("创建二维码成功！");
            }
        } catch (Exception e) {

            System.out.println("创建二维码失败！");
            e.printStackTrace();
        }
        return ticket;
    }

    /**
     * 根据ticket换取二维码
     *
     * @param ticket   二维码ticket
     * @param savePath 保存路径
     */
    public static String getQRCode(String ticket, String savePath) {
        String filePath = null;
        String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
        requestUrl = requestUrl.replace("TICKET", WeixinUtil.urlEncodeUTF8(ticket));

        try {

            TrustManager[] tm = {new WeiXX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");

            if (!savePath.endsWith("/")) {
                savePath += "/";
            }
            filePath = savePath + "first.jpg";

            BufferedInputStream bis = new BufferedInputStream(httpUrlConn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            httpUrlConn.disconnect();
        } catch (Exception e) {

            System.out.println("根据ticket换取二维码失败！");
            e.printStackTrace();
        }
        return filePath;

    }

    /**
     * 获取关注者列表
     *
     * @param accessToken 调用接口凭证
     * @param nextOpenId  第一个拉取的openId，不填默认从头开始拉取
     * @return WeixinUserList
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public static WeixinUserList getUserList(String accessToken, String nextOpenId) {
        WeixinUserList weixinUserList = null;
        if (null == nextOpenId) {
            nextOpenId = "";
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", nextOpenId);
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                weixinUserList = new WeixinUserList();
                weixinUserList.setCount(jsonObject.getInt("count"));
                weixinUserList.setTotal(jsonObject.getInt("total"));
                weixinUserList.setNextOpenId(jsonObject.getString("next_openid"));
                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                weixinUserList.setOpenIdList(JSONArray.toList(dataObject.getJSONArray("openid"), List.class));
            } catch (Exception e) {

                System.out.println("获取关注者列表失败！");
                e.printStackTrace();
            }
        }
        return weixinUserList;

    }

    public static void main(String[] args) {
        // WeixinOauth2Token()
    }
}
