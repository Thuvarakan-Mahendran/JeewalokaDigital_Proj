//package com.jeewaloka.digital.jeewalokadigital.Interceptor;
//
//import com.jeewaloka.digital.jeewalokadigital.service.Security.RateLimiterService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class RateLimiterInterceptor implements HandlerInterceptor {
//    @Autowired
//    private RateLimiterService rateLimiterService;
//
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String userIP = request.getRemoteAddr();
//        if(!rateLimiterService.isAllowed(userIP)){
//            response.setStatus(429);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"error\": \"Too Many Requests\", \"status\": 429}");
//            return  false;
//        }
//        return true;
//    }
//}
