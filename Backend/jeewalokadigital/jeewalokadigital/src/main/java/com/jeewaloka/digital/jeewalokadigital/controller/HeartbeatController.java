//package com.jeewaloka.digital.jeewalokadigital.controller;
//import com.jeewaloka.digital.jeewalokadigital.entity.HeartbeatMessage;
//import com.jeewaloka.digital.jeewalokadigital.service.Socket.UserPresenceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class HeartbeatController {
//
//    @Autowired
//    private UserPresenceService userPresenceService;
//
//    // Handle heartbeat messages from clients
//    @MessageMapping("/heartbeat")
//    @SendTo("/topic/heartbeat")
//    public HeartbeatMessage handleHeartbeat(HeartbeatMessage heartbeatMessage) {
//        // Update the user's heartbeat timestamp
//        userPresenceService.updateHeartbeat(heartbeatMessage.getUsername(), heartbeatMessage.getTimestamp());
//        // Optionally return the heartbeat message as an acknowledgment
//        return heartbeatMessage;
//    }
//}
//
