package com.jeewaloka.digital.jeewalokadigital.configaration;

import com.jeewaloka.digital.jeewalokadigital.Interceptor.AuthChannelInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   //STOMP (Simple Text Oriented Messaging Protocol)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry
                .addEndpoint("/ws")    //to establish the connection via this path
                .setAllowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://frontend:80") //allows cross origin request from which domains
                .withSockJS();              //enable SockJS fallback option for browser that don't support web socket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/topic","/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {           //this is jwt interceptor for web socket handshake
//        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(authChannelInterceptorAdapter);
    }
}
