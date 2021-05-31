package com.dt.module.base.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.SmartImageScalr;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysFileConf;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFileConfService;
import com.dt.module.base.service.ISysFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

@Controller
@RequestMapping("/api/file")
public class FileUpDownController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(FileUpDownController.class);

    public static final String THUMBS = "thumbs";

    @Autowired
    ISysFileConfService SysFileConfServiceImpl;

    @Autowired
    ISysFilesService SysFilesServiceImpl;

    public final static File scale5(String id, String srcImageFile, int height, int width, int origWidth, int origHeigh,
                                    String fitType, String crop) throws IOException {
        if (height <= 0 && width <= 0)
            return new File(srcImageFile);
        File srcFile = new File(srcImageFile);
        String format = null;
        int i = srcFile.getName().lastIndexOf(".");
        if (i > 0) {
            format = srcFile.getName().substring(i + 1);
        }
        if (format == null)
            format = "jpg";
        BufferedImage srcImage = null;
        SmartImageScalr sis = null;
        if (origWidth > 0 && origHeigh > 0) {
            sis = new SmartImageScalr(width, height, fitType, crop, origWidth, origHeigh);
        } else {
            srcImage = ImageIO.read(srcFile);
            sis = new SmartImageScalr(width, height, fitType, crop, srcImage);
        }
        String thumbFilePath = sis.getFileName(srcFile, format);
        thumbFilePath = srcFile.getParentFile().getAbsolutePath() + File.separatorChar + THUMBS + File.separatorChar
                + thumbFilePath;
        File thumbFile = new File(thumbFilePath);
        if (thumbFile.exists())
            return thumbFile;

        if (srcImage == null)
            srcImage = ImageIO.read(srcFile);
        BufferedImage thumbImage = sis.scaleAndCrop(srcImage);
        if (!thumbFile.getParentFile().exists())
            thumbFile.getParentFile().mkdirs();
        ImageIO.write(thumbImage, format, thumbFile);
        return thumbFile;
    }

    private static R valid(File file) {
        File parentPath = file.getParentFile();
        if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
            return R.FAILURE("创建文件失败");
        }
        if (!parentPath.canWrite()) {
            return R.FAILURE("不可写");
        }
        return R.SUCCESS_OPER();
    }

    public static String getWebRootDir() {
        return ToolUtil.getRealPathInWebApp("");
    }


    /**
     * 上传文件、图片
     */
    @RequestMapping("/fileupload.do")
    @ResponseBody
    @Acl(value = Acl.ACL_ALLOW, info = "上传")
    public R fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String bus = request.getParameter("bus");
        if (ToolUtil.isEmpty(bus)) {
            return R.FAILURE("请输入业务号");
        }
        String uuid = request.getParameter("uuid");
        if (ToolUtil.isEmpty(uuid)) {
            uuid = db.getUUID();
        }

        SysFileConf sfcobj = SysFileConfServiceImpl.getById(bus);

        if (sfcobj == null) {
            return R.FAILURE("数据库并未配置");
        }
        if (!"Y".equals(sfcobj.getIsUsed())) {
            return R.FAILURE("该上传配置选型未启用");
        }
        String limit = sfcobj.getLimitStr() == null ? "*" : sfcobj.getLimitStr().trim();
        String type = sfcobj.getType() == null ? "unknow" : sfcobj.getType();
        String keepname = sfcobj.getKeepname() == null ? "0" : sfcobj.getKeepname();
        // 从数据库中获取
        String bus_path = sfcobj.getPath();
        String end_path = "";
        File f = null;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file[0]");
        String OriginalFilename = file.getOriginalFilename();
        String curFilename = "";
        String dir = getWebRootDir() + ".." + File.separatorChar;
        Calendar cale = Calendar.getInstance();
        String path = "upload" + File.separatorChar + bus_path + File.separatorChar + cale.get(Calendar.YEAR) + ""
                + File.separatorChar + "" + (cale.get(Calendar.MONTH) + 1) + "" + File.separatorChar + ""
                + cale.get(Calendar.DAY_OF_MONTH) + "" + File.separatorChar;
        log.info("Upload Dir:" + dir);
        log.info("Type:" + type + ",Upload File:" + OriginalFilename);
        log.info("Upload Path:" + path);
        if ("image".equals(type)) {
            // 获得第1张图片（根据前台的name名称得到上传的文件）
            if (!(OriginalFilename.toUpperCase().endsWith("JPEG") || OriginalFilename.toUpperCase().endsWith("GIF")
                    || OriginalFilename.toUpperCase().endsWith("JPG") || OriginalFilename.toUpperCase().endsWith("PNG")
                    || OriginalFilename.toUpperCase().endsWith("BMP"))) {
                return R.FAILURE("图片后缀名不正确");
            }
            curFilename = "Image_" + uuid + ".png";
            end_path = path + curFilename;
            f = new File(dir + end_path);
            R vaf = valid(f);
            if (!vaf.isSuccess()) {
                return vaf;
            }

        } else if ("file".equals(type)) {
            // 判断上传类型
            if (!ifFileRight(OriginalFilename, limit)) {
                return R.FAILURE("上传的文件后缀不正确");
            }
            if ("1".equals(keepname)) {
                curFilename = OriginalFilename;
                end_path = path + OriginalFilename;
            } else {
                curFilename = "File_" + uuid + ".file";
                end_path = path + curFilename;
            }
            f = new File(dir + end_path);
            R vaf = valid(f);
            if (!vaf.isSuccess()) {
                return vaf;
            }
        } else {
            return R.FAILURE("不支持该上传类型");
        }

        log.info("File:" + f.getAbsolutePath());
        log.info("FileParent:" + f.getParentFile().getAbsolutePath());
        file.transferTo(f);
        Insert ins = new Insert("sys_files");
        ins.set("id", uuid);
        ins.set("path", end_path);
        ins.set("type", type);
        ins.set("dr", "0");
        ins.setIf("filename", curFilename);
        ins.setIf("filename_o", OriginalFilename);
        ins.setSE("create_time", DbUtil.getDbDateString(db.getDBType()));
        ins.set("bus", bus);
        db.execute(ins);
        return R.SUCCESS_OPER();
    }

    public boolean ifFileRight(String name, String limit) {
        if (limit.startsWith("*")) {
            return true;
        }
        String[] limit_arr = limit.split(",");
        for (int i = 0; i < limit_arr.length; i++) {
            if (name.toUpperCase().endsWith(limit_arr[i].toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 下载文件、图片
     */
    @RequestMapping("/filedown.do")
    @Acl(value = Acl.ACL_ALLOW, info = "下载")
    public void filedown(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {


        SysFiles fileobj = SysFilesServiceImpl.getById(id);
        String fileurl = fileobj.getPath();
        String filename = ToolUtil.isEmpty(fileobj.getFilenameO()) ? "unknow.file" : fileobj.getFilenameO();
        String filePath = getWebRootDir() + ".." + File.separatorChar + fileurl;
        File file = new File(filePath);
        System.out.println(file.getName());
        if (file.exists()) {

            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 下载图片
     */
    @RequestMapping("/imagedown.do")
    @Acl(value = Acl.ACL_ALLOW, info = "下载")
    public void imagedown(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (ToolUtil.isEmpty(id)) {
            id = "none";
        }
        // id = "F607ABCD1F4583F41EE166A501572FF6";
        SysFiles fileobj = SysFilesServiceImpl.getById(id);
        File file = null;
        String filePath = "";
        String ct = "";
        try {
            String type = fileobj.getType();
            if ("image".equals(type)) {
                ct = "image/jpeg";
            }
            String fileurl = fileobj.getPath();
            filePath = getWebRootDir() + ".." + File.separatorChar + fileurl;
            log.info("filePath" + filePath);
            String heightStr = request.getParameter("height");
            if (ToolUtil.isEmpty(heightStr)) {
                heightStr = request.getParameter("h");
            }
            String widthStr = request.getParameter("width");
            if (ToolUtil.isEmpty(widthStr)) {
                widthStr = request.getParameter("w");
            }
            String crop = request.getParameter("crop");
            String fit = request.getParameter("fit");
            fit = "width";
            crop = "TL";
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(widthStr);
            } catch (Exception e) {
            }
            try {
                height = Integer.parseInt(heightStr);
            } catch (Exception e) {
            }
            file = new File(filePath);
            log.info("图片位置:" + file.getAbsolutePath());
            if (!file.exists()) {
                log.info("图片不存在,去获取默认图片");
                file = getDefaultImageFile();
            } else {
                log.info("图片存在,开始裁截");
                int ow = 0;
                int oh = 0;
                log.info("height" + height);
                log.info("width" + width);
                file = scale5(id, filePath, height, width, ow, oh, fit, crop);
                if (file == null || !file.exists()) {
                    log.info("裁剪失败,去获取默认图片");
                    file = getDefaultImageFile();
                }
            }
        } catch (Exception e) {
            file = getDefaultImageFile();
        }
        try {
            BufferedImage input = ImageIO.read(file);
            response.reset();
            response.setContentType(ct);
            ImageIO.write(input, "png", response.getOutputStream());
        } catch (Exception e) {
            log.info("获取图片失败:" + filePath);
        }

    }

    /**
     * 默认图片
     */
    public File getDefaultImageFile() {
        log.info("获取默认图片:" + getWebRootDir() + File.separatorChar + "image" + File.separatorChar + "blank.jpg");
        return new File(getWebRootDir() + File.separatorChar + "image" + File.separatorChar + "blank.jpg");
    }
}
