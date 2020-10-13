package br.com.netlit.accounts.domain.password;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordResetHttpRequest {
  private @NotEmpty String username;
}
