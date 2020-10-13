package br.com.netlit.accounts.domain.account.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "credentials"
})
public class Account {

  @JsonProperty("credentials")
  private @NotNull @Valid Credentials credentials;
  @JsonProperty("name")
  private @NotEmpty String name;
}
