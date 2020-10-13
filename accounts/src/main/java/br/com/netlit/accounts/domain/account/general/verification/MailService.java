package br.com.netlit.accounts.domain.account.general.verification;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import br.com.netlit.accounts.domain.event.EmailEvent;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MailService {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MailClient mailClient;

	public void createdEmail(final String email, final String confirmResgistrationLink, final Boolean isHtmlText) {

		log.info("initializing email sending");
		Locale locale = getLocale("pt");

		log.info("e-mail mounting {}", email);
		final Context ctx = new Context(locale);
		ctx.setVariable("varConfirmRestrationLink", confirmResgistrationLink);

		log.info("initializing template mounting");
		String messageText = templateEngine().process("/templates/emailTemplate.html", ctx);

		String[] recipients = { email };
		log.info("sending e-mail to {}", email);
		String assunto = messageSourceContactUs().getMessage("Confirmar e-mail", null, locale);
		
		publisher.publishEvent(new EmailEvent(this, messageText, null, recipients, assunto, isHtmlText));

//		mailClient.sendEmail(assunto, recipients, messageText, isHtmlText);
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
