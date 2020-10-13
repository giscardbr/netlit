package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CNPJAlreadyUsedException extends RuntimeException {

  public CNPJAlreadyUsedException(final String document) {
    super("O CNPJ " + document + " já está em uso");
  }
}
