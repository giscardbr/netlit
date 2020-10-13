package br.com.netlit.oauth2.domain.service;

import br.com.netlit.oauth2.domain.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Value
class User implements UserDetails {

  private final UUID id;
  @JsonIgnore
  private final String password;
  private final String username;
  private final List<GrantedAuthority> authorities;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  public User(final UserEntity entity) {
    this.id = entity.getAccountId();
    this.password = "{noop}" + entity.getPassword();
    this.username = entity.getEmail();
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority(entity.getRole().name()));
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
    this.enabled = entity.isEnabled();
  }
}
