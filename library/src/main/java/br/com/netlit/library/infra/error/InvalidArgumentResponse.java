package br.com.netlit.library.infra.error;

import lombok.Data;

import java.util.List;

@Data
public class InvalidArgumentResponse {

  private List<String> errors;

  public InvalidArgumentResponse(final List<String> errors) {
    this.errors = errors;
  }
}
