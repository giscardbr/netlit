package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CPFAlreadyUsedException extends RuntimeException {

  public CPFAlreadyUsedException(final String document) {
    super("O CPF " + document + " já está em uso");
  }
}
