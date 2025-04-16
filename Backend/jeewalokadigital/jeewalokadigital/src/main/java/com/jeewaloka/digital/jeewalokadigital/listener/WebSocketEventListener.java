package com.jeewaloka.digital.jeewalokadigital.listener;

import com.jeewaloka.digital.jeewalokadigital.service.Socket.UserPresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate; // To send messages

    @Autowired
    private UserPresenceService userPresenceService; // To update Redis

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal userPrincipal = headerAccessor.getUser(); // Get user from interceptor

        if (userPrincipal != null && userPrincipal.getName() != null) {
            String username = userPrincipal.getName(); // Assuming name is the user ID
            logger.info("User Connected: {}", username);

            userPresenceService.userConnected(username);

            // Broadcast that the user is online
            Map<String, String> messagePayload = new HashMap<>();
            messagePayload.put("username", username);
            messagePayload.put("status", "online");

            // Send to a general topic where all clients listen for presence changes
            messagingTemplate.convertAndSend("/topic/presence", messagePayload);

        } else {
            logger.warn("Connect event received without authenticated user.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal userPrincipal = headerAccessor.getUser(); // Get user associated with the session

        if (userPrincipal != null && userPrincipal.getName() != null) {
            String username = userPrincipal.getName();
            logger.info("User Disconnected: {}", username);

            userPresenceService.userDisconnected(username);

            // Broadcast that the user is offline
            Map<String, String> messagePayload = new HashMap<>();
            messagePayload.put("username", username);
            messagePayload.put("status", "offline");

            messagingTemplate.convertAndSend("/topic/presence", messagePayload);
        } else {
            // This might happen if connection failed before authentication completed
            // Or if session cleanup logic runs after security context is cleared.
            // Optionally log session ID for debugging: event.getSessionId()
            logger.warn("Disconnect event received for session {} without authenticated user.", event.getSessionId());
        }
    }
}
