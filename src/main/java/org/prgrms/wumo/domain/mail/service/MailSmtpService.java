package org.prgrms.wumo.domain.mail.service;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSmtpService implements MailService {

	private static final String WUMO_MAIL = "5yeswumo@gmail.com";

	private static final String CODE_SUBJECT = "WuMo(우리들의 모임) 회원가입 이메일 인증 코드입니다.";
	private static final String CODE_CONTENT = "회원가입 화면에 아래의 이메일 인증 코드를 입력해주세요\n";

	private static final Random random = new Random();

	private final JavaMailSender javaMailSender;

	@Override
	public void sendCodeMail(String toAddress) {
		try {
			MimeMessage message = getMail(toAddress, CODE_CONTENT, CODE_SUBJECT + generateMailCode());
			javaMailSender.send(message);
		} catch (MessagingException exception) {
			throw new MailSendException("메일 전송에 실패하였습니다.");
		}
	}

	private MimeMessage getMail(String toAddress, String subject, String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

		messageHelper.setTo(toAddress);
		messageHelper.setFrom(WUMO_MAIL);
		messageHelper.setSubject(subject);
		messageHelper.setText(content);

		return message;
	}

	private String generateMailCode() {
		StringBuilder emailCode = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			emailCode.append(random.nextInt(10));
		}

		return emailCode.toString();
	}
}
