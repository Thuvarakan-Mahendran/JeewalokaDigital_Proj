//package com.jeewaloka.digital.jeewalokadigital.controller;
//
//import com.jeewaloka.digital.jeewalokadigital.service.Socket.UserPresenceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api/presence")
//public class PresenceController {
//    @Autowired
//    private UserPresenceService presenceService;
//
//    @GetMapping("/online")
//    public ResponseEntity<Set<Object>> getOnlineUsers() {
//        Set<Object> onlineUserNames = presenceService.getOnlineUserNames();
//        return ResponseEntity.ok(onlineUserNames);
//    }
//}

package com.jeewaloka.digital.jeewalokadigital.controller; // Your existing package

// ***MODIFIED IMPORT***
import com.jeewaloka.digital.jeewalokadigital.service.Socket.UserPresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/presence")
public class PresenceController {

    private static final Logger logger = LoggerFactory.getLogger(PresenceController.class);

    @Autowired
    private UserPresenceService userPresenceService; // Use the new service

    @GetMapping("/online")
    public ResponseEntity<Set<String>> getOnlineUsers() {
        Set<Object> onlineUserObjects = userPresenceService.getOnlineUserNames();

        if (onlineUserObjects == null) {
            return ResponseEntity.ok(Collections.emptySet());
        }

        // Convert Set<Object> to Set<String>, assuming objects are stored as Strings representing usernames
        Set<String> onlineUsernames = onlineUserObjects.stream()
                .filter(obj -> obj instanceof String) // Ensure object is a String
                .map(obj -> (String) obj)             // Cast to String
                .collect(Collectors.toSet());

        // Optional: Log if any non-string objects were found
        if (onlineUsernames.size() != onlineUserObjects.size()) {
            logger.warn("getOnlineUsers: Found non-string objects in the online users set from Redis. Only string usernames were returned.");
        }

        return ResponseEntity.ok(onlineUsernames);
    }
}