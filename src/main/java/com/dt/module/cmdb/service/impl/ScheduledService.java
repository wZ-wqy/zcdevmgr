package com.dt.module.cmdb.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.module.base.service.impl.SysInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lank
 * @date: Nov 2, 2019 2:40:03 PM
 * @Description:
 */
@Service
@EnableScheduling
@Lazy(false)
public class ScheduledService extends BaseService {



    @Scheduled(cron = "0 */59 * * * ? ")
    public void autoUploadSysInfo() {
        SysInfoService.me().uploadSysInfo();
    }

    /**
     * @Description:自动计算维保是否过期
     */
    @Scheduled(cron = "0 */30 * * * ? ")
    public void checkMaintenanceStatus() {
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);

        // 转脱保
        String sql1 = "update  res set wb='invalid' where id in (       select t.id from (         select id   "
                + "      from res   "
                + "      where wbout_date is not null and dr = 0 and    (wb <> 'invalid' or wb is null)   and wb_auto = '1'   "
                + "            and wbout_date < now()       ) t    )";
        int invalid = db.execute(sql1);
        // 转在保
        String sql2 = "update  res set wb='valid' where id in (       select t.id from (      select id   "
                + "  from res   "
                + "  where wbout_date is not null and dr = 0 and (wb <> 'valid' or wb is null)  and wb_auto = '1'   "
                + "        and wbout_date > now())t      )";
        int valid = db.execute(sql2);


    }
}
