package com.dt.module.base.job;

import com.dt.module.base.service.impl.JobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: lank
 * @date: Nov 7, 2017 10:27:05 AM
 * @Description:
 */
public class ClearSessionJob implements Job {

    private static Logger log = LoggerFactory.getLogger(ClearSessionJob.class);

    /**
     * 定期清除session
     */
    @Override
    public void execute(JobExecutionContext jc) throws JobExecutionException {

        log.info("session clear start.");
        JobService.me().finishedJobUpdate(jc);
        log.info("session clear end.");
    }
}
