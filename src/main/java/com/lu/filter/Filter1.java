package com.lu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class Filter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Access-Control-Allow-Credentials跨域问题
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");


        // 点击劫持：X-Frame-Options未配置
        response.addHeader("X-Frame-Options","SAMEORIGIN");
        // 检测到目标Referrer-Policy响应头缺失
        response.addHeader("Referer-Policy","origin");
        // Content-Security-Policy响应头确实
        response.addHeader("Content-Security-Policy","object-src 'self'");
        // 检测到目标X-Permitted-Cross-Domain-Policies响应头缺失
        response.addHeader("X-Permitted-Cross-Domain-Policies","master-only");
        // 检测到目标X-Content-Type-Options响应头缺失
        response.addHeader("X-Content-Type-Options","nosniff");
        // 检测到目标X-XSS-Protection响应头缺失
        response.addHeader("X-XSS-Protection","1; mode=block");
        // 检测到目标X-Download-Options响应头缺失
        response.addHeader("X-Download-Options","noopen");
        // 点击劫持：X-Frame-Options未配置
        response.addHeader("X-Frame-Options","SAMEORIGIN");
        // HTTP Strict-Transport-Security缺失
        response.addHeader("Strict-Transport-Security","max-age=63072000; includeSubdomains; preload");
        filterChain.doFilter(servletRequest,servletResponse);
        }


    @Override
    public void destroy() {

    }
}
