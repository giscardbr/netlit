package br.com.netlit.oauth2.domain.event;

import lombok.Data;

import java.util.UUID;

@Data
public class CredentialsUpdatedEvent {

  private final UUID accountId;
  private final String username;
  private final String password;
}
