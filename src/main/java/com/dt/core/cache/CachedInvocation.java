package com.dt.core.cache;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 标记了缓存注解的方法类信息 用于主动刷新缓存时调用原始方法加载数据
 */
public final class CachedInvocation {

    private final Object targetBean;

    private final Method targetMethod;

    private Object key;
    
    private Object[] arguments;

    private CacheableEntity cacheableEntity;

    public CachedInvocation(Object key, Object targetBean, Method targetMethod, Object[] arguments,
                            CacheableEntity cacheableEntity) {

        this.cacheableEntity = cacheableEntity;
        this.key = key;
        this.targetBean = targetBean;
        this.targetMethod = targetMethod;
        if (arguments != null && arguments.length != 0) {
            this.arguments = Arrays.copyOf(arguments, arguments.length);
        }
    }

    /**
     * @return the cacheableEntity
     */
    public CacheableEntity getcacheableEntity() {
        return cacheableEntity;
    }

    /**
     * @param cacheableEntity the cacheableEntity to set
     */
    public void setCacheableEntity(CacheableEntity cacheableEntity) {
        this.cacheableEntity = cacheableEntity;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object getKey() {
        return key;
    }

}
