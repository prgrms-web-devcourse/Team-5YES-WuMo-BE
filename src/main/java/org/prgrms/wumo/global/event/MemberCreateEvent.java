package org.prgrms.wumo.global.event;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@Getter
@RequiredArgsConstructor
public class MemberCreateEvent {

	private final String email;
}
