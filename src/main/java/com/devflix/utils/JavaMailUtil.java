package com.devflix.utils;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class JavaMailUtil {
    private static final String SMTP_EMAIL = "jkl7343@gmail.com";
    private static final String SMTP_PASSWORD = "cxbcvziegeotkxqj";
    private static final String MESSAGE_TYPE = "text/html;charset=euc-kr";

    public static void sendMail(final String title, final String content, final String... to) {
        for (final String address : to) {
            try {
                final MimeMessage msg = new MimeMessage(getSession());

                msg.setFrom(SMTP_EMAIL);
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
                msg.setSubject(title);
                msg.setContent(content, MESSAGE_TYPE);

                Transport.send(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties getProperties() {
        Properties props = System.getProperties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return props;
    }

    private static Session getSession() {
        return Session.getDefaultInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_EMAIL, SMTP_PASSWORD);
            }
        });
    }
}
