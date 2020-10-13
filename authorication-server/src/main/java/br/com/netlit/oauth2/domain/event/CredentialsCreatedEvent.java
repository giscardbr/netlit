package br.com.netlit.oauth2.domain.event;

import br.com.netlit.oauth2.domain.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class CredentialsCreatedEvent {

  private UUID accountId;
  private String email;
  private String password;
  private Role role;
}
