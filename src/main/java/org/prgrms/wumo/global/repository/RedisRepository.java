package org.prgrms.wumo.global.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRepository implements KeyValueRepository {

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void save(String key, String value, long expireSeconds) {
		ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set(key, value, Duration.ofSeconds(expireSeconds));
	}

	@Override
	public String get(String key) {
		ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	@Override
	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}
}
