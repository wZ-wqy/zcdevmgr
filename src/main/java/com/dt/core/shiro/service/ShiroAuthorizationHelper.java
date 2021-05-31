package com.dt.core.shiro.service;

import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.LoginService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lank
 * @time 2014年1月8日
 */
public class ShiroAuthorizationHelper {

    public static String authorizationCache = "authorizationCache";
    public static String passwordRetryCache = "passwordRetryCache";
    public static String authenticationCache = "authenticationCache";

    private static EhCacheManager cacheManager;
    private static Logger log = LoggerFactory.getLogger(ShiroAuthorizationHelper.class);

    /* 权限信息 */
    public static void clearAuthorizationInfo(PrincipalCollection principals) {
        Cache<Object, Object> cache = cacheManager.getCache(authorizationCache);
        if (principals != null) {
            if (principals.getPrimaryPrincipal() != null) {
                log.info("Clear clearAuthorizationInfo,Id:" + principals.getPrimaryPrincipal());
                cache.remove(principals.getPrimaryPrincipal());
            }
        }
    }

    /* 认证信息 */
    public static void clearAuthenticationInfo(String id) {
        if (ToolUtil.isNotEmpty(id)) {
            log.info("Clear clearAuthenticationInfo,Id:" + id);
            Cache<Object, Object> cache = cacheManager.getCache(authenticationCache);
            cache.remove(id);
        }
    }

    public static void showCache() {

        Cache<Object, Object> cache = cacheManager.getCache(authorizationCache);
        log.info("##########" + authorizationCache);
        if (cache != null) {
            log.info("size:" + cache.size());
            for (Object key : cache.keys()) {
                log.info("key:" + key + ",val:" + cache.get(key));
            }
        }

        Cache<Object, Object> cache2 = cacheManager.getCache(passwordRetryCache);
        log.info("##########" + passwordRetryCache);
        if (cache2 != null) {
            log.info("size:" + cache2.size());
            for (Object key : cache2.keys()) {
                log.info("key:" + key + ",val:" + cache2.get(key));
            }
        }
        Cache<Object, Object> cache3 = cacheManager.getCache(authenticationCache);
        log.info("##########" + authenticationCache);
        if (cache3 != null) {
            log.info("size:" + cache3.size());
            for (Object key : cache3.keys()) {
                log.info("key:" + key + ",val:" + cache3.get(key));
            }
        }
    }

    public static void clearSession(String id) {
        if (ToolUtil.isNotEmpty(id)) {
            log.info("Clear clearSession,Id:" + id);
            LoginService.me().logout(id);
        }
    }

    public static EhCacheManager getCacheManager() {
        return cacheManager;
    }

    public static void setCacheManager(EhCacheManager cacheManager) {
        ShiroAuthorizationHelper.cacheManager = cacheManager;
    }

}