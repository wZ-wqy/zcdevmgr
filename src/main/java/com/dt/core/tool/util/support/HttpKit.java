package com.dt.core.tool.util.support;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.dao.util.TypedHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class HttpKit {

    public static Boolean isAjax(HttpServletRequest request) {

        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header);
        return isAjax;

    }

    public static String getIp() {
        return HttpKit.getRequest().getRemoteHost();
    }

    /**
     * 获取所有请求的值
     */
    public static TypedHashMap<String, Object> getRequestParameters() {
        TypedHashMap<String, Object> values = new TypedHashMap<String, Object>();
        HttpServletRequest request = HttpKit.getRequest();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = enums.nextElement();
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }

        return values;
    }

    /**
     * 获取所有请求的值
     */
    public static void printParameters() {
        HttpServletRequest request = HttpKit.getRequest();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            //BaseCommon.print(enums.nextElement() + ":" + request.getParameter((String) enums.nextElement()));
        }
    }

    /**
     * 获取 HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        return response;
    }

    /**
     * 获取 包装防Xss Sql注入的 HttpServletRequest
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return new WafRequestWrapper(request);
    }

    public static JSONObject sendGetWithResp(String url) {
        // String result = "";
        JSONObject res = new JSONObject();
        BufferedReader in = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000)
                    .build();
            httpget.setConfig(requestConfig);
            httpget.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6");
            httpget.addHeader("accept", "*/*");
            httpget.addHeader("connection", "Keep-Alive");
            Long s = System.currentTimeMillis();
            HttpResponse resultRep = httpClient.execute(httpget);
            Long e = System.currentTimeMillis();
            res.put("response_time", e - s);
            res.put("result", EntityUtils.toString(resultRep.getEntity()));
            res.put("code", resultRep.getStatusLine().getStatusCode());
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> param) {
        String result = "";
        BufferedReader in = null;
        try {
            String para = "";
            for (String key : param.keySet()) {
                para += (key + "=" + param.get(key) + "&");
            }
            if (para.lastIndexOf("&") > 0) {
                para = para.substring(0, para.length() - 1);
            }
            String urlNameString = url + "?" + para;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static void sendPost(URI uri) throws java.net.SocketException {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
//            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
//            qparams.add(new BasicNameValuePair("region", String.valueOf(region)));
//            qparams.add(new BasicNameValuePair("platNum", platNum));
            httpPost = new HttpPost(uri);
            // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
            httpPost.setHeader("Content-Type", "application/json;charset=utf8");

            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            //  System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                //  System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                //    System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            //  e.printStackTrace();
        } catch (ParseException e) {
            //  e.printStackTrace();
        } catch (IllegalArgumentException e) {
           // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                //   e.printStackTrace();
            }
        }

    }
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            String para = "";
            for (String key : param.keySet()) {
                para += (key + "=" + param.get(key) + "&");
            }
            if (para.lastIndexOf("&") > 0) {
                para = para.substring(0, para.length() - 1);
            }
            String urlNameString = url + "?" + para;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取请求basePath
     *
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append("://");
        basePath.append(domain);
        if ("http".equalsIgnoreCase(scheme) && 80 != port) {
            basePath.append(":").append(port);
        } else if ("https".equalsIgnoreCase(scheme) && port != 443) {
            basePath.append(":").append(port);
        }
        return basePath.toString();
    }

    /**
     * 获取ip工具类，除了getRemoteAddr，其他ip均可伪造
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("Cdn-Src-Ip"); // 网宿cdn的真实ip
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP"); // 蓝讯cdn的真实ip
        }
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For"); // 获取代理ip
        }
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP"); // 获取代理ip
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 获取代理ip
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 获取真实ip
        }
        return ip;
    }

    @SuppressWarnings("rawtypes")
    public static String removeParam(HttpServletRequest request, String paramName) {
        String queryString = "";
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.equals(paramName)) {
                continue;
            }
            if (queryString.equals("")) {
                queryString = key + "=" + request.getParameter(key);
            } else {
                queryString += "&" + key + "=" + request.getParameter(key);
            }
        }
        return queryString;
    }
}
