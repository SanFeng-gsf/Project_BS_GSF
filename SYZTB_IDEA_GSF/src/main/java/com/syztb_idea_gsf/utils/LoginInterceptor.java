package com.syztb_idea_gsf.utils;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断需不需要拦截 (ThreadLocal是否有用户)
        if(UserHolder.getUser()==null){
            response.setStatus(401);
            return false;
        }
        return true;
    }


}
