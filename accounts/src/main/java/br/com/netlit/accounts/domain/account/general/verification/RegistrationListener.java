package br.com.netlit.accounts.domain.account.general.verification;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import lombok.val;

@Component
public class RegistrationListener implements
  ApplicationListener<OnRegistrationCompleteEvent> {
  
    @Autowired
    private VerificationTokenService service;
  
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Autowired
    private MailService mailService;
 
    @Autowired
	private UserRepository userRepository;
    
    @Autowired
	private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
 
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
    	
		val email = Optional.of(event.getUser().getEmail()).orElseThrow(NullAccountEmailException::new);
		if (this.userRepository.exists(email))
			throw new EmailAlreadyUsedException(email);
		
		if (this.verificationTokenRepository.findByEmail(email).isPresent())
			throw new EmailAlreadyUsedException(email);

        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.create(user.getEmail(), user.getPassword(), token, event.getAccountType());
         
        String confirmationUrl 
          = ServletUriComponentsBuilder.fromCurrentRequest().path("/").build().toUri() + "?token=" + token;

        mailService.createdEmail(user.getEmail(), confirmationUrl, true);
    }
}
