package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.BaseService;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.ReflectKit;
import com.dt.module.base.entity.ScheduleJob;
import com.google.common.collect.Maps;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScheduleMangerService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(ScheduleMangerService.class);

    private SchedulerFactory sf = new StdSchedulerFactory();

    public static ScheduleMangerService me() {
        return SpringContextUtil.getBean(ScheduleMangerService.class);
    }

    /**
     * @Description: 从数据库中初始化Job状态
     */
    public void jobInitLoadFromDb() {
        String sql = "select * from sys_job where dr='0' and jobenable='true' and inited='Y'";
        RcdSet res = db.query(sql);
        for (int i = 0; i < res.size(); i++) {
            ScheduleJob job = new ScheduleJob();
            job.setJobSeq(res.getRcd(i).getString("seq"));
            job.setCronExpression(res.getRcd(i).getString("jobcron"));
            job.setJobGroup(res.getRcd(i).getString("jobgroup"));
            job.setJobName(res.getRcd(i).getString("jobname"));
            job.setJobClassName(res.getRcd(i).getString("jobclassname"));
            jobAdd(job);
        }
    }

    /**
     * @Description: 停止schedule管理
     */
    public void scheduleStop() {
        try {
            sf.getScheduler().shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Boolean scheduleisShutdown() {
        try {
            Boolean isShutdown = sf.getScheduler().isShutdown();
            return isShutdown;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @Description: 获取schedule状态
     */
    public void schedulegGetStatus() {
        try {
            Boolean isShutdown = sf.getScheduler().isShutdown();
            if (isShutdown) {
                log.info("this is stop");
            } else {
                log.info("this is start");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 启动schedule管理
     */
    public void scheduleStart() {
        try {
            sf.getScheduler().start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 获取所有Job的状态, 只供前端使用
     */
    @SuppressWarnings("rawtypes")
    public JSONArray getJobAll(String type, String user_id) {

        String sql = "select t.*,'本地执行' nodename from sys_job t where 1=1 ";
        Map<String, ScheduleJob> jobs = Maps.newLinkedHashMap();
        RcdSet res = db.query(sql);
        for (int i = 0; i < res.size(); i++) {
            ScheduleJob job = new ScheduleJob();
            job.setJobSeq(res.getRcd(i).getString("seq"));
            job.setNode(res.getRcd(i).getString("node"));
            job.setNodeName(res.getRcd(i).getString("nodename"));
            job.setJobName(res.getRcd(i).getString("jobname"));
            job.setJobGroup(res.getRcd(i).getString("jobgroup"));
            job.setJobClassName(res.getRcd(i).getString("jobclassname"));
            job.setCronExpression(res.getRcd(i).getString("jobcron"));
            job.setJobType(res.getRcd(i).getString("jobtype"));
            job.setJobEnable(res.getRcd(i).getString("jobenable"));
            job.setJobDesc(res.getRcd(i).getString("mark"));
            job.setLastRun(res.getRcd(i).getString("last_run"));
            job.setJobSource("tab");
            jobs.put(res.getRcd(i).getString("seq"), job);
        }
        Scheduler scheduler;
        try {
            scheduler = sf.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    jobKey.getName().substring(jobKey.getName().indexOf("#idle#"));
                    String seqtmp = jobKey.getName().substring(jobKey.getName().indexOf("#idle#") + 6);
                    if (jobs.containsKey(seqtmp)) {
                        jobs.get(seqtmp).setJobPlanStatus(triggerState.name());
                        jobs.get(seqtmp).setJobTrigger(trigger.getKey() + "");
                    } else {
                        ScheduleJob job = new ScheduleJob();
                        // 格式jobnametest#idle#16051e6d2fdbf49cc3b7b58b57a6acaf
                        job.setJobPlanStatus(triggerState.name());
                        job.setJobTrigger(trigger.getKey() + "");
                        job.setJobGroup(jobKey.getGroup());
                        job.setJobName(jobKey.getName().replaceFirst(seqtmp, "").replaceFirst("#idle#", ""));
                        job.setJobSeq(seqtmp);
                        job.setJobSource("scheplan");
                        job.setJobEnable("true");
                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            String cronExpression = cronTrigger.getCronExpression();
                            job.setCronExpression(cronExpression);
                        }
                        jobs.put(seqtmp, job);
                    }
                }
            } // end JobKey
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        JSONArray data = new JSONArray();
        JSONObject object = null;
        Iterator iteratorjob = jobs.keySet().iterator();
        while (iteratorjob.hasNext()) {
            object = new JSONObject();
            Object o = iteratorjob.next();
            String key = (String) o;
            ScheduleJob value = jobs.get(key);
            String jobtype = value.getJobType();
            object.put("seq", value.getJobSeq());
            object.put("node", value.getNode());
            object.put("nodename", value.getNodeName());
            object.put("jobname", value.getJobTrueName());
            object.put("jobgroup", value.getJobGroup());
            object.put("jobcron", value.getCronExpression());
            object.put("jobcontent", value.getCronExpression() + "  " + value.getJobClassName());
            object.put("jobclassname", value.getJobClassName());
            object.put("jobenable", value.getJobEnable());
            object.put("jobrunstatus", value.getJobPlanStatus());
            object.put("jobtrigger", value.getJobTrigger());
            object.put("jobtype", jobtype);
            object.put("last_run", value.getLastRun());
            object.put("mark", value.getJobDesc());
            Boolean ifAdd = false;
            if (ToolUtil.isEmpty(jobtype)) {
                ifAdd = true;
            } else {
                // type为空或匹配
                ifAdd = ToolUtil.isEmpty(type) || jobtype.equals(type);
                // 去掉sys类型的数据
                if (jobtype.equals(JobService.TYPE_SYS)) {
                    ifAdd = false;
                }
                // 判断是否要添加sys的数据
                if (ToolUtil.isNotEmpty(user_id) && BaseCommon.isSuperAdmin(user_id)) {
                    if (jobtype.equals(JobService.TYPE_SYS)) {
                        ifAdd = true;
                    }
                }
            }
            if (ifAdd) {
                data.add(object);
            }
        }
        return data;
    }

    /**
     * @Description: 获取单个Job的状态
     */
    public void jobInfo(ScheduleJob job) {
        Scheduler scheduler;
        try {
            scheduler = sf.getScheduler();
            JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
            log.info("this job " + scheduler.checkExists(jobKey));
            log.info("this groupnames " + scheduler.getJobGroupNames());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 获取单个Job的状态
     */
    public JSONObject jobStatus(ScheduleJob job) {
        JSONObject object = new JSONObject();
        object.put("jobPlanStatus", "");
        Scheduler scheduler;
        try {
            scheduler = sf.getScheduler();
            JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (triggers.size() == 1) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(triggers.get(0).getKey());
                String status = triggerState.name();
                object.put("jobPlanStatus", status);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * @Description: 添加Job
     */
    public boolean jobAdd(ScheduleJob job) {
        log.info("Job add:" + job.getJobClassName() + "," + job.getJobSeq());
        if (jobExist(job)) {
            return true;
        }
        Scheduler scheduler;
        try {
            scheduler = sf.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobRunName(), job.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                /* 创建JobDetail */
                Class<Job> jobjf = ReflectKit.on(job.getJobClassName()).get();
                JobDetail jobDetail = ReflectKit.on("org.quartz.JobBuilder")
                        .call("newJob", jobjf.newInstance().getClass())
                        .call("withIdentity", job.getJobRunName(), job.getJobGroup()).call("build").get();
                jobDetail.getJobDataMap().put("scheduleJob", job);
                /* 表达式调度构建 */
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                /* 按新的cronExpression表达式构建一个新的trigger */
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobRunName(), job.getJobGroup())
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                return true;
            } else {
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 删除Job
     */
    public Boolean jobDel(ScheduleJob job) {
        log.info("Job delete:" + job.getJobClassName() + "," + job.getJobSeq());
        JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @Description: 触发Job
     */
    public void jobTriggerRun(ScheduleJob job) {
        log.info("Job trigger:" + job.getJobClassName() + "," + job.getJobSeq());
        JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 停止job
     */
    public void jobPause(ScheduleJob job) {
        log.info("Job pause:" + job.getJobClassName() + "," + job.getJobSeq());
        JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: job是否存在于队列
     */
    public boolean jobExist(ScheduleJob job) {
        JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
        try {
            Scheduler scheduler = sf.getScheduler();
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 暂停job
     */
    public void jobResume(ScheduleJob job) {
        log.info("Job resume:" + job.getJobClassName() + "," + job.getJobSeq());
        JobKey jobKey = JobKey.jobKey(job.getJobRunName(), job.getJobGroup());
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
