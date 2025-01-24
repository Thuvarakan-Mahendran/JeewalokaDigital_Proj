//package com.jeewaloka.digital.jeewalokadigital.authentication;
//
//import com.jeewaloka.digital.jeewalokadigital.entity.User;
//import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    private UserRepository userRepository;
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String uname = authentication.getName();
//        User user = userRepository.findByUname(uname);
//        if(user != null){
//            user.setLastLogin(LocalDateTime.now());
//            userRepository.save(user);
//        }
////        response.sendRedirect("/api"); //it will redirect to default page
//    }
//}
