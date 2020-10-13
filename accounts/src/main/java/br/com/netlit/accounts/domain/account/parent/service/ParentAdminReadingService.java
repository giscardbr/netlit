package br.com.netlit.accounts.domain.account.parent.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.CNPJAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.CPFAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountCNPJException;
import br.com.netlit.accounts.domain.account.error.NullAccountCPFException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.error.UserNotFoundException;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressType;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.AddressRepository;
import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import br.com.netlit.accounts.domain.account.mock.Credentials;
import br.com.netlit.accounts.domain.account.parent.resource.ParentUserHttpRequest;
import br.com.netlit.accounts.domain.account.parent.resource.School;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class ParentAdminReadingService {

	private final AccountRepository accountRepo;
	private final AddressRepository addressRepo;
	private final CredentialsRepository credentialsRepo;
	private final SubAccountRepository subAccountRepo;
	private final UserRepository userRepo;
	private final CredentialsService credentialsService;
	private final ObjectMapper mapper;

	public ParentAdminReadingService(
			final AccountRepository accountRepo, 
			final AddressRepository addressRepo,
			final CredentialsRepository credentialsRepo, 
			final SubAccountRepository subAccountRepo,
			final UserRepository userRepo,
			final CredentialsService credentialsService,
			final ObjectMapper mapper) {

		this.accountRepo = accountRepo;
		this.addressRepo = addressRepo;
		this.credentialsRepo = credentialsRepo;
		this.subAccountRepo = subAccountRepo;
		this.userRepo = userRepo;
		this.credentialsService = credentialsService;
		this.mapper = mapper;
	}

	public Resource<ParentAdminCreationRequest> byId(final String id) {

		UUID accountId = UUID.fromString(id);
		Optional<AccountEntity> account = accountRepo.findById(accountId);

		PaginatedScanList<SubAccountEntity> subAccountsList = this.subAccountRepo.findBy(accountId);

		List<ParentUserCreationRequest> childrenAccounts = subAccountsList.stream()
				.map(sa -> ParentUserCreationRequest.valueOf(ParentUserHttpRequest
						.builder()
						.id(sa.getId().toString())
						.birthDate(sa.getBirthDate())
						.gender(sa.getGender())
						.lastName(sa.getLastName())
						.name(sa.getName())
						.created(sa.getCreated())
						.type(sa.getType())
						.school(School
								.builder()
								.name(sa.getSchool()
								.getName())
								.year(sa.getSchool()
								.getYear())
								.build())
						.credentials(Credentials
								.builder()
								.email(credentialsRepo.findByAccountId(sa.getId()).get().getEmail())
								.build())
						.build()))
				.sorted(new Comparator<ParentUserCreationRequest>() {
					public int compare(ParentUserCreationRequest o1, ParentUserCreationRequest o2) {
						return o1.getCreated() == null || o2.getCreated() == null ? -1 : o1.getCreated().compareTo(o2.getCreated());
					};
				})
				.collect(Collectors.toList());

		Optional<AddressEntity> address = addressRepo.findBy(accountId, AddressType.BILLING);
		Optional<CredentialsEntity> credential = credentialsRepo.findByAccountId(accountId);
//	  final AccountEntity book = UUIDs.fromString(id).map(UUID::toString).flatMap(this.accountRepo::findById).orElseThrow(EntityNotFoundException::new);

		ParentAdminCreationRequest res = 
				ParentAdminCreationRequest.valueOf(
						credential.get(),
						(address.isPresent() ? address.get() : null), 
						account.get(), 
						childrenAccounts);
		log.info("Query Sucessful!");

		return new Resource<ParentAdminCreationRequest>(res, new ArrayList<Link>());
	}

	public UserDetail findUserDetailByEmail(String email) {
		
		UserEntity userEntity = this.userRepo.findById(email).orElseThrow(UserNotFoundException::new);
		
		
		switch (userEntity.getRole()) {
		case USER:
			
			Optional<SubAccountEntity> subAccount = this.subAccountRepo.findById(userEntity.getAccountId());

			if (subAccount.isPresent()) {
				return UserDetail
						.builder()
						.birthDate(subAccount.get().getBirthDate())
						.gender(subAccount.get().getGender())
						.lastName(subAccount.get().getLastName())
						.name(subAccount.get().getName())
						.isBusiness(false)
						.accountId(subAccount.get().getAccountId().toString())
						.role(userEntity.getRole())
						.build();
			}
					
		case ADMIN:
		default:

			Optional<AccountEntity> account = this.accountRepo.findById(userEntity.getAccountId());
			
			return UserDetail
					.builder()
					.birthDate(account.get().getBirthDate())
					.gender(account.get().getGender())
					.lastName(account.get().getLastName())
					.name(account.get().getName())
					.isBusiness(account.get().isBusiness())
					.entityName(account.get().getEntityName())
					.tradingName(account.get().getTradingName())
					.accountId(account.get().getId().toString())
					.role(userEntity.getRole())
					.build();

		}

	}
	
	public boolean checkEmail(final String email) {

		if (this.credentialsService.itsUnavailable(Optional.of(email).orElseThrow(NullAccountEmailException::new)))
			throw new EmailAlreadyUsedException(email);

		return true;

	}

	public Boolean checkDocument(String document, Boolean isBusiness) {
		
		
	    if (isBusiness) {
	    	
	    	if (this.accountRepo.exists(isBusiness, Optional.of(document).orElseThrow(NullAccountCNPJException::new))) throw new CNPJAlreadyUsedException(document);

	    } else {

	    	if (this.accountRepo.exists(isBusiness, Optional.of(document).orElseThrow(NullAccountCPFException::new))) throw new CPFAlreadyUsedException(document);

	    }

		return true;
	}

}
