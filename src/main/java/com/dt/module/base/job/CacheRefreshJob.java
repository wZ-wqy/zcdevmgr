package com.dt.module.base.job;

import com.dt.module.base.service.impl.CacheService;
import com.dt.module.base.service.impl.JobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: lank
 * @date: 2020年4月4日 下午3:15:52
 * @Description:
 */
public class CacheRefreshJob implements Job {

    private static Logger log = LoggerFactory.getLogger(CacheRefreshJob.class);

    /**
     * 主动刷新缓存Job
     */
    @Override
    public void execute(JobExecutionContext jc) throws JobExecutionException {
        log.info("CacheRefreshJob start.");
        CacheService.me().refreshCaches();
        log.info("CacheRefreshJob end.");
        JobService.me().finishedJobUpdate(jc);
    }


}
