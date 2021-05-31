package com.dt.core.cache;

/**
 * 缓存方法注册接口
 */
public interface InvocationRegistry {

    void registerInvocation(CachedInvocation invocatio);

}