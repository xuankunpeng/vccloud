package mailTest;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.ByteArrayDataSource;

public class Email {
	private static String EMAIL_CONFIG = "email_config1.properties";
	private Properties emailProp = new Properties();

	public Email() {
		InputStream is = getClass().getResourceAsStream("/" + EMAIL_CONFIG);
		try {
			emailProp.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class EmailAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			String userId = emailProp.getProperty("userId", "");
			String password = emailProp.getProperty("password", "");
			return new PasswordAuthentication(userId, password);
		}
	}

	public void send() throws Exception {

		try {
			String fromEmail = emailProp.getProperty("fromEmail", "");
			String toEmail = emailProp.getProperty("toEmails", "");
			String toEmail1 = emailProp.getProperty("toEmail", "");
			System.out.println(fromEmail);
			System.out.println(toEmail);
			System.out.println(toEmail1);
			Properties props = new Properties();
			try {
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.host", "smtp.163.com");
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl", "true");

			} catch (Exception e) {
				e.printStackTrace();
			}

			Session session;
			Authenticator authenticator = new EmailAuthenticator();
			session = Session.getInstance(props, authenticator);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail));
			message.setSubject("Outlook Meeting Request Using JavaMail");
			StringBuffer buffer = new StringBuffer();
			buffer.append("BEGIN:VCALENDAR\n"
							+ "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
							+ "VERSION:2.0\n" + "METHOD:REQUEST\n"
							+ "BEGIN:VEVENT\n"
							+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
							+ toEmail
							+ "\n"
							+ "ORGANIZER:MAILTO:"
							+ toEmail1
							+ "\n"
							+ "DTSTART:20140302T060000Z\n"
							+ "DTEND:20140302T070000Z\n"
							+ "LOCATION:Conference room\n"
							+ "UID:"
							+ UUID.randomUUID().toString()
							+ "\n"// 如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。
							+ "CATEGORIES:SuccessCentral Reminder\n"
							+ "DESCRIPTION:This the description of the meeting."
							+ "SUMMARY:Test meeting request\n"
							+ "PRIORITY:5\n"
							+ "CLASS:PUBLIC\n"
							+ "BEGIN:VALARM\n"
							+ "TRIGGER:-PT15M\n"
							+ "ACTION:DISPLAY\n"
							+ "DESCRIPTION:Reminder\n"
							+ "END:VALARM\n"
							+ "END:VEVENT\n" + "END:VCALENDAR");
			BodyPart messageBodyPart = new MimeBodyPart();
			// 测试下来如果不这么转换的话，会以纯文本的形式发送过去，
			// 如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求
			messageBodyPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(buffer.toString(),
							"text/calendar;method=REQUEST;charset=\"UTF-8\"")));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Email email = new Email();
			email.send();
			System.out.println("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
