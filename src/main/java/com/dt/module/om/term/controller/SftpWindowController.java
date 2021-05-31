package com.dt.module.om.term.controller;

import ch.ethz.ssh2.SFTPException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.om.term.entity.Machine;
import com.dt.module.om.term.websocket.SftpClient;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lank
 * @date: 2020年1月18日 上午9:06:42
 * @Description:
 */
@Controller
@RequestMapping("/api")
public class SftpWindowController<MySQLDatabaseBackupServicem> extends BaseController {
    private Map<String, Object> sftpsession;
    private Map<String, Object> sftpuploadSession = new HashMap<String, Object>();


    @RequestMapping(value = "/sftp/exeCommand.do", method = RequestMethod.POST)
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "执行sftp的命令")
    public R exeCommand(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile, String cmd,
                        String cmdParam, String fileFileName, String permissions, HttpServletRequest request,
                        HttpServletResponse response) {

        SftpClient sftp = getSftpClient(getUserId());
        if (sftp != null && sftp.isConnected()) {
            try {

                switch (cmd) {
                    case "cd":
                        sftp.changeDirectory(cmdParam);
                        break;
                    case "rm":
                        sftp.deleteFile(cmdParam);
                        break;
                    case "mkdir":
                        sftp.mkDir(cmdParam);
                        break;
                    case "upload":
                        File file = null;
                        if (uploadFile == null || uploadFile.isEmpty()) {
                            return R.FAILURE("请选择文件");
                        }

                        CommonsMultipartFile cf = (CommonsMultipartFile) uploadFile;
                        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
                        file = fi.getStoreLocation();
                        // 手动创建临时文件
                        // if (file.length() < 2048) {
                        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
                                + file.getName());
                        uploadFile.transferTo(tmpFile);
                        file = tmpFile;
                        // }
                        sftp.uploadFile(file, cf.getOriginalFilename(), sftpuploadSession);
                        break;
                    case "attr":
                        sftp.setAttributes(fileFileName, Integer.valueOf("" + permissions, 8));
                        break;
                }
                String json = JSON.toJSONString(sftp.ls());
                json = new String(json.getBytes("GBK"), StandardCharsets.UTF_8);
                return R.SUCCESS_OPER(JSONArray.parse(json));

            } catch (SFTPException ex) {
                return R.FAILURE("权限不够，操作失败！");
            } catch (IOException e) {
                e.printStackTrace();
                return R.FAILURE("执行命令错误");
            }

        } else {
            return R.FAILURE("连接错误");
        }

    }

    private SftpClient getSftpClient(String user_id) {
        return (SftpClient) sftpsession.get(user_id);
    }

    @RequestMapping("/sftp/downloadFile.do")
    @Acl(value = Acl.ACL_DENY, info = "使用sftp下载文件")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        String fileFileName = "";
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null && values.length > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    builder.append(values[i] + "");
                }
                if (name.equals("fileFileName")) {
                    fileFileName = builder.toString();
                }
            }
        }
        SftpClient sftp = getSftpClient(getUserId());

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + java.net.URLEncoder.encode(fileFileName, "UTF-8"));
        try {
            InputStream inStream = sftp.downloadFile(fileFileName);
            OutputStream myout = response.getOutputStream();

            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buff)) != -1) {
                myout.write(buff, 0, len);
            }
            myout.close();
            inStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @RequestMapping("/sftp/uploadState.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "更新状态")
    public R uploadState(String id) {

        String state = (String) sftpuploadSession.get("progress");
        return R.SUCCESS_OPER(JSONObject.parse(state));

    }

    @RequestMapping("/sftp/connectSftp.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "sftp连接")
    public R connectSftp(String id) {
        String user_id = getUserId();
        Machine m = (Machine) super.getSession().getAttribute("currentMachine");
        if (ToolUtil.isEmpty(user_id)) {
            return R.FAILURE_NOT_LOGIN();
        }
        if (ToolUtil.isEmpty(m)) {
            return R.FAILURE("为选择Machine");
        }
        SftpClient sftp = new SftpClient(m, "");
        if (sftp != null && !sftp.isConnected()) {
            return R.FAILURE("无法登录");
        }
        try {
            String json = JSON.toJSONString(sftp.ls());
            json = new String(json.getBytes("GBK"), StandardCharsets.UTF_8);
            if (sftpsession == null) {
                sftpsession = new HashMap<String, Object>();
            }

            SftpClient tmpsftp = getSftpClient(getUserId());
            if (tmpsftp != null) {
                tmpsftp.close();
                sftpsession.remove(user_id);
            }
            sftpsession.put(user_id, sftp);

            return R.SUCCESS_OPER(JSONArray.parse(json));
            // inputStream = new ByteArrayInputStream(json.getBytes("UTF-8"));
            // put the sftp client into session
        } catch (IOException e) {
            return R.FAILURE("Machine连接失败");
        }
    }
}
