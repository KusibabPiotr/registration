package my.app.registration.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;
    private static final String CONFIRMATION_SUBJECT = "Confirm your email";
    @Value(value = "${admin.email}")
    private String adminEmail;

    @Override
    public void send(String to, String email) {
        try {
            MimeMessage myMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(myMessage, "utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject(CONFIRMATION_SUBJECT);
            helper.setFrom(adminEmail);
            mailSender.send(myMessage);
        } catch (MessagingException | MailSendException e) {
            log.error("Failed to send email", e);
        }
    }
}
