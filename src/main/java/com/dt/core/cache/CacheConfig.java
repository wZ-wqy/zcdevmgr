package com.dt.core.cache;

/**
 * @author: lank
 * @date: 2020年3月28日 下午7:34:24
 * @Description:
 */
public class CacheConfig {

    // 刷新数据建议设置长些,短的话主动刷新轮不上
    //#cache#expire#flushtime,例如public#45#10,45s过期，在最后10s内，如果有访问则强制刷新
    public static final String CACHE_PUBLIC = "public";

    public static final String CACHE_PUBLIC_45_10 = "public#45#10";

    public static final String CACHE_PUBLIC_80_10 = "public#80#10";
    // 非常频繁
    public static final String CACHE_PUBLIC_5_2 = "public#5#2";

    public static final String CACHE_PUBLIC_1d_1h = "public#86400#3600";
    // 非常频繁
    public static final String CACHE_PUBLIC_2h_20min = "public#7200#1200";
    // 不频繁
    public static final String CACHE_PUBLIC_3h_1h = "public#10800#3600";
    // 报表
    public static final String CACHE_PUBLIC_6h_30min = "public#21600#1800";

    public static final String CACHE_PUBLIC_300_150 = "public#300#150";

    public static final String CACHE_PUBLIC_600_120 = "public#300#120";

    // 用户数据
    public static final String CACHE_USER = "user";

    public static final String CACHE_USER_180_60 = "user#180#60";

    public static final String CACHE_USER_480_300 = "user#480#300";

    public static final String CACHE_USER_1200_600 = "user#1200#600";

    public static final String CACHE_WX_CONF = "wxconf";

    public static final String CACHE_WX_CONF_300_180 = "wxconf#300#180";


}
