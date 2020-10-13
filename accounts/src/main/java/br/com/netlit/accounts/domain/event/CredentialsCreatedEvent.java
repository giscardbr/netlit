package br.com.netlit.accounts.domain.event;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.context.ApplicationEvent;

import br.com.netlit.accounts.domain.account.general.entity.AccountRole;
import lombok.NonNull;
import lombok.Value;

@Value
public class CredentialsCreatedEvent extends ApplicationEvent {

  private final UUID accountId;
  private final String email;
  private final String password;
  private final AccountRole role;

  public CredentialsCreatedEvent(@NonNull final Object source,
                                 @NonNull final UUID accountId,
                                 @NonNull final String email,
                                 @NonNull @NotEmpty(message = "{password.notEmpty}") final String password,
                                 @NonNull final AccountRole role) {
    super(source);
    this.accountId = accountId;
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
