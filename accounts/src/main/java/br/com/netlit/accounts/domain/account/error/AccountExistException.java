package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountExistException extends RuntimeException {

  public AccountExistException(final String email) {
    super("Já existe uma conta associado ao email " + email + "");
  }
}
