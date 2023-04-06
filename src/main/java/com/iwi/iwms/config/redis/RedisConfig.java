package com.iwi.iwms.config.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    // lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        
        //기본: key 직렬화
        //value는 GenericJackson2JsonRedisSerializer로 json 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        
        //Hash: key:hashKey 직렬화
        //value는 GenericJackson2JsonRedisSerializer로 json 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //디폴트 직렬화
        //redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        
        //Hash value만 Json으로 직렬화 
        var serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);

        return redisTemplate;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
    	var mapper = new ObjectMapper();
    	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    	mapper.registerModules(new JavaTimeModule(), new Jdk8Module());
    	return mapper;
    }
    
}
