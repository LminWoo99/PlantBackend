package Plant.PlantProject.service.user;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


public interface MailServiceInter {

	// 메일 내용 작성
	MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;


	// 랜덤 인증 코드 전송
	String createKey();

	// 메일 발송
	String sendSimpleMessage(String to) throws Exception;
}