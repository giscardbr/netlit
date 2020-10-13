package br.com.netlit.oauth2.domain.event;

public class NullUserException extends RuntimeException {

  public NullUserException() {
    super("The user should not be null");
  }
}
