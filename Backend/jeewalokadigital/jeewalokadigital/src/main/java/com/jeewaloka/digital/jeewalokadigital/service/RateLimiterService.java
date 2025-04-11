package com.jeewaloka.digital.jeewalokadigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    private final RedisTemplate<String, Object> redisTokenTemplate;
    private static final int REQUEST_LIMIT = 10;
    private static final int TIME_WINDOW_SECONDS = 1000*10;
    @Autowired
    public RateLimiterService(@Qualifier("redisTokenTemplate") RedisTemplate<String, Object> redisTokenTemplate){
        this.redisTokenTemplate = redisTokenTemplate;
    }
    public boolean isAllowed(String userIP){
        String key = "rateLimit" + userIP;
        Long requestCount = redisTokenTemplate.opsForValue().increment(key,1);
        if(requestCount == 1){
            redisTokenTemplate.expire(key,TIME_WINDOW_SECONDS, TimeUnit.MILLISECONDS);
        }
        return requestCount <= REQUEST_LIMIT;
    }
}
