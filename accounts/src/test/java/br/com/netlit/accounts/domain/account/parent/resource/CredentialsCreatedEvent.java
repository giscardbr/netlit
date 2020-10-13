package br.com.netlit.accounts.domain.account.parent.resource;

import java.util.UUID;

import br.com.netlit.accounts.domain.account.general.entity.AccountRole;

public class CredentialsCreatedEvent {

  private final UUID accountId;
  private final String email;
  private final String password;
  private final AccountRole role;

  public CredentialsCreatedEvent(
                                 final UUID accountId,
                                 final String email,
                                 final String password,
                                 final AccountRole role) {
    this.accountId = accountId;
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
