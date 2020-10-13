package br.com.netlit.accounts.domain.account.general.credentials;

import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import org.springframework.stereotype.Service;

@Service
class CredentialsReadingService {

  private final CredentialsRepository credentialsRepo;

  CredentialsReadingService(final CredentialsRepository credentialsRepo) {
    this.credentialsRepo = credentialsRepo;
  }

  boolean itsUnavailable(final String email) {
    return this.credentialsRepo.exists(email);
  }
}
