package com.dt.core.tool.lang;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 根据bean名称获取指定类型bean
     *
     * @param beanName bean名称
     * @param clazz    返回的bean类型,若类型不匹配,将抛出异常
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    private static void assertApplicationContext() {
        if (SpringContextUtil.applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextUtil!");
        }
    }

    /**
     * bean的类型
     *
     * @param beanName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getType(String beanName) {
        return applicationContext.getType(beanName);
    }

    /**
     * 是否是单例
     *
     * @param beanName
     * @return
     */
    public static boolean isSingleton(String beanName) {
        return applicationContext.isSingleton(beanName);
    }

    /**
     * 是否包含bean
     *
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

}
