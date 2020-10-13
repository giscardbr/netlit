package br.com.netlit.accounts.domain.event;

import lombok.NonNull;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Value
public class CredentialsUpdatedEvent extends ApplicationEvent {

  private final UUID accountId;
  private final String username;
  private final String password;

  public CredentialsUpdatedEvent(@NonNull final Object source,
                                 @NonNull final UUID accountId,
                                 @NonNull final String username,
                                 @NonNull final String password) {
    super(source);
    this.accountId = accountId;
    this.username = username;
    this.password = password;
  }
}
