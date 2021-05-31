package com.dt.core.shiro.service;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseConstants;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 安全框架角色资源配置服务类
 *
 * @author lank
 */
public class SimpleFilterChainDefinitionsService {
    /**
     * 默认premission字符串
     */
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";
    private final static Logger log = LoggerFactory.getLogger(SimpleFilterChainDefinitionsService.class);
    private String filterChainDefinitions;
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    public static SimpleFilterChainDefinitionsService me() {
        return SpringContextUtil.getBean(SimpleFilterChainDefinitionsService.class);
    }

    @PostConstruct
    public void init() {
        log.info("init.");
    }

    /**
     * 读取配置资源
     */
    public Section obtainPermission() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        log.info("size:" + section.size());
        return section;
    }

    public Map<String, String> initCustomPermission() {
        HashMap<String, String> res = new HashMap<String, String>();
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
                if (Acl.ACL_ALLOW.equals(aclvalue)) {
                    Iterator<String> it = pSet.iterator();
                    while (it.hasNext()) {
                        String str = it.next();
                        res.put(str, "anon");
                    }
                } else if (Acl.ACL_USER.equals(aclvalue)) {
                    Iterator<String> it = pSet.iterator();
                    while (it.hasNext()) {
                        String str = it.next();
                        res.put(str, "authc,user");
                    }
                } else {

                }
            } else {
                //没有设置acl
                if (BaseConstants.acldef.equals("allow")) {
                    Iterator<String> it = pSet.iterator();
                    while (it.hasNext()) {
                        String str = it.next();

                        res.put(str, "anon");
                    }
                }
            }

        }
        return res;
    }

    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            // 获取过滤管理器
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            // 清空初始权限配置
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 重新构建生成
            shiroFilterFactoryBean.setFilterChainDefinitions(filterChainDefinitions);
            // initPublicPermission
            Map<String, String> publicchains = initCustomPermission();
            for (Map.Entry<String, String> entry : publicchains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                log.debug("initCustomPermission:" + url + "," + chainDefinition);
                manager.createChain(url, chainDefinition);
            }
            // 加载chainDefinition中的
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                log.debug("initStaticChainDefinition:" + url + " " + chainDefinition);
                manager.createChain(url, chainDefinition);
            }
            log.info("update shiro permission success...");
        }
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public Class<? extends SimpleFilterChainDefinitionsService> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}
