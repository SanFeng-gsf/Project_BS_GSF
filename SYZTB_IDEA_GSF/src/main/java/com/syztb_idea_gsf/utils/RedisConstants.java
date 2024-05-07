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
    public static final String LOGIN_SYS_USER_KEY = "login:sysToken:";
    public static final Long LOGIN_USER_TTL = 60L;
    public static final Long CACHE_NULL_TTL = 2L;
    public static final String CACHE_USER_KEY = "cache:user:";
    public static final Long CACHE_USER_TTL = 30L;
    public static final String CACHE_ALL_ZB_KEY = "cache:all:zhaoB:";
    public static final String CACHE_ALL_ZB_UN_KEY = "cache:all:zhaoBUn:";
    public static final Long CACHE_SELECT_ZB_TTL = 30L;
    public static final String CACHE_WEI_GUI_ZB_KEY = "cache:weiGui:zhaoB:";
    public static final String CACHE_SUCCESS_ZB_KEY = "cache:success:zhaoB:";
    public static final String CACHE_UN_SUCCESS_ZB_KEY = "cache:un_success:zhaoB:";
    public static final String CACHE_ZB_KEY = "cache:zhaoB:";
    public static final String CACHE_TB_KEY = "cache:touB:";
    public static final Long CACHE_TB_TTL = 30L;
    public static final String LOCK_ALL_ZB_KEY = "lock:all:zhaoB:";
    public static final String LOCK_ALL_ZB_UN_KEY = "lock:all:zhaoBUn:";
    public static final String LOCK_ZB_KEY = "lock:zhaoB:";
    public static final String LOCK_TB_KEY = "lock:zhaoB:";

}
