package org.prgrms.wumo.domain.party.dto.request;

import java.util.Arrays;

import javax.validation.ValidationException;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PartyType {

	ONGOING("ONGOING"),
	COMPLETED("COMPLETED"),
	ALL("ALL");

	private final String value;

	PartyType(String value) {
		this.value = value;
	}

	@JsonCreator
	public static PartyType from(String value) {
		return Arrays.stream(PartyType.values())
				.filter(partyType -> partyType.value.equals(value))
				.findFirst()
				.orElseThrow(() -> new ValidationException("올바른 조회 기준이 아닙니다."));
	}

}
