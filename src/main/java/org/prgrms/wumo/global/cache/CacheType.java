package org.prgrms.wumo.global.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
	ROUTES("routes", ConstantConfig.DEFAULT_TTL_SEC, ConstantConfig.DEFAULT_MAX_SIZE);

	private final String cacheName;
	private final int expiredAfterWrite;
	private final int maximumSize;

	static class ConstantConfig {
		static final int DEFAULT_TTL_SEC = 900;
		static final int DEFAULT_MAX_SIZE = 10000;
	}
}
