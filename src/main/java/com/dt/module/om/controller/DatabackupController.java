package com.dt.module.om.controller;


import com.dt.core.annotion.Acl;
import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.module.om.service.MySQLDatabaseBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author: lank
 * @date: 2020年1月18日 上午9:06:42
 * @Description:
 */
@Controller
@RequestMapping("/api")
@PropertySource(value = "classpath:config.properties")
public class DatabackupController<MySQLDatabaseBackupServicem> extends BaseController {

    @Autowired
    MySQLDatabaseBackupService mySQLDatabaseBackupService;

    @Value("${jdbc.url}")
    private String jdbcurl;

    @Value("${jdbc.username}")
    private String jdbcusername;

    @Value("${jdbc.password}")
    private String jdbcpassword;


    /**
     * @Description:数据库备份操作
     */
    @RequestMapping(value = "/databackup.do")
    @ResponseBody
    @Acl(value = Acl.ACL_DENY, info = "backup")
    public R databackup() {
        String[] jdbcarr = jdbcurl.split(":");
        String ip = jdbcarr[2].replaceAll("//", "");
        String portstr = jdbcarr[3].split("/")[0];
        String dbname = jdbcarr[3].replaceAll("\\?", "/").split("/")[1];
        ThreadTaskHelper.run(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLDatabaseBackupService.me().backupWithZip(ip, ConvertUtil.toInt(portstr, 3306), dbname, jdbcusername, jdbcpassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return R.SUCCESS("后台备份运行中,请稍后查询执行结果...");
    }

}
