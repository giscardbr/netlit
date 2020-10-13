package br.com.netlit.customerservice.domain.service;

import br.com.netlit.customerservice.domain.MailClient;
import br.com.netlit.customerservice.domain.resource.data.ContactUsRequest;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Service
@Log4j2
public class ContactUsService {

  @Autowired
  private MailClient mailClient;

  private String emailTo;

  public ContactUsService(@Value("${application.emailTo}") String emailTo) {
    this.emailTo = emailTo;
  }

  public void createdEmail(ContactUsRequest contactUsRequest) {

    log.info("initializing email sending");
    Locale locale = getLocale("pt");

    log.info("e-mail mounting {}", contactUsRequest);
    final Context ctx = new Context(locale);
    ctx.setVariable("varName", contactUsRequest.getName());
    ctx.setVariable("varEmail", contactUsRequest.getEmail());
    ctx.setVariable("varTelephone", contactUsRequest.getTelephone());
    ctx.setVariable("varSchool", contactUsRequest.getSchool() == null ? "" : contactUsRequest.getSchool());
    ctx.setVariable("varCity", contactUsRequest.getCity());
    ctx.setVariable("varState", contactUsRequest.getState());
    ctx.setVariable("varSubject", contactUsRequest.getSubject());
    ctx.setVariable("varRequestNumber", contactUsRequest.getRequestNumber() == null ? "" : contactUsRequest.getRequestNumber());
    ctx.setVariable("varMessage", contactUsRequest.getMessage());

    log.info("initializing template mounting");
    String messageText = templateEngine().process("/templates/emailTemplate.html", ctx);

    String[] recipients = {emailTo};
    log.info("sending e-mail to {}", emailTo);
    String assunto =
        messageSourceContactUs().getMessage(contactUsRequest.getSubject(), null, locale)
            + " - " + contactUsRequest.getName();
    mailClient.sendEmail(assunto, recipients, messageText);
  }

  public TemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(templateResolver());
    return templateEngine;
  }

  public ITemplateResolver templateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    return templateResolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSourceContactUs() {
    ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
    msgSrc.setBasename("locale/messages");
    msgSrc.setUseCodeAsDefaultMessage(true);
    return msgSrc;
  }

  public Locale getLocale(String language) {
    return new Locale("pt", "BR");
  }


}
