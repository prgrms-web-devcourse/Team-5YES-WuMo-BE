package org.prgrms.wumo.domain.route.dto.request;

import java.util.Arrays;

import javax.validation.ValidationException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum SortType {
	NEWEST("NEWEST"),
	LIKES("LIKES");

	private final String value;

	SortType(String value) {
		this.value = value;
	}

	@JsonCreator
	public static SortType from(String value) {
		return Arrays.stream(SortType.values())
			.filter(sortType -> sortType.equals(value))
			.findFirst()
			.orElseThrow(() -> new ValidationException("올바른 정렬 기준이 아닙니다."));
	}
}
