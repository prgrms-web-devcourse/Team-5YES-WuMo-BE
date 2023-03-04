package org.prgrms.wumo.global.util;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

	private final RedisTemplate redisTemplate;

	public void save(String key, String value, long expireSeconds) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value, Duration.ofSeconds(expireSeconds));
	}

	public String get(String key) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}
}
