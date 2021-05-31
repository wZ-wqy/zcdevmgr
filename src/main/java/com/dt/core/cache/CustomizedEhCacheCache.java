package com.dt.core.cache;

import com.dt.core.tool.lang.SpringContextUtil;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.Callable;

public class CustomizedEhCacheCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedEhCacheCache.class);

    private final Ehcache cache;

    // 格式cacahename#5#2
    // #expiredtime 0注解层面上永未不过期(具体还要看其他配置),当有值是,优先级最高
    // #refreshtime 0离快过期时刷新数据
    // expiredtime = -1 可能来自主动刷新或者默认情况需要设置
    // expiredtime=  -2  则设置cache不需要设置,没有对cache设置cache#40#20
    private int expiredtime = -1;
    private int refreshtime = -1;

    public CustomizedEhCacheCache(Ehcache ehcache) {
        Assert.notNull(ehcache, "Ehcache must not be null");
        Status status = ehcache.getStatus();
        Assert.isTrue(Status.STATUS_ALIVE.equals(status),
                "An 'alive' Ehcache is required - current cache is " + status.toString());
        cache = ehcache;
    }

    public CustomizedEhCacheCache(Ehcache ehcache, int et, int rt) {
        expiredtime = et;
        refreshtime = rt;
        Assert.notNull(ehcache, "Ehcache must not be null");
        Status status = ehcache.getStatus();
        Assert.isTrue(Status.STATUS_ALIVE.equals(status),
                "An 'alive' Ehcache is required - current cache is " + status.toString());
        cache = ehcache;
    }

    private CacheSupport getCacheSupport() {
        return SpringContextUtil.getBean(CacheSupport.class);
    }

    @Override
    public final String getName() {
        return cache.getName();
    }

    @Override
    public final Ehcache getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Element element = cache.get(key);
        return toValueWrapper(element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Element element = cache.get(key);
        Object value = (element != null ? element.getObjectValue() : null);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        } else {
            return (T) value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        logger.info("put key:" + key + ",expiredtime:" + expiredtime);
        Element e = new Element(key, value);
        if (expiredtime > 0) {
            // 注解中有设置
            e.setTimeToLive(expiredtime);
        } else if (expiredtime == -2) {
            // 注解中没有设置,引用原来cache的
        } else if (expiredtime == -1) {
            // expiredtime=-1,正常情况下可能来自主动刷新需要获取ttl,主动刷新并未获取ttl
            Element ce = cache.get(key);
            if (ce != null) {
                // 如果没有找到cache
                e.setTimeToLive(ce.getTimeToLive());
            } else {
                logger.info("Can't cache it,key not exists. cache:" + cache.getName() + ",key:" + key + ",expiredtime:"
                        + expiredtime, ",will put obj again");
            }
        } else {
            logger.info("未知");
        }
        cache.put(e);
        cache.flush();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Element existingElement = cache.putIfAbsent(new Element(key, value));
        return toValueWrapper(existingElement);
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
        ThreadTaskHelper.run(new Runnable() {
            @Override
            public void run() {
                removeCacheByKey(key);
            }
        });
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    private ValueWrapper toValueWrapper(Element element) {
        if (element == null) {
            return null;
        }
        Long expired = (element.getExpirationTime() - System.currentTimeMillis()) / 1000;
        logger.info("@From mem " + cache.getName() + ":" + element.getObjectKey() + ",ExpirationTime:" + expired + ",RefreshTime:" + refreshtime);
        // 判断是否要刷新
        if (refreshtime > 0 && expired > 0 && expired <= refreshtime) {
            ThreadTaskHelper.run(new Runnable() {
                @Override
                public void run() {
                    // 重新加载数据
                    logger.info("refresh " + cache.getName() + ",key:" + element.getObjectKey());
                    CustomizedEhCacheCache.this.getCacheSupport().refreshCacheByKey(cache.getName(),
                            element.getObjectKey().toString());
                }
            });
        }
        cache.flush();
        return new SimpleValueWrapper(element.getObjectValue());
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    public Element getKey(String key) {
        return cache.get(key);
    }

    public List<?> getAllKeys() {
        return cache.getKeys();
    }

    private void removeCacheByKey(Object key) {
        //同步删除运行时环境
        if (cache.isKeyInCache(key)) {
            cache.remove(key);
        }
        CustomizedEhCacheCache.this.getCacheSupport().removeCacheByKey(cache.getName(), key.toString());
    }

}
