package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullAccountCPFException extends RuntimeException {

  public NullAccountCPFException() {
    super("O CPF deve ser informado");
  }
}
