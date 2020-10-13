package br.com.netlit.customerservice.domain.service;

import br.com.netlit.customerservice.domain.MailClient;
import br.com.netlit.customerservice.domain.resource.data.SchoolCityRequest;
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
public class SchoolCityService {

  @Autowired
  private MailClient mailClient;

  private String emailSales1;
  private String emailSales2;

  public SchoolCityService(@Value("${application.email_sales1}") String emailSales1,
      @Value("${application.email_sales2}") String emailSales2) {
    this.emailSales1 = emailSales1;
    this.emailSales2 = emailSales2;
  }

  public void createdEmail(SchoolCityRequest schoolCityRequest) {

    log.info("initializing email sending");
    Locale locale = getLocale("pt");

    log.info("e-mail mounting {}", schoolCityRequest);
    final Context ctx = new Context(locale);

    ctx.setVariable("varSchool", schoolCityRequest.getSchool());
    ctx.setVariable("varName", schoolCityRequest.getName());
    ctx.setVariable("varFunction", schoolCityRequest.getFunction());
    ctx.setVariable("varEmail", schoolCityRequest.getEmail());
    ctx.setVariable("varTelephone", schoolCityRequest.getTelephone());
    ctx.setVariable("varState", schoolCityRequest.getState());
    ctx.setVariable("varCity", schoolCityRequest.getCity());
    ctx.setVariable("varNeed", schoolCityRequest.getNeed());

    log.info("initializing template mounting");
    String messageText = templateEngine().process("/templates/emailSchoolCity.html", ctx);

    String[] recipients = {emailSales1, emailSales2};
    log.info("sending e-mail to {}", recipients);
    String assunto = messageSourceSales().getMessage("Vendas", null, locale)
        + " - " + schoolCityRequest.getName();

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
  public ResourceBundleMessageSource messageSourceSales() {
    ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
    msgSrc.setBasename("locale/messages");
    msgSrc.setUseCodeAsDefaultMessage(true);
    return msgSrc;
  }

  public Locale getLocale(String language) {
    return new Locale("pt", "BR");
  }


}
