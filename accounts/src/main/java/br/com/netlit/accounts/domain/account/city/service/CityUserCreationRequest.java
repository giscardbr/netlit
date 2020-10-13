package br.com.netlit.accounts.domain.account.city.service;

import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.OnUserAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.mock.Account;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class CityUserCreationRequest {

  private final @NotEmpty String name;
  private final @Valid CredentialsCreationRequest credentialsCreationRequest;
  private final @Null(groups = OnAdminAccountCreation.class) @NotNull(groups = OnUserAccountCreation.class) UUID accountId;

  public static CityUserCreationRequest valueOf(final Account account) {
    final CredentialsCreationRequest credentialsCreationRequest = CredentialsCreationRequest.valueOf(account.getCredentials());
    return builder()
        .name(account.getName())
        .credentialsCreationRequest(credentialsCreationRequest)
        .build();
  }
}
