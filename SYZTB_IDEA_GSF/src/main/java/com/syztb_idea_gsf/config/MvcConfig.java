package com.syztb_idea_gsf.config;

import com.syztb_idea_gsf.utils.LoginInterceptor;
import com.syztb_idea_gsf.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * 拦截器配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(   // 排除的拦截器路径(可以直接浏览的写进去)
                        "/sys/**",
                        "/zbUn/**",
                        "/userIcon/**",
                        "/jb/**",
                        "/zb/**",
                        "/tb/**",
                        "/user/**",
                        "/shop/**",
                        "/shop-type/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login"
                ).order(1);
        // 第一个拦截器，用于刷新有效期
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(0);
    }
}
