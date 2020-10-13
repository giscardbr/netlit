//package br.com.netlit.accounts.infra.mail;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailProvider {
//
//  @Bean
//  public JavaMailSender javaMailSender() {
//    final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//    mailSender.setHost("smtp.gmail.com");
//    mailSender.setPort(587);
//
//    @Value("${aws.sns.credentials.updated.topic}") String senha;
//    mailSender.setUsername(@Value("${EMAIL}"));
//    mailSender.setPassword("${PASSWORD}");
//
//    final Properties props = mailSender.getJavaMailProperties();
//    props.put("mail.transport.protocol", "smtp");
//    props.put("mail.smtp.auth", "true");
//    props.put("mail.smtp.starttls.enable", "true");
//    props.put("mail.debug", "true");
//
//    return mailSender;
//  }
//}
