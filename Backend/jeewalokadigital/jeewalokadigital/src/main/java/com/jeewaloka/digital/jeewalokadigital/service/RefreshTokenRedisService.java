//package com.jeewaloka.digital.jeewalokadigital.service;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class RefreshTokenRedisService {
//    private final RedisTemplate<String, Object> redisTokenTemplate;
//    private final HashOperations<String, String, String> hashOperations;
//
//    private static final long REFRESH_TOKEN_EXPIRY = 1000*60*60*24*7;
//
//    public RefreshTokenRedisService(@Qualifier("redisTokenTemplate") RedisTemplate<String, Object> redisTokenTemplate){
//        this.redisTokenTemplate = redisTokenTemplate;
//        this.hashOperations = redisTokenTemplate.opsForHash();
//    }
//
////    public void storeRefreshToken(String username, String refreshToken, String ip, String userAgent){
////        String key = "refreshToken" + username;
////        hashOperations.put(key, refreshToken,ip+ " " + userAgent);
////        redisTokenTemplate.expire(key, REFRESH_TOKEN_EXPIRY, TimeUnit.MILLISECONDS);
////    }
//
//    public void storeRefreshToken(String username, String refreshToken, String ip, String userAgent){
//        String key = "refreshToken" + username;
//        hashOperations.put(key, refreshToken, refreshToken);
//        hashOperations.put(key, refreshToken+" Agent_details",ip+ " " + userAgent);
//        redisTokenTemplate.expire(key, REFRESH_TOKEN_EXPIRY, TimeUnit.MILLISECONDS);
//    }
//
////    public void storeRefreshToken(String username, String refreshToken){
////        String key = "refreshToken" + username;
////        hashOperations.put(key, refreshToken, refreshToken);
////        redisTokenTemplate.expire(key, REFRESH_TOKEN_EXPIRY, TimeUnit.MILLISECONDS);
////    }
//
////    public boolean checkTokenExists(String username, String refreshToken){
////        return !hashOperations.get("refreshToken" + username, refreshToken).isEmpty()? true : false;
////    }
//
////    public boolean checkTokenExists(String username, String refreshToken){
////        System.out.println("entered check function");
////        boolean isExist = hashOperations.hasKey("refreshToken" + username, refreshToken);
////        System.out.println(isExist);
////        return isExist;
////    }
//
//    public Map<String, String> getActiveSessions(String username){
//        System.out.println("entered to check existence");
//        return hashOperations.entries("refreshToken" + username);
//    }
//
////    public boolean validateRefreshToken(String username, String refreshToken, String ip, String userAgent){
////        String storeValue = hashOperations.get("refreshToken" + username, refreshToken);
////        if(storeValue == null){
////            return false;
////        }
////        String[] storedData = storeValue.split(" ");
////        String storedIP = storedData[0];
////        String storedUserAgent = storedData[1];
////        return storedIP.equals(ip) && storedUserAgent.equals(userAgent);
////    }
//
//    public boolean validateRefreshToken(String username, String refreshToken){
//        String storeValue = hashOperations.get("refreshToken" + username, refreshToken);
//        if(storeValue == null){
//            return false;
//        }
//        return storeValue.equals(refreshToken);
//    }
//
//    public void revokeAllSessions(String username){
//        redisTokenTemplate.delete("refreshToken" + username);
//    }
//
////    public boolean revokeRefreshToken(String username, String refreshToken){
////        Long removed = hashOperations.delete("refreshToken" + username,refreshToken);
////        return removed != null && removed > 0;
////    }
//
//    public boolean revokeRefreshToken(String username, String refreshToken){
//        Long removedRefreshToken = hashOperations.delete("refreshToken" + username, refreshToken);
//        Long removedAgent = hashOperations.delete("refreshToken" + username, refreshToken+" Agent_details");
//        return removedRefreshToken != null && removedRefreshToken > 0 && removedAgent != null && removedAgent > 0;
//    }
//
//}



package com.jeewaloka.digital.jeewalokadigital.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenRedisService {
    private final RedisTemplate<String, Object> redisTokenTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7 days

    public RefreshTokenRedisService(@Qualifier("redisTokenTemplate") RedisTemplate<String, Object> redisTokenTemplate) {
        this.redisTokenTemplate = redisTokenTemplate;
        this.hashOperations = redisTokenTemplate.opsForHash();
    }

    // Store refresh token details in a unique hash and add the token to the user's set.
    public void storeRefreshToken(String username, String refreshToken, String ip, String userAgent) {
        // Unique key for the refresh token details.
        String tokenKey = "refreshToken:" + refreshToken;

        // Prepare token details.
        Map<String, Object> tokenDetails = new HashMap<>();
        tokenDetails.put("username", username);
        tokenDetails.put("ip", ip);
        tokenDetails.put("userAgent", userAgent);
        tokenDetails.put("createdAt", String.valueOf(System.currentTimeMillis()));

        // Store the token details in a Redis hash.
        redisTokenTemplate.opsForHash().putAll(tokenKey, tokenDetails);
        redisTokenTemplate.expire(tokenKey, REFRESH_TOKEN_EXPIRY, TimeUnit.MILLISECONDS);

        // Add the token to a set tracking all tokens for this user.
        String userTokensKey = "user:" + username + ":refreshTokens";
        redisTokenTemplate.opsForSet().add(userTokensKey, refreshToken);
    }

    // Retrieve the details for a specific refresh token.
    public Map<Object, Object> getRefreshTokenDetails(String refreshToken) {
        String tokenKey = "refreshToken:" + refreshToken;
        return redisTokenTemplate.opsForHash().entries(tokenKey);
    }

    // Retrieve all active sessions (tokens and their details) for a user.
//    public Map<String, Map<Object, Object>> getActiveSessions(String username) {
//        String userTokensKey = "user:" + username + ":refreshTokens";
//        Set<Object> tokens = redisTokenTemplate.opsForSet().members(userTokensKey);
//        Map<String, Map<Object, Object>> sessions = new HashMap<>();
//        if (tokens != null) {
//            for (Object tokenObj : tokens) {
//                String token = tokenObj.toString();
//                Map<Object, Object> details = getRefreshTokenDetails(token);
//                sessions.put(token, details);
//            }
//        }
//        return sessions;
//    }

    public List<Map<Object, Object>> getActiveSessions(String username) {
        String userTokensKey = "user:" + username + ":refreshTokens";
        Set<Object> tokens = redisTokenTemplate.opsForSet().members(userTokensKey);
        List<Map<Object, Object>> sessions = new ArrayList<>();
        if (tokens != null) {
            for (Object tokenObj : tokens) {
                String token = tokenObj.toString();
                Map<Object, Object> details = getRefreshTokenDetails(token);
                sessions.add(details);
            }
        }
        return sessions;
    }


    // Validate a refresh token by checking its existence and comparing stored IP and user agent.
//    public boolean validateRefreshToken(String username, String refreshToken, String ip, String userAgent) {
//        String tokenKey = "refreshToken:" + refreshToken;
//        Map<Object, Object> details = redisTokenTemplate.opsForHash().entries(tokenKey);
//        if (details == null || details.isEmpty()) {
//            return false;
//        }
//        // Validate the username.
//        String storedUsername = (String) details.get("username");
//        System.out.println("stored username is " + storedUsername);
//        if (!username.equals(storedUsername)) {
//            return false;
//        }
//        // Validate the IP address and user agent.
//        String storedIp = details.get("ip");
//        String storedUserAgent = details.get("userAgent");
//        return ip.equals(storedIp) && userAgent.equals(storedUserAgent);
//    }

    // Overloaded method for validation without IP/user-agent checks.
    public boolean validateRefreshToken(String username, String refreshToken) {
        String tokenKey = "refreshToken:" + refreshToken;
        Map<Object, Object> details = redisTokenTemplate.opsForHash().entries(tokenKey);
        if (details.isEmpty()) {
            return false;
        }
        String storedUsername = (String) details.get("username");
        System.out.println("stored username is " + storedUsername);
        return username.equals(storedUsername);
    }

    // Revoke a specific refresh token: delete its hash and remove it from the user's token set.
    public boolean revokeRefreshToken(String username, String refreshToken) {
        String tokenKey = "refreshToken:" + refreshToken;
        Boolean deleted = redisTokenTemplate.delete(tokenKey);
        String userTokensKey = "user:" + username + ":refreshTokens";
        Long removedCount = redisTokenTemplate.opsForSet().remove(userTokensKey, refreshToken);
        return Boolean.TRUE.equals(deleted) && removedCount != null && removedCount > 0;
    }

    // Revoke all refresh tokens for a given user.
    public void revokeAllSessions(String username) {
        String userTokensKey = "user:" + username + ":refreshTokens";
        Set<Object> tokens = redisTokenTemplate.opsForSet().members(userTokensKey);
        if (tokens != null) {
            for (Object tokenObj : tokens) {
                String token = tokenObj.toString();
                String tokenKey = "refreshToken:" + token;
                redisTokenTemplate.delete(tokenKey);
            }
        }
        // Delete the user's token set.
        redisTokenTemplate.delete(userTokensKey);
    }
}
