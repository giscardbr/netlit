package br.com.netlit.customerservice.domain;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Log4j2
public class MailClient {

    @Autowired
    private JavaMailSender mailSender;

    public MailClient() {
    }

    public void sendEmail(String subject, String recipient[], String message) {
        log.info("sending email");
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.setFrom("no-reply@editoradobrasil.com.br");

            mailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
