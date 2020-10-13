package br.com.netlit.customerservice.domain.resource.data;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolCityRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String email;

  @NotBlank
  private String function;

  @NotBlank
  private String telephone;

  @NotBlank
  private String state;

  @NotBlank
  private String city;

  @NotBlank
  private String need;

  @NotBlank
  private String school;
}
