package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullAccountCredentialsException extends RuntimeException {

  public NullAccountCredentialsException() {
    super("The account credentials should not be null");
  }
}
