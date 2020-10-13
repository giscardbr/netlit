package br.com.netlit.accounts.domain.account.general.credentials;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountException;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AccountRole;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.event.CredentialsCreatedEvent;
import lombok.val;

@Service
public class CredentialsWritingService {

  private final ApplicationEventPublisher publisher;
  private final AccountRepository accountRepo;
  private final SubAccountRepository subAccountRepo;
  private final CredentialsRepository credentialsRepo;
  private final CredentialsReadingService readingService;

  CredentialsWritingService(
      final ApplicationEventPublisher publisher,
      final AccountRepository accountRepo,
      final SubAccountRepository subAccountRepo,
      final CredentialsRepository credentialsRepo,
      final CredentialsReadingService readingService) {

    this.publisher = publisher;
    this.accountRepo = accountRepo;
    this.subAccountRepo = subAccountRepo;
    this.credentialsRepo = credentialsRepo;
    this.readingService = readingService;
  }

  public void create(final CredentialsCreationRequest credentialsRequest) {
    val email = credentialsRequest.getEmail();
    if (this.readingService.itsUnavailable(email)) throw new EmailAlreadyUsedException(email);

    val entity = new CredentialsEntity();
    val accountId = credentialsRequest.getAccountId();
    val subAccount = this.subAccountRepo.findById(accountId);
    subAccount.map(SubAccountEntity::getType).ifPresent(accountType -> {
      entity.setAccountRole(AccountRole.USER);
      entity.setAccountType(accountType);
    });
    if (!subAccount.isPresent()) {
      val accountType = this.accountRepo.findById(accountId)
          .map(AccountEntity::getType)
          .orElseThrow(NullAccountException::new);
      entity.setAccountRole(AccountRole.ADMIN);
      entity.setAccountType(accountType);
    }

    entity.setAccountId(accountId);
    entity.setEmail(email);
    this.credentialsRepo.save(entity);
    this.publisher.publishEvent(
        new CredentialsCreatedEvent(this,
            entity.getAccountId(),
            entity.getEmail(),
            credentialsRequest.getPassword(),
            entity.getAccountRole()));
  }

  public void delete(final CredentialsEntity entity) {
	  credentialsRepo.delete(entity);
  }
}

