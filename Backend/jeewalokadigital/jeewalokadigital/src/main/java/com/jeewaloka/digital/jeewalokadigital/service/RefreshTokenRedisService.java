package com.jeewaloka.digital.jeewalokadigital.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenRedisService {
    private final RedisTemplate<String, Object> redisTokenTemplate;
    private final HashOperations<String, String, String> hashOperations;

    private static final long REFRESH_TOKEN_EXPIRY = 1000*60*60*24*7;

    public RefreshTokenRedisService(@Qualifier("redisTokenTemplate") RedisTemplate<String, Object> redisTokenTemplate){
        this.redisTokenTemplate = redisTokenTemplate;
        this.hashOperations = redisTokenTemplate.opsForHash();
    }

    public void storeRefreshToken(String username, String refreshToken, String ip, String userAgent){
        String key = "refreshToken" + username;
        hashOperations.put(key, refreshToken,ip+ " " + userAgent);
        redisTokenTemplate.expire(key, REFRESH_TOKEN_EXPIRY, TimeUnit.MILLISECONDS);
    }

//    public boolean checkTokenExists(String username, String refreshToken){
//        return !hashOperations.get("refreshToken" + username, refreshToken).isEmpty()? true : false;
//    }

//    public boolean checkTokenExists(String username, String refreshToken){
//        System.out.println("entered check function");
//        boolean isExist = hashOperations.hasKey("refreshToken" + username, refreshToken);
//        System.out.println(isExist);
//        return isExist;
//    }

    public Map<String, String> getActiveSessions(String username){
        System.out.println("entered to check existence");
        return hashOperations.entries("refreshToken" + username);
    }

    public boolean validateRefreshToken(String username, String refreshToken, String ip, String userAgent){
        String storeValue = hashOperations.get("refreshToken" + username, refreshToken);
        if(storeValue == null){
            return false;
        }
        String[] storedData = storeValue.split(" ");
        String storedIP = storedData[0];
        String storedUserAgent = storedData[1];
        return storedIP.equals(ip) && storedUserAgent.equals(userAgent);
    }

    public void revokeAllSessions(String username){
        redisTokenTemplate.delete("refreshToken" + username);
    }

    public boolean revokeRefreshToken(String username, String refreshToken){
        Long removed = hashOperations.delete("refreshToken" + username,refreshToken);
        return removed != null && removed > 0;
    }

}
