package com.dt.module.zbx.controller;


import com.dt.core.annotion.Acl;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zbx.service.impl.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


@Controller
@RequestMapping("/api/zbx/image")
@Configuration
@PropertySource(value = "classpath:config.properties")
public class ImageController {

    @Value("${zbx.user}")
    public String zbxuser;

    @Value("${zbx.pwd}")

    public String zbxpwd;

    @Value("${zbx.server}")
    public String zbxserver;

    @Autowired
    ImageService imageService;


    /**
     * @Description:获取图形
     */
    @Acl(info = "获取图形", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getOneByGraphId.do")
    public void getOneByGraphId(String width, String graphid, String start, String end, HttpServletRequest request, HttpServletResponse response) {

        String url = zbxserver + "/chart2.php?";
        if (ToolUtil.isEmpty(width)) {
            width = "800";
        }
        String furl = "";
        String imagefilename = "/tmp/" + ToolUtil.getUUID() + graphid + ".png";
        String getstr = "graphid=" + graphid + "&from=" + start + "&to=" + end + "&height=250&width=" + width + "&profileIdx=web.charts.filter&_=ud0q8iun";
        try {
            String getstrEn = URLEncoder.encode(getstr, "UTF-8");
            furl = url + getstr;
            System.out.println(furl);
            URL restURL = new URL(furl);
            HttpURLConnection connection = (HttpURLConnection) restURL.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            FileOutputStream file = new FileOutputStream(new File(imagefilename));
            byte[] bytes = new byte[1024 * 1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) != -1) {
                file.write(bytes, 0, length);
            }
            file.close();
            inputStream.close();
            BufferedImage input = ImageIO.read(new File(imagefilename));
            response.reset();
            response.setContentType("image/jpeg");
            ImageIO.write(input, "png", response.getOutputStream());
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

