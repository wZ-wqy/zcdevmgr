package com.dt.core.cache;

/**
 * 主动刷新缓存接口
 */
public interface CacheSupport {

    /**
     * 刷新容器中所有值
     *
     * @param cacheName
     */
    void refreshCache(String cacheName);

    /**
     * 按容器以及指定键更新缓存
     *
     * @param cacheName
     * @param cacheKey
     */
    void refreshCacheByKey(String cacheName, String cacheKey);

    /**
     * 按容器以及指定键删除缓存
     *
     * @param cacheName
     * @param cacheKey
     */
    void removeCacheByKey(String cacheName, String cacheKey);

}