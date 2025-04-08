//package com.jeewaloka.digital.jeewalokadigital.Scheduler;
//
//import com.jeewaloka.digital.jeewalokadigital.entity.HeartbeatMessage;
//import com.jeewaloka.digital.jeewalokadigital.service.Socket.UserPresenceService;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableScheduling
//public class PresenceScheduler {
//
//    // Define the timeout threshold (e.g., 10 seconds)
//    private static final long TIMEOUT_MILLIS = 10000;
//
//    @Autowired
//    private UserPresenceService userPresenceService;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    // Scheduled task to run every 5 seconds to check for inactive users
//    @Scheduled(fixedRate = 5000)
//    public void checkUserPresence() {
//        long currentTime = System.currentTimeMillis();
//        for (Map.Entry<String, Long> entry : userPresenceService.getUserHeartbeatMap().entrySet()) {
//            String username = entry.getKey();
//            long lastHeartbeat = entry.getValue();
//            if ((currentTime - lastHeartbeat) > TIMEOUT_MILLIS) {
//                // Remove the user if their heartbeat is too old
//                userPresenceService.removeUser(username);
//                // Broadcast the offline status on /topic/presence
//                HeartbeatMessage offlineMsg = new HeartbeatMessage(username, currentTime);
//                messagingTemplate.convertAndSend("/topic/presence", offlineMsg);
//            }
//        }
//    }
//}
//
