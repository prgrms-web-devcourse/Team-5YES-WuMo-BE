package org.prgrms.wumo.global.jwt;

import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieProvider {

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final int RESET = 0;
    private static final int EXPIRED = 604800000;

    public ResponseCookie generateTokenCookie(String refreshToken) {
        return generateTokenCookieBuilder(refreshToken)
                .maxAge(Duration.ofMinutes(EXPIRED))
                .build();
    }

    public ResponseCookie generateResetTokenCookie() {
        return generateTokenCookieBuilder("")
                .maxAge(RESET)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder generateTokenCookieBuilder(String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }
}
