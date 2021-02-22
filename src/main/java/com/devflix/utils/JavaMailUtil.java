package com.devflix.utils;

import com.devflix.entity.MemberConfirm;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

@Component
public class JavaMailUtil {
    private final String SMTP_EMAIL = "jkl7343@gmail.com";
    private final String SMTP_PASSWORD = "cxbcvziegeotkxqj";
    private final String MESSAGE_TYPE = "text/html;charset=euc-kr";

    private void sendMail(final String title, final String content, final String to) {
        try {
            final MimeMessage msg = new MimeMessage(getSession());

            msg.setFrom(new InternetAddress(SMTP_EMAIL, "Devflix"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(title);
            msg.setContent(content, MESSAGE_TYPE);

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Properties getProperties() {
        Properties props = System.getProperties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return props;
    }

    private Session getSession() {
        return Session.getDefaultInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_EMAIL, SMTP_PASSWORD);
            }
        });
    }

    @Async("threadPoolTaskExecutor")
    public void emailConfirmSendMail(final MemberConfirm confirm, final UUID uuid) {
        final String title = "Devflix 이메일 인증하기";
        StringBuilder content = new StringBuilder();

        content.append("<div style=\"max-width:100%;width:600px;margin:0 auto;box-sizing:border-box;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;font-size:16px;line-height:22px;color:#252525;word-wrap:break-word;word-break:break-word;text-align:left;\">");
        content.append("<div id=\"email\" style=\"padding:24px 40px 24px 40px;margin:0\">");
        content.append("<div title=\"title\" style=\"font-size:24px;line-height:1.17;color:#252525;margin:0 0 40px 0;padding:0\">Devflix 이메일 인증하기</div>");
        content.append("<div title=\"content\" style=\"font-size:16px;line-height:22px;color:#252525\">\n" +
                "      <u></u> 안녕하세요 고객님,<br>\n" +
                "      <br>\n" +
                "      <u></u> 고객님께서는 <a href=" + confirm.getEmail() + " target=\"_blank\">" + confirm.getEmail() + "</a>을 Devflix ID로 등록하셨습니다.<br>\n" +
                "      <u></u> 이메일 주소를 인증하려면, 아래에 인증 코드를 입력하세요.<br>\n" +
                "      <p style=\"margin:40px 0 8px 0;padding:0;text-align:center;color:#8f8f8f;font-size:14px\"><u></u> CODE</p>\n" +
                "      <p style=\"margin:0 0 40px 0;padding:0;text-align:center;color:#252525;font-size:30px;letter-spacing:0.83;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word;line-height:33px\">" + uuid.toString() +"</p>      \n" +
                "      <p style=\"margin:0 0 40px 0;padding:0;font-size:16px;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\"><u></u> Devflix</p>\n" +
                "      <p style=\"margin:0 0 24px 0;padding:0;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word;font-size:12px;line-height:16px;color:#909090\">\n" +
                "        <u></u> 참고 : 이 메일은 발신 전용이므로 회신하실 수 없습니다. 궁금하신 사항은 다음 웹사이트로 문의해 주시기 바랍니다. :<br>\n" +
                "        <a href=\"\" style=\"color:#0072de;text-decoration:underline;font-size:12px;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.content.samsung.com/csweb/faq/searchFaq.do&amp;source=gmail&amp;ust=1613893849033000&amp;usg=AFQjCNFi5S8q0vP09p7u2fywGkDfVl1Kig\"><u></u> Devflix 바로가기</a>\n" +
                "      </p>\n" +
                "      <div title=\"footer\" style=\"margin:0;padding:40px 0 0 0;box-sizing:border-box\">\n" +
                "        <div title=\"footer-logo\" style=\"margin:0 0 2px 0;padding:0\"></div>\n" +
                "        <div title=\"copyright\" style=\"margin:0;padding:0;font-size:10px;opacity:0.6;font-weight:300;line-height:12px;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\">\n" +
                "          \n" +
                "        </div><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "      </div></div><div class=\"adL\">\n" +
                "    </div></div>");
        content.append("</div>");
        content.append("</div>");

        sendMail(title, content.toString(), confirm.getEmail());
    }

    @Async("threadPoolTaskExecutor")
    public void findPasswordSendMail(final MemberConfirm confirm, final UUID uuid, HttpServletRequest request) {
        final String title = "Devflix 비밀번호 찾기";
        final String NEW_PASSWORD_URL = request.getRequestURL().toString() + "login/new-password/" + uuid.toString();
        StringBuilder content = new StringBuilder();

        content.append("<div style=\"max-width:100%;width:600px;margin:0 auto;box-sizing:border-box;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;font-size:16px;line-height:22px;color:#252525;word-wrap:break-word;word-break:break-word;text-align:left;\">");
        content.append("<div id=\"email\" style=\"padding:24px 40px 24px 40px;margin:0\">");
        content.append("<div title=\"title\" style=\"font-size:24px;line-height:1.17;color:#252525;margin:0 0 40px 0;padding:0\">Devflix 패스워드 찾기</div>");
        content.append("<div title=\"content\" style=\"font-size:16px;line-height:22px;color:#252525\">\n" +
                "      <u></u> 안녕하세요 고객님,<br>\n" +
                "      <br>\n" +
                "      <u></u> 고객님께서는 <a href=" + confirm.getEmail() + " target=\"_blank\">" + confirm.getEmail() + "</a>을 Devflix 패스워드 찾기를 하셨습니다.<br>\n" +
                "      <u></u> 패스워드 재설정을 위해 하단 주소를 통해 비밀번호를 재설정해 주세요.<br>\n" +
                "      <p style=\"margin:40px 0 8px 0;padding:0;text-align:center;color:#8f8f8f;font-size:14px\"><u></u> URL</p>\n" +
                "      <p style=\"margin:0 0 40px 0;padding:0;text-align:center;color:#252525;font-size:30px;letter-spacing:0.83;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word;line-height:33px\"><a href=\"" + NEW_PASSWORD_URL +"\">패스워드 재설정</a></p>      \n" +
                "      <p style=\"margin:0 0 40px 0;padding:0;font-size:16px;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\"><u></u> Devflix</p>\n" +
                "      <p style=\"margin:0 0 24px 0;padding:0;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word;font-size:12px;line-height:16px;color:#909090\">\n" +
                "        <u></u> 참고 : 이 메일은 발신 전용이므로 회신하실 수 없습니다. 궁금하신 사항은 다음 웹사이트로 문의해 주시기 바랍니다. :<br>\n" +
                "        <a href=\"\" style=\"color:#0072de;text-decoration:underline;font-size:12px;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.content.samsung.com/csweb/faq/searchFaq.do&amp;source=gmail&amp;ust=1613893849033000&amp;usg=AFQjCNFi5S8q0vP09p7u2fywGkDfVl1Kig\"><u></u> Devflix 바로가기</a>\n" +
                "      </p>\n" +
                "      <div title=\"footer\" style=\"margin:0;padding:40px 0 0 0;box-sizing:border-box\">\n" +
                "        <div title=\"footer-logo\" style=\"margin:0 0 2px 0;padding:0\"></div>\n" +
                "        <div title=\"copyright\" style=\"margin:0;padding:0;font-size:10px;opacity:0.6;font-weight:300;line-height:12px;color:#252525;font-family:Arial,Helvetica,'sans-serif';font-weight:normal;word-wrap:break-word;word-break:break-word\">\n" +
                "          \n" +
                "        </div><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "      </div></div><div class=\"adL\">\n" +
                "    </div></div>");
        content.append("</div>");
        content.append("</div>");

        sendMail(title, content.toString(), confirm.getEmail());
    }
}
