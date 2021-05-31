package com.dt.core.tool.net;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private String[] errorToIgnor = null;
    private int timeout = 3000; // 5s
    private int trys = 2;

    public HttpClient() {
    }

    public void setErrorToIgnor(String[] errorToIgnor) {
        this.errorToIgnor = errorToIgnor;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTrys(int trys) {
        this.trys = trys;
    }

    public String fetchData(String url) {
        return fetchData(url, "GBK");
    }

    public String fetchDataByUTF8(String url) {
        return fetchData(url, "UTF-8");
    }

    public String fetchData(String url, String encoding) {

        int i = 0;

        boolean appendBr = true;
        while (true) {
            try {
                long st = System.currentTimeMillis();
                URL _url = new URL(url);

                HttpURLConnection conn = null;

                conn = (HttpURLConnection) _url.openConnection();

                conn.setConnectTimeout(timeout);
                conn.connect();
                InputStream in = conn.getInputStream();
                BufferedReader bin = new BufferedReader(new InputStreamReader(in, encoding));
                String s = null;
                StringBuffer all = new StringBuffer();

                while ((s = bin.readLine()) != null) {

                    all.append(s);
                    if (appendBr)
                        all.append("\n");
                }
                bin.close();
                st = System.currentTimeMillis() - st;

                return all.toString();
            } catch (Exception e) {
                if (i > trys) {
                    if (e instanceof FileNotFoundException) {
                        System.err.println("404 错误(" + url + ")");
                        return null;
                    }

                    if (errorToIgnor != null)
                        for (String txt : errorToIgnor) {
                            if (e.getMessage().indexOf(txt) != -1) {
                                break;
                            }
                        }
                    else {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
            i++;
        }
    }

}
