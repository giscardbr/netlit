//package br.com.netlit.accounts.domain.account.city.service;
//
//import br.com.netlit.accounts.domain.account.city.resource.CityAdminCreationHttpRequest;
//import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
//import lombok.Builder;
//import lombok.Singular;
//import lombok.Value;
//import lombok.val;
//import org.hibernate.validator.constraints.br.CNPJ;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Value
//@Builder
//public class CityAdminCreationRequest {
//
//  private final @NotEmpty String name;
//  private final @NotEmpty String mobile;
//  private final @CNPJ String cnpj;
//  private final @NotNull @Valid CredentialsCreationRequest credentials;
//  private final @NotNull @Valid BillingCreationRequest billing;
//  @Singular
//  private final List<@NotNull @Valid CityUserCreationRequest> accounts;
//
//  public static CityAdminCreationRequest valueOf(final CityAdminCreationHttpRequest httpRequest) {
//    val credentials = CredentialsCreationRequest.valueOf(httpRequest.getCredentials());
//    val accounts = httpRequest.getAccounts().stream()
//        .map(CityUserCreationRequest::valueOf)
//        .collect(Collectors.toList());
//    val billing = BillingCreationRequest.valueOf(httpRequest.getBilling());
//    return builder()
//        .name(httpRequest.getName())
//        .mobile(httpRequest.getMobile())
//        .cnpj(httpRequest.getCnpj())
//        .credentials(credentials)
//        .billing(billing)
//        .accounts(accounts)
//        .build();
//  }
//}
