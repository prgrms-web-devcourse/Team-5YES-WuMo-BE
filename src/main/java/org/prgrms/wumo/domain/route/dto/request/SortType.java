package org.prgrms.wumo.domain.route.dto.request;

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
		for (SortType sortType : SortType.values()) {
			if (sortType.getValue().equals(value)) {
				return sortType;
			}
		}
		return null;
	}
}
