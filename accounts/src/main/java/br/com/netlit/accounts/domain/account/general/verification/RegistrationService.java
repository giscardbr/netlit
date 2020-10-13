package br.com.netlit.accounts.domain.account.general.verification;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.entity.Role;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class RegistrationService {

	private final ApplicationEventPublisher publisher;
	private final ObjectMapper mapper;
	private final UserRepository userRepository;
	
	public RegistrationService(
			final ApplicationEventPublisher publisher, 
			final ObjectMapper mapper,
			final UserRepository userRepository
			) {

		this.publisher = publisher;
		this.mapper = mapper;

		this.userRepository = userRepository;
		
	}

	public UserEntity createUserEntity(@Valid UserEntity user) {

		val email = Optional.of(user.getEmail()).orElseThrow(NullAccountEmailException::new);
		if (this.userRepository.exists(email))
			throw new EmailAlreadyUsedException(email);

		UserEntity userEntity = UserEntity.builder()
				.email(email)
				.enabled(false)
				.role(Role.ADMIN)
				.password(user.getPassword())
				.build();
		
		this.userRepository.save(userEntity);
		
		return userEntity;
	}

	public UserEntity enableUser(@Valid UserEntity user) {

		UserEntity userEntity = UserEntity.builder()
				.email(user.getEmail())
				.enabled(true)
				.role(user.getRole())
				.password(user.getPassword())
				.build();
		
		this.userRepository.save(userEntity);
		
		return userEntity;
	}

	public Optional<UserEntity> findUserByEmail(final String email) {
		return this.userRepository.findById(email);
	}
}
