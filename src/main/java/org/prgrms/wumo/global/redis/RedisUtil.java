package org.prgrms.wumo.global.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {

	private final StringRedisTemplate stringRedisTemplate;

	public void save(String key, String value, long expireSeconds) {
		ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set(key, value, Duration.ofSeconds(expireSeconds));
	}

	public String get(String key) {
		ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}
}
