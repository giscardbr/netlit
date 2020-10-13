package br.com.netlit.accounts.domain.account.parent.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountCredentialsException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.OnUserAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.SchoolEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.event.ParentUserCreationRequestEvent;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class ParentUserWritingService {

  private final CredentialsService credentialsService;
  private final SubAccountRepository subAccountRepo;
  private final ObjectMapper mapper;

  public ParentUserWritingService(
      final SubAccountRepository subAccountRepo,
      final CredentialsService credentialsService,
      final ObjectMapper mapper) {

    this.subAccountRepo = subAccountRepo;
    this.credentialsService = credentialsService;
    this.mapper = mapper;
  }

  @Validated(OnUserAccountCreation.class)
  public void create(final @Valid @NotNull ParentUserCreationRequest accountRequest) {
    log.info("Creating son account");
    val credentials = Optional.of(accountRequest).map(ParentUserCreationRequest::getCredentialsCreationRequest);
    val email = credentials
        .map(CredentialsCreationRequest::getEmail)
        .orElseThrow(NullAccountEmailException::new);
    if (this.credentialsService.itsUnavailable(email)) throw new EmailAlreadyUsedException(email);

    val school = accountRequest.getSchool();
    val schoolEntity = new SchoolEntity();
    schoolEntity.setName(school.getName());
    schoolEntity.setYear(school.getYear());

    val entity = new SubAccountEntity();
    entity.setType(AccountType.PARENT);
    entity.setName(accountRequest.getName());
    entity.setLastName(accountRequest.getLastName());
    entity.setBirthDate(accountRequest.getBirthDate());
    entity.setGender(accountRequest.getGender());
    entity.setSchool(schoolEntity);
    entity.setAccountId(accountRequest.getAccountId());
    entity.setCreated(LocalDateTime.now());
    this.subAccountRepo.save(entity);

    val credentialsRequest = credentials
        .orElseThrow(NullAccountCredentialsException::new)
        .toBuilder()
        .accountId(entity.getId())
        .build();
    this.credentialsService.create(credentialsRequest);

    val accountId = entity.getId();
    log.info("Created son account " + accountId);
  }

  @EventListener
  void create(final ParentUserCreationRequestEvent event) throws JsonProcessingException {
    log.info("New parent user request: " + this.mapper.writeValueAsString(event));
    final ParentUserCreationRequest request = event.getRequest();
    this.create(request);
  }
}

