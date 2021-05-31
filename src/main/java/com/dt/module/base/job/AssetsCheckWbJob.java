package com.dt.module.base.job;

import com.dt.module.zc.service.impl.AssetsJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AssetsCheckWbJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AssetsJobService.me().checkMaintenanceMethod();
    }
}
