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

    public boolean set(String key, Object value, long timeout) {
		/*
		 * opsForValue String 
		 * opsForList  List 
		 * opsForSet   Set 
		 * opsForZSet  Sorted Set
		 * opsForHash  Hash
		 */
    	
		try {
			redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch(Exception e) {}
		
		return false;
    }

    public Object get(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch(Exception e) {}
		
		return null;
    }

    public boolean delete(String key) {
		try {
			 return redisTemplate.delete(key);
		} catch(Exception e) {}
		
		return false;
    }

    public boolean hasKey(String key) {
		try {
			 return redisTemplate.hasKey(key);
		} catch(Exception e) {}
		
		return false;
    }
	
	public void setHash(String key, String hashKey, Object value, long timeout) {
		redisTemplate.opsForHash().put(key, hashKey, value);
		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
	}
	
	public Object getHash(String key, String hashKey) {
		try {
			return redisTemplate.opsForHash().get(key, hashKey);
		} catch(Exception e) {}
		
		return null;
	}

}