package org.prgrms.wumo.global.base62;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base62Util {

	// 어순 배열을 일반적인 방법(0-9A-Za-z)에서 변경시켜 해독하기 어려도록 변경
	private static final char[] BASE62 = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();

	public static String encode(long value) {
		StringBuilder sb = new StringBuilder();
		while (value > 0) {
			int i = (int)(value % 62L);
			sb.append(BASE62[i]);
			value /= 62L;
		}
		return sb.toString();
	}

	public static int decode(String value) {
		int result = 0;
		int power = 1;
		String S_BASE62 = new String(BASE62);
		for (int i = 0; i < value.length(); i++) {
			int digit = S_BASE62.indexOf(value.charAt(i));
			result += digit * power;
			power *= 62;
		}
		return result;
	}

}
