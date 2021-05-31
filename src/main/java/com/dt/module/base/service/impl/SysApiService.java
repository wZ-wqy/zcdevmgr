package com.dt.module.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysApi;
import com.dt.module.base.mapper.SysApiMapper;
import com.dt.module.base.service.ISysApiService;
import com.dt.module.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
@Service
public class SysApiService extends ServiceImpl<SysApiMapper, SysApi> implements ISysApiService {

    @Autowired
    DB db;

    private static Logger log = LoggerFactory.getLogger(SysApiService.class);

    public static SysApiService me() {
        return SpringContextUtil.getBean(SysApiService.class);
    }

    /**
     * @Description: 更新接口地址
     */
    public R updateApi() {
        List<SQL> sqls = new ArrayList<SQL>();
        WebApplicationContext wc = (WebApplicationContext) SpringContextUtil.getApplicationContext();
        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo rmi = entry.getKey();
            PatternsRequestCondition pc = rmi.getPatternsCondition();
            Set<String> pSet = pc.getPatterns();
            HandlerMethod hm = entry.getValue();
            Acl am = hm.getMethodAnnotation(Acl.class);
            if (ToolUtil.isNotEmpty(am)) {
                String aclvalue = am.value();
                String aclinfo = am.info();
                String type = am.type();
                Iterator<String> it = pSet.iterator();
                while (it.hasNext()) {
                    String str = it.next();
                    Insert me = new Insert("sys_api");
                    me.set("id", db.getUUID());
                    me.setIf("ct", str);
                    me.setIf("ctacl", aclvalue);
                    me.setIf("apitype", "url");
                    me.setIf("info", aclinfo);
                    me.setIf("type", type);
                    me.setSE("rectime", DbUtil.getDbDateString(db.getDBType()));
                    sqls.add(me);
                }
            }

        }
        if (sqls.size() > 0) {
            log.info("Save collect Api.");
            db.execute("truncate table sys_api");
            db.executeSQLList(sqls);
        } else {
            log.info("Save collect Api failed.");
        }
        return R.SUCCESS_OPER();
    }

}
