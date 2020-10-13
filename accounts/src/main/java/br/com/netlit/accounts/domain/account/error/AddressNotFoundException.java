package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressNotFoundException extends RuntimeException {

  public AddressNotFoundException() {
    super("Não foi encontrado o endereço.");
  }
}
