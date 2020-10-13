package br.com.netlit.accounts.domain.account.city.service;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountCredentialsException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.OnUserAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.event.CityUserCreationRequestEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j2
@Service
@Validated
public class CityUserWritingService {

  private final SubAccountRepository subAccountRepo;
  private final CredentialsService credentialsService;
  private final ObjectMapper mapper;

  public CityUserWritingService(
      final SubAccountRepository subAccountRepo,
      final CredentialsService credentialsService,
      final ObjectMapper mapper) {

    this.subAccountRepo = subAccountRepo;
    this.credentialsService = credentialsService;
    this.mapper = mapper;
  }

  @Validated(OnUserAccountCreation.class)
  public void create(final @Valid @NotNull CityUserCreationRequest accountRequest) {
    log.info("Creating sub city account");
    val credentials = Optional.of(accountRequest).map(CityUserCreationRequest::getCredentialsCreationRequest);
    val email = credentials
        .map(CredentialsCreationRequest::getEmail)
        .orElseThrow(NullAccountEmailException::new);
    if (this.credentialsService.itsUnavailable(email)) throw new EmailAlreadyUsedException(email);

    val entity = new SubAccountEntity();
    entity.setType(AccountType.CITY);
    entity.setName(accountRequest.getName());
    entity.setAccountId(accountRequest.getAccountId());
    this.subAccountRepo.save(entity);

    val credentialsRequest = credentials
        .orElseThrow(NullAccountCredentialsException::new)
        .toBuilder()
        .accountId(entity.getId())
        .build();
    this.credentialsService.create(credentialsRequest);

    val accountId = entity.getId();
    log.info("Created sub city account " + accountId);
  }

  @EventListener
  void create(final CityUserCreationRequestEvent event) throws JsonProcessingException {
    log.info("New city user request: " + this.mapper.writeValueAsString(event));
    final CityUserCreationRequest request = event.getRequest();
    this.create(request);
  }
}

