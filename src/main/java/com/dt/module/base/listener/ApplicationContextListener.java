package com.dt.module.base.listener;

import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.common.base.BaseConstants;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.shiro.service.SimpleFilterChainDefinitionsService;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.ScheduleMangerService;
import com.dt.module.base.service.impl.SysInfoService;
import com.dt.module.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;



@Component("myServletContextListener")
@Configuration
@PropertySource(value = "classpath:config.properties")
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Value("${shiro.enable}")
    private String shiroenable;

    @Value("${shiro.updateperm}")
    private String updateperm;

    @Value("${job.enable}")
    private String jobenable;

    @Value("${acl.def}")
    private String acldef;

    @Value("${wx.enable}")
    private String wx_enable;

    @Value("${wx.appId}")
    private String wx_appId;

    @Value("${wx.secret}")
    private String wx_secret;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
        if (null == event.getApplicationContext().getParent()) {
            SpringContextUtil.getApplicationContext();
            log.info(">>>>> spring initialize finish <<<<<");
            // 判断acldef
            if (ToolUtil.isNotEmpty(acldef) && "allow".equals(acldef.trim().toLowerCase())) {
                BaseConstants.acldef = "allow";
            } else {
                BaseConstants.acldef = "deny";
            }

            // 判断shiro
            if (ToolUtil.isNotEmpty(shiroenable) && "true".equals(shiroenable.toLowerCase())) {
                BaseConstants.shiroenable = "true";
            } else {
                BaseConstants.shiroenable = "false";
            }
            // 判断shiroupdateperm
            if (ToolUtil.isNotEmpty(updateperm) && "true".equals(updateperm.toLowerCase())) {
                log.info("Update Shiro Chain");
                SimpleFilterChainDefinitionsService.me().updatePermission();
            } else {
                log.info("No Shiro Module");
            }

            // 判断Job
            if (ToolUtil.isNotEmpty(jobenable) && "true".equals(jobenable.toLowerCase())) {
                log.info("Job Start.");
                ScheduleMangerService scheduleMangerService = ScheduleMangerService.me();
                scheduleMangerService.scheduleStart();
                scheduleMangerService.jobInitLoadFromDb();
            } else {
                log.info("Job Not Start.");
            }

            // 处理微信公众
            if (ToolUtil.isNotEmpty(wx_enable) && "true".equals(wx_enable.toLowerCase())) {
                if (!ToolUtil.isOneEmpty(wx_appId, wx_appId)) {
                    Rcd rs = DB.instance().uniqueRecord("select * from wx_apps where dr=0 and app_id=?", wx_appId);
                    if (ToolUtil.isEmpty(rs)) {
                        Insert me = new Insert("wx_apps");
                        me.set("id", ToolUtil.getUUID());
                        me.set("name", "wx_app");
                        me.set("app_id", wx_appId);
                        me.set("secret", "secret");
                        me.setSE("cdate", DbUtil.getDbDateString(DB.instance().getDBType()));
                        me.set("dr", 0);
                        DB.instance().execute(me);
                        log.info("Insert Wx apps.");
                    } else {
                        Update me = new Update("wx_apps");
                        me.set("secret", "secret");
                        me.where().and("app_id=?", wx_appId);
                        DB.instance().execute(me);
                        log.info("Update Wx apps.");
                    }

                }
            }
            //加载当前配置
            ThreadTaskHelper.run(new Runnable() {
                @Override
                public void run() {
                    SysInfoService.me().uploadSysInfo();
                }
            });


            // 预热
            ThreadTaskHelper.run(new Runnable() {
                @Override
                public void run() {
                    // log.info("预热:regionService.queryRegion");
                    // regionService.queryRegion();
                    // log.info("预热:regionService.queryRegionALL");
                    // regionService.queryRegionALL();
                    // log.info("预热:systemService.queryMsg");
                    // systemService.queryMsg();
                }
            });

        }
    }
}
