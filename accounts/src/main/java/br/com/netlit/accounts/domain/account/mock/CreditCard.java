package br.com.netlit.accounts.domain.account.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "number",
    "name",
    "cvv",
    "exp"
})
public class CreditCard {

  @JsonProperty("cvv")
  private Long cvv;
  @JsonProperty("exp")
  private String exp;
  @JsonProperty("name")
  private String name;
  @JsonProperty("number")
  private String number;
}
