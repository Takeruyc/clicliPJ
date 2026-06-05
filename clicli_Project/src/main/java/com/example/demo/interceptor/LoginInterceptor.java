package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession();

        // Session 取用户
        Object loginUser =
                session.getAttribute("loginUser");

        // 未登录
        if (loginUser == null) {

            response.sendRedirect("/login");

            return false;
        }

        // 已登录
        return true;
    }
}