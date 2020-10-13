package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends RuntimeException {

  public EmailAlreadyUsedException(final String email) {
    super("O endereço de e-mail " + email + " já está em uso");
  }
}
