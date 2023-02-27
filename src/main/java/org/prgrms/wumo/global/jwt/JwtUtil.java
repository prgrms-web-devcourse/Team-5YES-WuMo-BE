package org.prgrms.wumo.global.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUtil {
	public static long getMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (long)authentication.getPrincipal();
	}
}
