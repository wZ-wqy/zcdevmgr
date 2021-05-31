package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.BaseService;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.ScheduleJob;
import com.dt.module.db.DB;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lank
 * @version 创建时间：2017年8月1日 下午5:50:44 类说明
 */
@Service
public class JobService extends BaseService {

    public static String TYPE_SYS = "sys";

    public static String TYPE_USER = "user";

    @Autowired
    ScheduleMangerService scheduleMangerService = null;

    public static JobService me() {
        return SpringContextUtil.getBean(JobService.class);
    }

    /**
     * 停止Job
     */
    public Boolean finishedJobUpdate(JobExecutionContext jc) {
        ScheduleJob job = (ScheduleJob) jc.getJobDetail().getJobDataMap().get("scheduleJob");
        Update ups = new Update("sys_job");
        ups.setSE("last_run", DbUtil.getDbDateString(DB.instance().getDBType()));
        ups.where().and("seq=?", job.getJobSeq() == null ? "" : job.getJobSeq());
        DB.instance().execute(ups);
        return true;
    }

    /**
     * @Description: 查询job
     */
    public JSONArray queryJob(String type, String user_id) {
        JSONArray data = null;
        data = scheduleMangerService.getJobAll(type, user_id);
        return data;
    }

    /**
     * @Description: 根据id获取ScheduleJob
     */
    public ScheduleJob getScheduleJob(String seq) {
        String sql = "select * from sys_job where seq=?";
        ScheduleJob job = new ScheduleJob();
        job.setJobInstanceValid(false);
        Rcd res = db.uniqueRecord(sql, seq);
        if (ToolUtil.isNotEmpty(res)) {
            job.setJobSeq(res.getString("seq"));
            job.setCronExpression(res.getString("jobcron"));
            job.setJobGroup(res.getString("jobgroup"));
            job.setJobName(res.getString("jobname"));
            job.setJobClassName(res.getString("jobclassname"));
            job.setJobInstanceValid(true);
        } else {
        }
        return job;
    }

    /**
     * @Description: enableJob, 返回job状态
     */
    public JSONObject enableJob(String seq) {
        ScheduleJob job = getScheduleJob(seq);
        if (job.isJobInstanceValid()) {
            db.execute("update sys_job set jobenable='true' where seq=? ", seq);
            scheduleMangerService.jobAdd(job);
        }
        return scheduleMangerService.jobStatus(job);
    }

    /**
     * @Description: disableJob, 返回job状态
     */
    public JSONObject disabledJob(String seq) {
        ScheduleJob job = getScheduleJob(seq);
        if (job.isJobInstanceValid()) {
            db.execute("update sys_job set jobenable='false' where seq= ?", seq);
            scheduleMangerService.jobDel(job);
        }
        return scheduleMangerService.jobStatus(job);
    }

    /**
     * @Description: 删除Job, 返回job状态, 不允许删除sys的job
     */
    public Boolean removejob(String seq, String jobname, String jobgroupname) {
        ScheduleJob job = getScheduleJob(seq);
        job.setJobSeq(seq);
        job.setJobGroup(jobgroupname);
        job.setJobName(jobname);
        if (scheduleMangerService.jobDel(job)) {
            db.execute("delete from sys_job where jobtype<>'" + BaseCommon.getSuperAdmin() + "' and seq=?", seq);
            return true;
        }
        return false;
    }

    /**
     * @Description: 停止job, 返回job状态
     */
    public JSONObject pausejob(String seq) {
        ScheduleJob job = getScheduleJob(seq);
        if (job.isJobInstanceValid()) {
            scheduleMangerService.jobPause(job);
        }
        return scheduleMangerService.jobStatus(job);
    }

    /**
     * @Description: 暂停job, 返回job状态
     */
    public JSONObject resumejob(String seq) {
        ScheduleJob job = getScheduleJob(seq);
        if (job.isJobInstanceValid()) {
            scheduleMangerService.jobResume(job);
        }
        return scheduleMangerService.jobStatus(job);
    }

    /**
     * @Description: job立即运行一次, 返回job状态
     */
    public void runoncejob(String seq) {
        ScheduleJob job = getScheduleJob(seq);
        if (job.isJobInstanceValid()) {
            if (!scheduleMangerService.jobExist(job)) {
                scheduleMangerService.jobAdd(job);
            }
            scheduleMangerService.jobTriggerRun(job);
        }
    }
}
