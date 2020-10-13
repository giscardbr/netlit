package br.com.netlit.accounts.domain.account.parent.service;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.domain.account.general.entity.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDetail {

  private @Past @NotNull LocalDate birthDate;
  private @NotNull Gender gender;
  private String lastName;
  private String name;
  private String entityName;
  private String tradingName;
  private boolean isBusiness;
  private String accountId;
  private Role role;

}
