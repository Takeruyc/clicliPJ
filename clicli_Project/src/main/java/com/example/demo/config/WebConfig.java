package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.LoginInterceptor;

@Configuration
public class WebConfig
        implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

//拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/video/**", "/admin/**")
                .excludePathPatterns("/", "/login", "/register", "/admin/login",
                                     "/css/**", "/uploads/**", "/logout");
    }
             
//             放行静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}