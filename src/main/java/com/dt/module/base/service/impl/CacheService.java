package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.cache.*;
import com.dt.core.common.base.R;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.DateTimeKit;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * @author: lank
 * @date: 2020年3月30日 下午6:06:12
 * @Description:
 */
@Service
public class CacheService {

    @Autowired
    private CacheSupportImpl cacheSupportImpl;

    @Autowired
    private CacheManager cacheManager;

    public static String API_CACHE = "public";

    private static Logger log = LoggerFactory.getLogger(CacheService.class);

    public static CacheService me() {
        return SpringContextUtil.getBean(CacheService.class);
    }

    public CacheManager initCacheManager() {
        try {
            if (cacheManager == null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheManager;
    }


    /**
     * 刷新所有缓存
     */
    public R refreshCaches() {
        Collection<String> col = initCacheManager().getCacheNames();
        for (String cache : col) {
            if (cache.indexOf("#") == -1) {
                log.info("check cachename:" + cache);
                if (cache.toLowerCase().endsWith("cache")) {
                    log.info("check " + cache + " filter out.");
                } else {
                    log.info("action fresh cache " + cache);
                    refreshCache(cache);
                }

            }
        }
        return R.SUCCESS_OPER();
    }


    /**
     * 根据cache名称清除缓存
     *  @param cache
     */
    public R clearCache(String cache) {
        CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(cache)));
        for (int i = 0; i < c.getAllKeys().size(); i++) {
            String key = c.getAllKeys().get(i).toString();
            removeCacheKey(cache, key);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * 根据cache刷新缓存
     * @param cache
     */
    public R refreshCache(String cache) {
        if (ToolUtil.isEmpty(cache)) {
            return R.FAILURE_NO_DATA();
        }
        CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(cache)));
        for (int i = 0; i < c.getAllKeys().size(); i++) {
            // 捕捉瞬间key失效报错问题
            try {
                // 判断是否需要刷新
                String key = c.getAllKeys().get(i).toString();

                Element el = c.getKey(key);
                //el为null,强制刷新
                if (el == null) {
                    log.info("Refresh required,cache:" + cache + ",key:" + key + ",value is null,force to refresh");
                    ThreadTaskHelper.run(new Runnable() {
                        @Override
                        public void run() {
                            cacheSupportImpl.refreshCacheByKey(cache, key);
                        }
                    });
                    continue;
                }
                long hit = el.getHitCount();
                Long expired = (el.getExpirationTime() - System.currentTimeMillis()) / 1000;
                if (expired < 0) {
                    expired = expired * (-1);
                }
                CachedInvocation inv = CacheSupportImpl.cacheInvocationsMap.get(cache).get(key);
                if (inv == null || inv.getcacheableEntity() == null) {
                    log.info("CachedInvocation is null or CacheableEntity is null,cache:" + cache + ",key" + key);
                    removeCacheKey(cache, key);
                    continue;
                }
                CacheableEntity ce = inv.getcacheableEntity();
                int refreshtime = ce.getRefreshtime();
                // 主动刷新时间太低,并且命中率不高,则不去主动刷新,refreshtime second
                if (refreshtime < 120 && hit < 2) {
                    log.info("too low to refresh,too low to hit.cache:" + cache + ",key:" + key + ",refreshtime:"
                            + refreshtime + ",hit:" + hit);
                    continue;
                }
                if (refreshtime > 0 && expired > 0 && expired <= refreshtime) {
                    log.info("Refresh required,cache:" + cache + ",key:" + key + ",cacheableEntity:" + ce.toString() + ",refreshtime:" + refreshtime);
                    ThreadTaskHelper.run(new Runnable() {
                        @Override
                        public void run() {
                            cacheSupportImpl.refreshCacheByKey(cache, key);
                        }
                    });
                } else {
                    log.info("No refresh required,cache:" + cache + ",key:" + key + ",cacheableEntity:" + ce.toString() + ",refreshtime:" + refreshtime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return R.SUCCESS_OPER();
    }

    /**
     * 删除缓存
     *  @param cache
     *  @param key
     */
    public R removeCacheKey(String cache, String key) {
        if (ToolUtil.isOneEmpty(cache, key)) {
            return R.FAILURE_NO_DATA();
        }
        initCacheManager().getCache(cache).evict(key);
        return R.SUCCESS_OPER();
    }

    /**
     * 查询缓存
     *  @param cache
     *  @param key
     */
    public R queryKeyValue(String cache, String key) {
        if (ToolUtil.isOneEmpty(cache, key)) {
            return R.FAILURE_NO_DATA();
        }
        Cache.ValueWrapper v = initCacheManager().getCache(cache).get(key);
        return R.SUCCESS_OPER(v.get().toString());
    }

    /**
     *  设置缓存
     *  @param key
     *  @param ct
     *  @param timeout
     */
    public R putCacheKeyForApi(String key, String ct, long timeout) {
        if (ToolUtil.isOneEmpty(key, ct)) {
            return R.FAILURE_NO_DATA();
        }
        CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(API_CACHE)));
        Element e = new Element(key, ct);
        e.setTimeToLive((int) timeout);
        c.put(key, ct);
        return R.SUCCESS_OPER();
    }

    /**
     *  根据cache查询缓存
     *  @param cache
     */
    public R queryCustomizedEhCacheCacheManagerCacheKeys(String cache) {
        if (ToolUtil.isEmpty(cache)) {
            return R.FAILURE_NO_DATA();
        }
        JSONArray res = new JSONArray();
        CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(cache)));
        for (int i = 0; i < c.getAllKeys().size(); i++) {
            // 捕捉瞬间key失效报错问题
            try {
                Element el = c.getKey(c.getAllKeys().get(i).toString());
                if (!ToolUtil.isEmpty(el)) {
                    JSONObject e = new JSONObject();
                    e.put("cache", cache);
                    e.put("key", c.getAllKeys().get(i));
                    e.put("hit", el.getHitCount());
                    e.put("ctime",
                            DateTimeKit.format(new Date(el.getCreationTime()), DateTimeKit.NORM_DATETIME_PATTERN));
                    e.put("accesstime",
                            DateTimeKit.format(new Date(el.getLastAccessTime()), DateTimeKit.NORM_DATETIME_PATTERN));
                    e.put("expiretime",
                            DateTimeKit.format(new Date(el.getExpirationTime()), DateTimeKit.NORM_DATETIME_PATTERN));
                    e.put("ttl", el.getTimeToLive());
                    e.put("tti", el.getTimeToIdle());
                    res.add(e);
                }
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        return R.SUCCESS_OPER(res);
    }

    /**
     *  查询缓存
     */
    public R queryCacheCacheManagerCaches() {
        JSONArray res = new JSONArray();
        Collection<String> col = initCacheManager().getCacheNames();
        for (String cache : col) {
            if (cache.indexOf("#") == -1) {
                JSONObject e = new JSONObject();
                e.put("id", cache);
                e.put("name", cache);
                res.add(e);
            }
        }
        return R.SUCCESS_OPER(res);
    }

}
