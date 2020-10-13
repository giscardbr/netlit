package br.com.netlit.accounts.domain.account.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SchoolGradeRoomAlreadyExistsException extends RuntimeException {

  public SchoolGradeRoomAlreadyExistsException() {
    super("O código já existe.");
  }
}
