package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CredentialsNotFoundException extends RuntimeException {

  public CredentialsNotFoundException() {
    super("Não foi encontrada a credencial.");
  }
}
