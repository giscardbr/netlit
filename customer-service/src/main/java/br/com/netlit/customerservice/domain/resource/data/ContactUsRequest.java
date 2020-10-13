package br.com.netlit.customerservice.domain.resource.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactUsRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String telephone;

    private String school;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String subject;

    private String requestNumber;

    @NotBlank
    private String message;
}
