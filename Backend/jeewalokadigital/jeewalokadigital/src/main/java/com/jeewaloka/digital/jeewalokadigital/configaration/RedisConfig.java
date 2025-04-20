package com.jeewaloka.digital.jeewalokadigital.configaration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    // No need for separate methods for the same connection factory config
    // Use this one factory bean where needed if host/port are the same.
    // If you need different settings (like password), create separate ones.
    private LettuceConnectionFactory createLettuceConnectionFactory(int database) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost); // Use injected host
        config.setPort(redisPort);     // Use injected port
        config.setDatabase(database);
        // config.setPassword(...) // Add if you have a password set
        return new LettuceConnectionFactory(config);
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setDatabase(0);
//        return new LettuceConnectionFactory(config);
        return createLettuceConnectionFactory(0);
    }
    @Bean
    @Lazy
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

//    @Bean
//    public RedisConnectionFactory redisCacheConnectionFactory(){
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setDatabase(0);
//        return new LettuceConnectionFactory(config);
//    }
//    @Bean
//    @Lazy
//    public RedisTemplate<String, Object> redisCacheTemplate(){
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisCacheConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }

    @Bean
    public RedisConnectionFactory redisTokenConnectionFactory(){
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setDatabase(1);
//        return new LettuceConnectionFactory(config);
        return createLettuceConnectionFactory(1);
    }
    @Bean
    @Lazy
    public RedisTemplate<String, Object> redisTokenTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisTokenConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
