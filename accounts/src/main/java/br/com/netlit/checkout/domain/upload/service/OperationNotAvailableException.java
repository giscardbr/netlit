package br.com.netlit.checkout.domain.upload.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperationNotAvailableException extends RuntimeException {

  public OperationNotAvailableException(final String message) {
    super(message);
  }
}
