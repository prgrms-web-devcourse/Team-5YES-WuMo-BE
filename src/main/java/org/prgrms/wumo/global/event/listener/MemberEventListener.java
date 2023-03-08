package org.prgrms.wumo.global.event.listener;

import org.prgrms.wumo.global.event.MemberCreateEvent;
import org.prgrms.wumo.global.sender.Sender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

	private final Sender sender;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
	public void handleMemberCreateEvent(MemberCreateEvent memberCreateEvent) {
		sender.sendWelcome(memberCreateEvent.getEmail());
	}
}
