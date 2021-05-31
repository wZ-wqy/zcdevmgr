package com.dt.module.flow.base;

import com.bstek.uflo.env.EnvironmentProvider;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.shiro.ShiroUser;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;


public class UfloEnvironmentProvider implements EnvironmentProvider {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    //返回流程引擎需要使用的Hibernate SessionFactory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {

        System.out.println("set flow setSessionFactory "+sessionFactory.getClass());
        this.sessionFactory = sessionFactory;
    }

    public PlatformTransactionManager getPlatformTransactionManager() {
        return platformTransactionManager;
    }

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager)
    {
        System.out.println("set flow PlatformTransactionManager "+platformTransactionManager.getClass());
        this.platformTransactionManager = platformTransactionManager;
    }

    /*
     * getCategoryId方法返回null表示不对流程进行分类处理。只有该值为null 流程设计器里才也可以为空
     * 该值主要用于saas多租户或者独立部署流程引擎时使用
     */
    public String getCategoryId() {
        return null;
    }

    /*
     * getLoginUser方法用于返回当前登录用户的用户id 不是用户名！！！
     */
    public String getLoginUser() {
//    	//返回当前系统的登录用户
        ShiroUser u = ShiroKit.getUser();
        String user = "anonymous";
        if (u != null) {
            user = u.getId();
        }
        System.out.println("getLoginUser:" + user);
        return user;
    }

}