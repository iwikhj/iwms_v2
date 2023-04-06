package com.iwi.iwms.config.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

import lombok.RequiredArgsConstructor;

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
    	try {
        	redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}    	
    }

    public Object get(String key) {
    	try {
    		return redisTemplate.opsForValue().get(key);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}
    }

    public boolean delete(String key) {
    	try {
        	return redisTemplate.delete(key);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}
    }

    public boolean hasKey(String key) {
    	try {
        	return redisTemplate.hasKey(key);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}
    }
	
	public void setHash(String key, String hashKey, Object value, long timeout) {
    	try {
    		redisTemplate.opsForHash().put(key, hashKey, value);
    		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}		
	}
	
	public Object getHash(String key, String hashKey) {
    	try {
    		return redisTemplate.opsForHash().get(key, hashKey);
    	} catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
    	}
	}

}