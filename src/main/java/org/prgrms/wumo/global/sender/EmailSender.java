package org.prgrms.wumo.global.sender;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.prgrms.wumo.global.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSender implements Sender {

	@Value("${wumo.mail}")
	private String FromAddress;

	private static final String CODE_SUBJECT = "WuMo(우리들의 모임) 회원가입 이메일 인증 코드입니다.";
	private static final String CODE_CONTENT = "회원가입 화면에 아래의 이메일 인증 코드를 입력해주세요\n";
	private static final int SAVE_SECONDS = 180;

	private static final Random random = new Random();

	private final JavaMailSender javaMailSender;
	private final RedisRepository redisRepository;

	@Override
	public void sendCode(String toAddress) {
		try {
			String mailCode = generateMailCode();
			MimeMessage message = getMail(toAddress, CODE_CONTENT, CODE_SUBJECT + mailCode);
			javaMailSender.send(message);
			redisRepository.save(toAddress, mailCode, SAVE_SECONDS);
		} catch (MessagingException exception) {
			throw new MailSendException("메일 전송에 실패하였습니다.");
		}
	}

	private MimeMessage getMail(String toAddress, String subject, String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

		messageHelper.setTo(toAddress);
		messageHelper.setFrom(FromAddress);
		messageHelper.setSubject(subject);
		messageHelper.setText(content);

		return message;
	}

	private String generateMailCode() {
		StringBuilder emailCode = new StringBuilder();
		emailCode.append(random.nextInt(100000, 1000000));

		return emailCode.toString();
	}
}
