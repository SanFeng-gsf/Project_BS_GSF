package com.syztb_idea_gsf.utils;

public class RedisConstants {
    /**
     * 同一定义常量
     */

    public static final String LOGIN_NAME_PASSWORD_KEY = "login:name:password:";
    public static final Long LOGIN_NAME_PASSWORD_TTL = 60L;
    public static final String LOGIN_PASSWORD_KEY = "login:password:";
    public static final Long LOGIN_PASSWORD_TTL = 60L;
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 60L;
    public static final Long CACHE_NULL_TTL = 2L;
    public static final String CACHE_ALL_ZB_KEY = "cache:all:zhaoB:";
    public static final Long CACHE_ALL_ZB_TTL = 30L;
    public static final Long CACHE_ZB_TTL = 30L;
    public static final String CACHE_ZB_KEY = "cache:zhaoB:";

    public static final String LOCK_ALL_ZB_KEY = "lock:all:zhaoB:";
    public static final String LOCK_ZB_KEY = "lock:zhaoB:";
    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";
}
