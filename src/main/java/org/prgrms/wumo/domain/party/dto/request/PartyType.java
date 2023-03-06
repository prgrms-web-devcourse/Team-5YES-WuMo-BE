package org.prgrms.wumo.domain.party.dto.request;

import java.util.Arrays;

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
				.orElse(null);
	}

}
