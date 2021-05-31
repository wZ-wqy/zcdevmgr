package com.dt.module.om.service;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.ISysDbbackupRecService;
import com.dt.module.base.service.ISysFileConfService;
import com.dt.module.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@PropertySource(value = "classpath:config.properties")
public class MySQLDatabaseBackupService extends BaseService {

    @Value("${tool.mysqldump}")
    public String tool_mysqldump;

    private static Logger log = LoggerFactory.getLogger(MySQLDatabaseBackupService.class);

    @Autowired
    ISysFileConfService SysFileConfServiceImpl;

    @Autowired
    ISysDbbackupRecService SysDbbackupRecServiceImpl;

    public static MySQLDatabaseBackupService me() {
        return SpringContextUtil.getBean(MySQLDatabaseBackupService.class);
    }

    /**
     * @Description:获取目录
     */
    public static String getWebRootDir() {
        return ToolUtil.getRealPathInWebApp("");
    }

    /**
     * @Description:检查文件
     */
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

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MySQLDatabaseBackupService d = new MySQLDatabaseBackupService();
        System.out.println(d.decideMysqldumpColumnStatistics());

    }

    /**
     * @Description:备份操作
     */
    public R backupWithZip(String host, int port, String dbName, String username, String password) throws Exception {
        log.info("backupWithZip Start,Info list host:" + host + ",port:" + port + ",db:" + dbName, "username:" + username);
        String id = ToolUtil.getUUID();
        Insert ins = new Insert("sys_dbbackup_rec");
        ins.set("id", id);
        ins.set("dr", "0");
        ins.set("dbname", dbName);
        ins.setSE("create_time", "now()");
        DB.instance().execute(ins.getSQL());
        Update ups = new Update("sys_dbbackup_rec");
        ups.where().and("id=?", id);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Long starttime = System.currentTimeMillis();
        Calendar cale = Calendar.getInstance();
        Date now = cale.getTime();
        String filename = format.format(now) + ".sql";
        String dir = getWebRootDir() + ".." + File.separatorChar;
        String bus_path = "databackup";
        String path = "upload" + File.separatorChar + bus_path + File.separatorChar + cale.get(Calendar.YEAR) + ""
                + File.separatorChar + "" + (cale.get(Calendar.MONTH) + 1) + "" + File.separatorChar;
        String endpath = dir + path;

        R vaf = valid(new File(endpath + filename));
        if (!vaf.isSuccess()) {
            ups.setIf("result", vaf.getMessage());
            DB.instance().execute(ups.getSQL());
            return vaf;
        }

        R bR = backup(host, port, dbName, username, password, endpath + filename);
        if (bR.isFailed()) {
            ups.setIf("result", bR.getMessage());
            DB.instance().execute(ups.getSQL());
            return bR;
        }
        zip(endpath + filename, endpath + filename + ".zip");
        File tf = new File(endpath + filename + ".zip");
        ups.setIf("result", "备份成功");
        ups.setIf("filepath", endpath + filename + ".zip");
        Long endtime = System.currentTimeMillis();
        Long distance = endtime - starttime;
        if (tf != null) {
            ups.setIf("filesize", tf.length() + "");
        }
        ups.setIf("duration", (distance / 1000) + "秒");
        DB.instance().execute(ups.getSQL());
        File sf = new File(endpath + filename);
        if (sf != null) {
            sf.delete();
        }
        return R.SUCCESS();
    }

    /**
     * @Description:获取备份工具
     */
    private String getBackupToolMysqldump() {
        String res = "mysqldump";
        if (ToolUtil.isNotEmpty(tool_mysqldump)) {
            return tool_mysqldump;
        }
        return res;
    }

    /**
     * @Description:备份工具备份参数
     */
    private String decideMysqldumpColumnStatistics() {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            String[] commands = new String[3];
            String os = System.getProperties().getProperty("os.name");
            if (os.startsWith("Win")) {
                commands[0] = "cmd.exe";
                commands[1] = "/c";
            } else {
                commands[0] = "/bin/sh";
                commands[1] = "-c";
            }

            commands[2] = getBackupToolMysqldump() + " --help";
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(commands, null);
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            // 读取输出
            String line;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line + "\n");
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
            // 返回执行结果
            if (result.toString().indexOf("column-statistics") > 0) {
                return "--column-statistics=0";
            }
            return "";
        }

    }

    /**
     * @Description:备份操作
     */
    public R backup(String host, int port, String dbName, String username, String password, String filePath) {
        Long starttime = System.currentTimeMillis();
        JSONObject res = new JSONObject();
        try {
            File file = new File(filePath);
            String[] commands = new String[3];
            String os = System.getProperties().getProperty("os.name");
            if (os.startsWith("Win")) {
                commands[0] = "cmd.exe";
                commands[1] = "/c";
            } else {
                commands[0] = "/bin/sh";
                commands[1] = "-c";
            }
            StringBuilder mysqldump = new StringBuilder();
            mysqldump.append(getBackupToolMysqldump());
            mysqldump.append(" " + decideMysqldumpColumnStatistics() + " ");
            mysqldump.append("  --opt");

            mysqldump.append(" --user=").append(username);
            mysqldump.append(" --password=").append(password);

            mysqldump.append(" --host=").append(host);
            mysqldump.append(" --protocol=tcp");
            mysqldump.append(" --port=").append(port);

            mysqldump.append(" --default-character-set=utf8");
            mysqldump.append(" --single-transaction=TRUE");

            mysqldump.append(" --routines");
            mysqldump.append(" --events");

            mysqldump.append(" ").append(dbName);
            mysqldump.append(" > ");
            mysqldump.append("").append(filePath);

            String command = mysqldump.toString();
            commands[2] = command;
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(commands);
            if (process.waitFor() == 0) {
                Long endtime = System.currentTimeMillis();
                Long distance = endtime - starttime;
                log.info("【" + dbName + "】备份成功，耗时：" + distance + "ms");
                res.put("distance", distance);
                return R.SUCCESS_OPER(res);
            } else {
                InputStream is = process.getErrorStream();
                if (is != null) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    log.info("数据库备【" + dbName + "】份失败\r\n" + sb.toString());
                    return R.FAILURE("数据库备份失败" + sb.toString());
                }
            }
        } catch (Exception e) {
            log.error("数据库备【" + dbName + "】份失败", e);
            return R.FAILURE("数据库备份失败" + e.getMessage());
        }
        return null;
    }

    /**
     * @Description:压缩操作
     */
    public void zip(String sourceFileName, String zipFileName) throws Exception {
        //File zipFile = new File(zipFileName);
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File sourceFile = new File(sourceFileName);
        //调用函数
        compress(out, bos, sourceFile, sourceFile.getName());
        bos.close();
        out.close();
    }

    /**
     * @Description:压缩操作
     */
    public void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                out.putNextEntry(new ZipEntry(base + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                bos.write(tag);
            }
            bis.close();
            fos.close();

        }
    }

}