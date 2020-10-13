package br.com.netlit.accounts.domain.account.parent.service;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.AccountNotFoundException;
import br.com.netlit.accounts.domain.account.error.CredentialsNotFoundException;
import br.com.netlit.accounts.domain.account.error.UserNotFoundException;
import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.SchoolEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import br.com.netlit.accounts.domain.event.ParentUserCreationRequestEvent;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class SubAccountService {
	
	private final AccountRepository accountRepo;
	private final CredentialsService credentialsService;
	private final CredentialsRepository credentialsRepo;  
	private final ApplicationEventPublisher publisher;
	private final ObjectMapper mapper;
	private final UserRepository userRepository;
	private final SubAccountRepository subAccountRepository;
	
	public SubAccountService(
			final ApplicationEventPublisher publisher, 
			final AccountRepository accountRepo,
			final CredentialsService credentialsService, 
			final ObjectMapper mapper,
			final UserRepository userRepository, 
			final SubAccountRepository subAccountRepository,
			final CredentialsRepository credentialsRepo
			
			) {

		this.publisher = publisher;
		this.accountRepo = accountRepo;
		this.credentialsService = credentialsService;
		this.mapper = mapper;
		this.userRepository = userRepository;
		this.subAccountRepository = subAccountRepository;
		this.credentialsRepo = credentialsRepo;
	}
	
	public ParentUserCreationRequest findById(final UUID subAccountId) {
		
		SubAccountEntity subAccount = subAccountRepository.findById(subAccountId).orElseThrow(AccountNotFoundException::new);
		UserEntity user = userRepository.findBy(subAccountId).iterator().next();
		CredentialsEntity credentials = credentialsRepo.findById(user.getEmail()).orElseThrow(CredentialsNotFoundException::new);
		
		ParentUserCreationRequest response = ParentUserCreationRequest.valueOf(subAccount, credentials, user);
		
		return response;
	}

	@Validated(OnAdminAccountCreation.class)
	public void create(final @Valid @NotNull ParentUserCreationRequest request) {

		CredentialsCreationRequest credentials = CredentialsCreationRequest
			.builder()
				.email(request.getCredentialsCreationRequest().getEmail())
				.password(request.getCredentialsCreationRequest().getPassword())
				.accountId(request.getAccountId())
			.build();
//		this.credentialsService.create(credentials);

//		this.publisher.publishEvent(new ParentUserCreationRequestEvent(this, request));
		
	}

	public void update(final UUID accountId, final @Valid @NotNull ParentUserCreationRequest request) {
		log.info("Updating sub account");

			SubAccountEntity saEntity = subAccountRepository.findById(UUID.fromString(request.getId()))
					.orElseThrow(AccountNotFoundException::new);
			saEntity.setBirthDate(request.getBirthDate());
			saEntity.setGender(request.getGender());
			saEntity.setLastName(request.getLastName());
			saEntity.setName(request.getName());

			SchoolEntity sEntity = new SchoolEntity();
			sEntity.setName(request.getSchool().getName());
			sEntity.setYear(request.getSchool().getYear());
			saEntity.setSchool(sEntity);
			saEntity.setType(AccountType.PARENT);
			subAccountRepository.save(saEntity);
			
			if (request.getCredentialsCreationRequest().getPassword() != null) {
				UserEntity user = userRepository.findById(request.getCredentialsCreationRequest().getEmail()).orElseThrow(UserNotFoundException::new);
				user.setPassword(request.getCredentialsCreationRequest().getPassword());
				userRepository.save(user);
			}
							
		log.info("Updated sub account " + accountId);

	}

	@EventListener
	void create(final ParentUserCreationRequestEvent event) throws JsonProcessingException {
		log.info("New parent admin request: " + this.mapper.writeValueAsString(event));
		val request = event.getRequest();
		this.create(request);
	}

	public void deactivate(UUID subAccountId) {

		SubAccountEntity entity = subAccountRepository.findById(subAccountId).orElseThrow(AccountNotFoundException::new);
		CredentialsEntity credentials = credentialsRepo.findByAccountId(subAccountId).orElseThrow(CredentialsNotFoundException::new);
		UserEntity user = userRepository.findById(credentials.getEmail()).orElseThrow(UserNotFoundException::new);
		
		userRepository.delete(user);
		credentialsRepo.delete(credentials);
		subAccountRepository.delete(entity);

	}
	
	public PaginatedScanList<SubAccountEntity> findBy(final UUID accountId) {
		return this.subAccountRepository.findBy(accountId);
	}
}
