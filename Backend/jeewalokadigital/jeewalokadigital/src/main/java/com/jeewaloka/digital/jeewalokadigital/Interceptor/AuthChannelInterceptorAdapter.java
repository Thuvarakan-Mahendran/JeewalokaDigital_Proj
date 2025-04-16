package com.jeewaloka.digital.jeewalokadigital.Interceptor;

import com.jeewaloka.digital.jeewalokadigital.service.Security.JwtService;
import com.jeewaloka.digital.jeewalokadigital.service.Security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthChannelInterceptorAdapter.class);
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        return ChannelInterceptor.super.preSend(message, channel);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())){
            String authToken = accessor.getFirstNativeHeader("Authorization");
            if(authToken != null && authToken.startsWith("Bearer ")){
                String accessToken = authToken.substring(7);
                String username = jwtService.extractUsername(accessToken);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                if(username != null && jwtService.isTokenValid(accessToken,userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    accessor.setUser(authentication);
//                    logger.info("User {} authenticated for web socket session", authentication.getName());
                } else {
                    throw new IllegalArgumentException("invalid jwt token");
                }
            } else {
                throw new IllegalArgumentException("missing or invalid authorization header for web socket connection");
            }
        }
        return message;
    }
}
