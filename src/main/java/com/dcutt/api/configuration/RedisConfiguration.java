package com.dcutt.api.configuration;

import com.dcutt.api.model.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

@Configuration
public class RedisConfiguration {

    private RedisServer redisServer;

    @PostConstruct
    public void setup() throws IOException {
        if (redisServer == null) redisServer = new RedisServer();
        redisServer.start();
    }

    @PreDestroy
    public void tearDown() {
        redisServer.stop();
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, SortedMap<Long, SortedSet<Event>>> redisTemplate() {
        final RedisTemplate<String, SortedMap<Long, SortedSet<Event>>> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(TreeMap.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TreeMap.class));
        return template;
    }

}