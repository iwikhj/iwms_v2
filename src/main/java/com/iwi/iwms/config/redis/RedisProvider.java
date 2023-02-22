package com.iwi.iwms.config.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisProvider {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout) {
		/*
		 * opsForValue String 
		 * opsForList  List 
		 * opsForSet   Set 
		 * opsForZSet  Sorted Set
		 * opsForHash  Hash
		 */
    	redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public Object get(String key) {
    	return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
    	return redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
    	return redisTemplate.hasKey(key);
    }
	
	public void setHash(String key, String hashKey, Object value, long timeout) {
		redisTemplate.opsForHash().put(key, hashKey, value);
		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
	}
	
	public Object getHash(String key, String hashKey) {
		if(redisTemplate.opsForHash().hasKey(key, hashKey)) {
			return redisTemplate.opsForHash().get(key, hashKey);
		}
		return null;
	}

}