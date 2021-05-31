package com.dt.module.base.job;


import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.JobService;
import com.dt.module.om.service.MySQLDatabaseBackupService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.*;
import java.util.Properties;


public class DatabackupJob implements Job {



    /**
     * 定期备份数据库
     */
    @Override
    public void execute(JobExecutionContext jc) throws JobExecutionException {
        Properties properties = new Properties();
        System.out.println(ToolUtil.getRealPathInWebApp("") + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "config.properties");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(ToolUtil.getRealPathInWebApp("") + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jdbcurl = properties.getProperty("jdbc.url");
        String jdbcusername = properties.getProperty("jdbc.username");
        String jdbcpassword = properties.getProperty("jdbc.password");

        String[] jdbcarr = jdbcurl.split(":");
        String ip = jdbcarr[2].replaceAll("//", "");
        String portstr = jdbcarr[3].split("/")[0];
        String dbname = jdbcarr[3].replaceAll("\\?", "/").split("/")[1];

        try {
            MySQLDatabaseBackupService.me().backupWithZip(ip, ConvertUtil.toInt(portstr, 3306), dbname, jdbcusername, jdbcpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JobService.me().finishedJobUpdate(jc);
    }
}
