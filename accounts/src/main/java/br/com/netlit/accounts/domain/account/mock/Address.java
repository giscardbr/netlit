package br.com.netlit.accounts.domain.account.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "street",
    "number",
    "city",
    "state",
    "zip",
    "district",
    "additional_information",
    "phone",
    "id",
    "city_code"
})
public class Address {

  @JsonProperty("additional_information")
  private String additionalInformation;
  @JsonProperty("city")
  private @NotEmpty String city;
  @JsonProperty("district")
  private @NotEmpty String district;
  @JsonProperty("number")
  private @NotNull Long number;
  @JsonProperty("phone")
  private @NotEmpty String phone;
  @JsonProperty("state")
  private @NotEmpty String state;
  @JsonProperty("street")
  private @NotEmpty String street;
  @JsonProperty("zip")
  private @NotEmpty String zip;
  @JsonProperty("id")
  private String id;
  @JsonProperty("city_code")
  private String cityCode;
}
