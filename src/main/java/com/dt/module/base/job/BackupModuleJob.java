package com.dt.module.base.job;

import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.DbUtil;
import com.dt.module.base.service.impl.JobService;
import com.dt.module.db.DB;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author: lank
 * @date: 2017年11月9日 下午2:30:20
 * @Description:
 */
public class BackupModuleJob implements Job {

    /**
     * 备份系统模块
     */
    @Override
    public void execute(JobExecutionContext jc) throws JobExecutionException {

        String sql = "insert into sys_modules_item_history select t.*,"
                + DbUtil.getDbDateString(DB.instance().getDBType()) + ",'" + MD5Util.encrypt(new Date().getTime() + "")
                + "' from sys_modules_item t";
        DB.instance().execute(sql);
        JobService.me().finishedJobUpdate(jc);
    }
}
