package mailTest;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class Email2 {

	private static String EMAIL_CONFIG = "email_config.properties";
	private Properties emailProp = new Properties();
	
	public Email2() {
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
			System.out.println("userId:"+userId);
			System.out.println("password:"+password);
			return new PasswordAuthentication(userId, password);
		}
	}

	@SuppressWarnings("deprecation")
	public void send() throws Exception {

		try {
			String fromEmail = emailProp.getProperty("fromEmail", "");
			String toEmail = emailProp.getProperty("toEmails", "");
			String toEmail1 = emailProp.getProperty("toEmail", "");
			String toEmail2 = emailProp.getProperty("toEmail1", "");
			String description="string开会了";
			System.out.println("fromEmail:"+fromEmail);
			System.out.println("toEmails:"+toEmail);
			System.out.println("toEmail:"+toEmail1);
			System.out.println("toEmail1:"+toEmail2);
			Properties props = new Properties();
			try {
			    props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", "mail.vccloud.com.cn");
				//props.put("mail.smtp.port", "25");

			} catch (Exception e) {
				e.printStackTrace();
			}

			Session session;
			Authenticator authenticator = new EmailAuthenticator();
			session = Session.getInstance(props, authenticator);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail1));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail2));
			message.setSubject("Outlook Meeting Request Using JavaMail开会了");
//			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("0:yyyyMMddHHmmssZ");       
//			Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
//			String   str   =   formatter.format(curDate);
			StringBuffer buffer = new StringBuffer();
//			buffer.append("BEGIN:VCALENDAR\n"
//							+ "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
//							+ "VERSION:2.0\n" + "METHOD:REQUEST\n"
//							+ "BEGIN:VEVENT\n"
//							+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
//							+ toEmail
//							+ ";"
//							+ toEmail1
//							+ "\n"
//							+ "ORGANIZER:MAILTO:"
//							+ toEmail1
//							+ "\n"
//							+ "DTSTART:20140302T060000Z\n"
//							+ "DTEND:20140302T070000Z\n"
//							+ "LOCATION:Conference room\n"
//							+ "UID:"
//							+ UUID.randomUUID().toString()
//							+ "\n"// ���id��ͬ�Ļ���outlook����Ϊ��ͬһ��������������ʹ��uuid��
//							+ "CATEGORIES:SuccessCentral Reminder\n"
//							+ "DESCRIPTION:This the description of the meeting.����"
//							+ "SUMMARY:Test meeting request\n"
//							+ "PRIORITY:5\n"
//							+ "CLASS:PUBLIC\n"
//							+ "BEGIN:VALARM\n"
//							+ "TRIGGER:-PT15M\n"
//							+ "ACTION:DISPLAY\n"
//							+ "DESCRIPTION:Reminder\n"
//							+ "END:VALARM\n"
//							+ "END:VEVENT\n" + "END:VCALENDAR");
			buffer.append("BEGIN:VCALENDAR\n");
			buffer.append("PRODID:-//Events Calendar//iCal4j 1.0//EN");
			buffer.append("VERSION:2.0\n");
			buffer.append("METHOD:REQUEST\n");
			buffer.append("BEGIN:VEVENT\n");
//            if (attendees != null)
//            {
//                foreach (string attendee in attendees)
//                {
			buffer.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail+ "\n");
			buffer.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail1+ "\n");
			buffer.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail2+ "\n");
//			buffer.append("ATTENDEE;CN="+ toEmail+";RSVP=TRUE:mailto:" + toEmail+ "\n");
//			buffer.append("ATTENDEE;CN="+ toEmail1+";RSVP=TRUE:mailto:" + toEmail1+ "\n");
//			buffer.append("ATTENDEE;CN="+ toEmail2+";RSVP=TRUE:mailto:" + toEmail2+ "\n");
//                }
//            }
                    buffer.append("CLASS:PUBLIC\n");
                    buffer.append("CREATED:"/*+str*/+"\n");
                    buffer.append("DESCRIPTION;LANGUAGE=zh_CN;charset=\"UTF8\":"+description+"\n" );
                    buffer.append("DTEND:20140302T070000Z\n");
                    buffer.append("DTSTAMP:"/*+str*/+"\n");
                    buffer.append("DTSTART:20140302T060000Z\n");
                    buffer.append("ORGANIZER;CN=\"name\":mailto:" + toEmail1+ "\n");
                    buffer.append("SEQUENCE:0\n");
                    buffer.append("UID:" + UUID.randomUUID().toString()+ "\n");
                    buffer.append("LOCATION:Conference room\n" );
                    buffer.append("SUMMARY;LANGUAGE=zh_CN:Test meeting符文 request\n");
                    buffer.append("BEGIN:VALARM\n");
                    buffer.append("TRIGGER:-PT720M\n");
                    buffer.append("ACTION:DISPLAY\n");
                    buffer.append("DESCRIPTION:Reminder\n");
                    buffer.append("END:VALARM\n");
                    buffer.append("END:VEVENT\n");
                    buffer.append("END:VCALENDAR\n");
            
			BodyPart messageBodyPart = new MimeBodyPart();
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
			Email2 email = new Email2();
			email.send();
			System.out.println("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
