package br.com.netlit.accounts.domain.account.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "mobile",
    "credentials",
    "cnpj",
    "accounts",
    "billing"
})
public abstract class AccountCreationHttpRequest {

  @JsonProperty("accounts")
  private @Valid @NotNull @Size(min = 1) List<Account> accounts = new ArrayList<>();
  @JsonProperty("billing")
  private @Valid @NotNull Billing billing;
  @JsonProperty("credentials")
  private @Valid @NotNull Credentials credentials;
  @JsonProperty("mobile")
  private @NotEmpty String mobile;
  @JsonProperty("name")
  private @NotEmpty String name;
}
