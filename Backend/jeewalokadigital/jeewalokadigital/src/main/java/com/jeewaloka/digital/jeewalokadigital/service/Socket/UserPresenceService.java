package com.jeewaloka.digital.jeewalokadigital.service.Socket;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserPresenceService {

    // In-memory map to track the last heartbeat timestamp for each user.
//    private final ConcurrentMap<String, Long> userHeartbeatMap = new ConcurrentHashMap<>();
//
//
//
//    // Update the heartbeat timestamp for a given user.
//    public void updateHeartbeat(String username, long timestamp) {
//        userHeartbeatMap.put(username, timestamp);
//    }
//
//    // Check if a user is online given a timeout threshold.
//    public boolean isUserOnline(String username, long timeoutMillis) {
//        Long lastHeartbeat = userHeartbeatMap.get(username);
//        if (lastHeartbeat == null) {
//            return false;
//        }
//        return (System.currentTimeMillis() - lastHeartbeat) <= timeoutMillis;
//    }
//
//    // Remove a user from the map (marking them as offline).
//    public void removeUser(String username) {
//        userHeartbeatMap.remove(username);
//    }
//
//    // Return the complete map of user heartbeats.
//    public ConcurrentMap<String, Long> getUserHeartbeatMap() {
//        return userHeartbeatMap;
//    }

    private static final String ONLINE_USERS_KEY = "online_users";

    private final RedisTemplate<String, Object> redisTemplate;
    public UserPresenceService(@Qualifier("redisTokenTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void userConnected(String userId) {
        redisTemplate.opsForSet().add(ONLINE_USERS_KEY, userId);
    }

    public void userDisconnected(String userId) {
        redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, userId);
    }

    public boolean isUserOnline(String userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ONLINE_USERS_KEY, userId));
    }

    public Set<Object> getOnlineUserNames() {
        return redisTemplate.opsForSet().members(ONLINE_USERS_KEY);
    }
}
