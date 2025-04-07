//package com.jeewaloka.digital.jeewalokadigital.controller;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Controller
//public class UserPresenceController{
//    private static final Set<String> onlineUsers = new HashSet<>();
//
//    @MessageMapping("/user-online")
//    @SendTo("/topic/onlineUsers")
//    public Set<String> userOnline(String userId) {
//        onlineUsers.add(userId);
//        return onlineUsers;
//    }
//
//    @MessageMapping("/user-offline")
//    @SendTo("/topic/onlineUsers")
//    public Set<String> userOffline(String userId) {
//        onlineUsers.remove(userId);
//        return onlineUsers;
//    }
//}
