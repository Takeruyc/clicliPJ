package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        String requestUri = request.getRequestURI();
        
        if (requestUri.equals("/login") || requestUri.equals("/register") ||
            requestUri.equals("/admin/login") || requestUri.startsWith("/css/") ||
            requestUri.startsWith("/uploads/") || requestUri.equals("/logout")) {
            return true;
        }
        
        boolean adminRequest = requestUri.startsWith("/admin/");

        if (loginUser == null) {
            response.sendRedirect(adminRequest ? "/admin/login" : "/login");
            return false;
        }

        if (adminRequest) {
            User user = (User) loginUser;
            if (!"admin".equals(user.getRole())) {
                response.sendRedirect("/");
                return false;
            }
        }

        return true;
    }
}