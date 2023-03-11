package org.prgrms.wumo.global.sender;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.prgrms.wumo.global.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSender implements Sender {

	@Value("${wumo.mail}")
	private String fromAddress;

	private static final String CODE_SUBJECT = "WuMo(우리들의 모임) 이메일 인증 코드입니다.";
	private static final String CODE_CONTENT = "WuMo(우리들의 모임) 화면에 아래의 이메일 인증 코드를 입력해주세요\n";
	private static final int SAVE_SECONDS = 180000;

	private static final String WELCOME_SUBJECT = "WuMo(우리들의 모임) 회원가입을 환영합니다.";
	private static final String WELCOME_CONTENT = "WuMo(우리들의 모임)에서 약속, 여행 일정을 편하게 관리하며 추억을 남겨보세요!";

	private static final Random random = new Random();

	private final JavaMailSender javaMailSender;
	private final RedisRepository redisRepository;

	@Override
	public void sendCode(String toAddress) {
		try {
			String verificationCode = generateVerificationCode();
			MimeMessage message = getMessage(toAddress, CODE_SUBJECT, CODE_CONTENT + verificationCode);
			javaMailSender.send(message);
			redisRepository.save(toAddress, verificationCode, SAVE_SECONDS);
		} catch (MessagingException exception) {
			throw new MailSendException("메일 전송에 실패하였습니다.");
		}
	}

	@Async
	@Override
	public void sendWelcome(String toAddress) {
		try {
			MimeMessage message = getMessage(toAddress, WELCOME_SUBJECT, WELCOME_CONTENT);
			javaMailSender.send(message);
		} catch (MessagingException exception) {
			throw new MailSendException("메일 전송에 실패하였습니다.");
		}
	}

	private MimeMessage getMessage(String toAddress, String subject, String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

		messageHelper.setTo(toAddress);
		messageHelper.setFrom(fromAddress);
		messageHelper.setSubject(subject);
		messageHelper.setText(content);

		return message;
	}

	private String generateVerificationCode() {
		return String.valueOf(random.nextInt(100000, 1000000));
	}
}
