package br.com.netlit;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DisplayEnvironmentVariablesListener implements
  ApplicationListener<ContextRefreshedEvent> {
 
	@Autowired
	private Environment env;
 
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {

    	
    	List<String> envVar = Arrays.asList(
    			"aws.dynamodb.table-prefix",
    			"aws.sns.topic",
    			"aws.sns.credentials.updated.topic",
    			"logging.level.com.amazonaws.auth.profile.internal.BasicProfileConfigLoader",
    			"logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter",
    			"security.oauth2.resource.user-info-uri",
    			"server.servlet.context-path",
    			"spring.application.name",
    			"spring.data.web.pageable.prefix",
    			"spring.jackson.default-property-inclusion",
    			"spring.jpa.open-in-view",
    			"spring.main.banner-mode",
    			"spring.profiles.active",
    			"spring.mail.host",
    			"spring.mail.port",
    			"spring.mail.username",
    			"spring.mail.password",
    			"spring.mail.properties.mail.smtp.auth",
    			"spring.mail.properties.mail.smtp.starttls.enable",
    			"spring.mail.properties.mail.smtp.starttls.required",
    			"spring.mail.properties.mail.smtp.ssl.enable",
    			"spring.mail.test-connection",
    			"netlit.protheus.url",
    			"aws.sqs.queue.sendmail"
    	);
    	
    	String format = "# %-90s#";
    	String border = String.format(format, "#").replace(" ", "#");
		System.out.println(border);
    	System.out.println(String.format(format, "ENVIRONMENT VARIABLES"));
    	envVar.stream().forEach(var -> System.out.println(String.format(format, var + "=" + env.getProperty(var))));
		System.out.println(border);
    }
}