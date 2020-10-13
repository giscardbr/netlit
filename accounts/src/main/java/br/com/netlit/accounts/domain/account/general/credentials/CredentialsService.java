package br.com.netlit.accounts.domain.account.general.credentials;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
@Validated
public class CredentialsService {

  private final CredentialsReadingService readingService;
  private final CredentialsWritingService writingService;

  public CredentialsService(final CredentialsReadingService readingService, final CredentialsWritingService writingService) {
    this.readingService = readingService;
    this.writingService = writingService;
  }

  public boolean itsUnavailable(final String email) {
    return this.readingService.itsUnavailable(email);
  }

  @Validated(OnCredentialsCreation.class)
  public void create(final @Valid @NotNull CredentialsCreationRequest credentialsRequest) {
    this.writingService.create(credentialsRequest);
  }
}
