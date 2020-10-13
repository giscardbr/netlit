package br.com.netlit.accounts.domain.account.general.verification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.ExpiredTokenException;
import br.com.netlit.accounts.domain.account.error.InvalidTokenException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import lombok.val;

@RestController
@RequestMapping(RegistrationResource.PATH)
public class RegistrationResource {
	
	public static final String PATH = "/registration";

	final private ApplicationEventPublisher eventPublisher;
	
	final private RegistrationService registrationService;

	final private VerificationTokenService verificationTokenService;

	private final CredentialsService credentialsService;

	public RegistrationResource(
			final ApplicationEventPublisher eventPublisher, 
			final RegistrationService registrationService,
			final VerificationTokenService verificationTokenService,
			final CredentialsService credentialsService
			) {
		super();
		this.eventPublisher = eventPublisher;
		this.registrationService = registrationService;
		this.verificationTokenService = verificationTokenService;
		this.credentialsService = credentialsService;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody final @Valid RegistrationCreateRequest req) {

		UserEntity userEntity = 
				UserEntity.builder()
				.email(req.getEmail())
				.password(req.getPassword())
				.build();
		
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity, req.getAccountType()));
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/resend", method = RequestMethod.GET)
	public ResponseEntity<?> registrationResend(@RequestParam("email") String email) throws IOException{

		this.verificationTokenService.resend(email);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public ResponseEntity<RegistrationCheckResponse> registrationCheck(@RequestParam("email") String email) throws IOException{

		RegistrationCheckResponse response = RegistrationCheckResponse.builder()
			.accountType(this.verificationTokenService.check(email))
			.build();
		
		return new ResponseEntity<RegistrationCheckResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping
	public void confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException{
		
		try {
			
			VerificationTokenEntity tokenEntity = this.verificationTokenService.findBy(token).orElseThrow(InvalidTokenException::new);
			
			if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
				
				throw new ExpiredTokenException();
			}
			
			this.verificationTokenService.expiryVerificationToken(tokenEntity);
			
			this.registrationService.createUserEntity(UserEntity.builder()
					.email(tokenEntity.getEmail())
					.password(tokenEntity.getPassword())
					.build());

		} catch (Exception e) {

			response.sendRedirect(String.format("http://netlit-web.s3-website-us-east-1.amazonaws.com/error?message=%s&lang=%s", e.getMessage(), response.getLocale().getLanguage())); 
			return;
		}
	    
	    response.sendRedirect("http://netlit-web.s3-website-us-east-1.amazonaws.com/confirmation?lang=" + response.getLocale().getLanguage());
	    return;
	}	
}
