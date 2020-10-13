package br.com.netlit.accounts.domain.account.mock.library;

import br.com.netlit.accounts.domain.account.mock.AccountCreationHttpRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotEmpty;

@Data
public class LibraryAccountCreationHttpRequest extends AccountCreationHttpRequest {

  @JsonProperty("cnpj")
  private @CNPJ @NotEmpty String cnpj;
}
