package br.com.netlit.accounts.domain.account.error;

public class NullAccountException extends RuntimeException {

  public NullAccountException() {
    super("The account should not be null");
  }
}
