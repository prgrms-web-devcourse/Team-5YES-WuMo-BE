package org.prgrms.wumo.domain.route.dto.request;

import java.util.Arrays;

import javax.validation.ValidationException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum SortType {
	NEWEST, LIKES;

	@JsonCreator
	public static SortType from(String value) {
		return Arrays.stream(SortType.values())
			.filter(sortType -> sortType.name().equals(value))
			.findFirst()
			.orElseThrow(() -> new ValidationException("올바른 정렬 기준이 아닙니다."));
	}
}
