package br.com.netlit.accounts.domain.account.general.verification;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.AccountExistException;
import br.com.netlit.accounts.domain.account.error.InvalidTokenException;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class VerificationTokenService {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Autowired
    private MailService mailService;

    private final ObjectMapper mapper;
	private final VerificationTokenRepository repository;
	private final UserRepository userRepository;
	
	public VerificationTokenService(
			final ObjectMapper mapper,
			final VerificationTokenRepository repository,
			final UserRepository userRepository
			) {

		this.mapper = mapper;

		this.repository = repository;
		this.userRepository = userRepository;
	}

	public VerificationTokenEntity create(final String email, final String password, final String token, AccountType accountType) {

		VerificationTokenEntity entity = VerificationTokenEntity.build(email, password, token, accountType);
		this.repository.save(entity);

		return entity;
	}

	public Optional<VerificationTokenEntity> findBy(final String token) {
		return this.repository.findByToken(token);
	}

	public void expiryVerificationToken(final VerificationTokenEntity tokenEntity) {

		tokenEntity.setExpiryDate(LocalDateTime.now());
		tokenEntity.setVerified(true);
		
		this.repository.save(tokenEntity);

	}

	public void resend(final String email) {
		
		VerificationTokenEntity tokenEntity = this.repository.findByEmail(email).orElseThrow(InvalidTokenException::new);
	 
        String newToken = UUID.randomUUID().toString();
        
        // It updates expiry date 
        VerificationTokenEntity updatedTokenEntity = VerificationTokenEntity
        		.builder()
        		.id(tokenEntity.getId())
        		.email(tokenEntity.getEmail())
        		.token(newToken)
        		.expiryDate(VerificationTokenEntity.calculateExpiryDate(VerificationTokenEntity.EXPIRATION))
        		.build();
        
        this.repository.save(updatedTokenEntity);
        
        String confirmationUrl  =
        
        ServletUriComponentsBuilder.fromCurrentRequest().path("/").buildAndExpand().toUri().toString() + "?token=" + newToken;
        
        mailService.createdEmail(tokenEntity.getEmail(), confirmationUrl.toString(), true);
		
	}

	public AccountType check(final String email) {
		
		VerificationTokenEntity tokenEntity = this.repository.findByEmail(email).orElseThrow(InvalidTokenException::new);

		Optional<UserEntity> userEntityOptional = this.userRepository.findById(tokenEntity.getEmail());
		
		if (tokenEntity.isVerified() && userEntityOptional.get().isEnabled())
			throw new AccountExistException(email);
		 
		 return tokenEntity.getAccountType();
	}

}
