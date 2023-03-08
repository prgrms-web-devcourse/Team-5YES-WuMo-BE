package org.prgrms.wumo.global.sender;

public interface Sender {
	void sendCode(String toAddress);

	void sendWelcome(String toAddress);
}
