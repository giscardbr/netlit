package br.com.netlit.accounts.domain.account.general.credentials;

import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.OnUserAccountCreation;
import br.com.netlit.accounts.domain.account.mock.Credentials;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class CredentialsCreationRequest {

  private final String email;
  private final @NotEmpty String password;
  private final @Null(groups = {OnAdminAccountCreation.class, OnUserAccountCreation.class}) @NotNull(groups = OnCredentialsCreation.class) UUID accountId;

  public static CredentialsCreationRequest valueOf(final @NonNull Credentials credentials) {
    return builder()
        .email(credentials.getEmail())
        .password(credentials.getPassword())
        .build();
  }
}
