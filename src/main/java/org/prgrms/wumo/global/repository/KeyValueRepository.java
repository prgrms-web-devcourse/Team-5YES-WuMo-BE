package org.prgrms.wumo.global.repository;

public interface KeyValueRepository {
	void save(String key, String value, long expireSeconds);

	String get(String key);

	void delete(String key);
}
