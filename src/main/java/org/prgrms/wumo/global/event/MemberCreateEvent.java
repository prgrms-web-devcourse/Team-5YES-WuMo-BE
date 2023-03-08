package org.prgrms.wumo.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberCreateEvent {

	private final String email;
}
