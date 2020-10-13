package br.com.netlit.accounts.domain.account.parent.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Past;

import br.com.netlit.accounts.domain.account.city.service.BillingCreationRequest;
import br.com.netlit.accounts.domain.account.general.address.AddressCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.domain.account.mock.Address;
import br.com.netlit.accounts.domain.account.parent.resource.ParentAdminCreationHttpRequest;
import br.com.netlit.accounts.infra.utils.validation.CpfCnpj;
import br.com.netlit.accounts.infra.utils.validation.CpfCnpjValidator;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.val;

@Value
@Builder
public class AccountRequest {

  private @Past LocalDate birthDate;
  @Singular
  private final List<ParentUserCreationRequest> childrenAccounts;
  private @CpfCnpj(message = "{cpf.invalid}") String cpf;
  private final CredentialsCreationRequest credentials;
  private final AddressCreationRequest address;
  private final BillingCreationRequest billing;
  private Gender gender;
  private String lastName;
  private String mobile;
  private String name;
  private String rg;
  private Long readers;
  private String entityNumber;
  private String entityName;
  private String tradingName;
  private @CpfCnpj(message = "{cnpj.invalid}") String cnpj;
  private boolean isBusiness;
  private String email;
  
  public static AccountRequest valueOf(final ParentAdminCreationHttpRequest httpRequest) {
	val address = AddressCreationRequest.valueOf(httpRequest.getBilling().getAddress());
    val billing = BillingCreationRequest.valueOf(httpRequest.getBilling());
    val childrenAccounts = Optional.ofNullable(httpRequest.getAccounts())
        .map(Collection::stream)
        .orElseGet(Stream::empty)
        .map(ParentUserCreationRequest::valueOf)
        .collect(Collectors.toList());
    return builder()
        .name(httpRequest.getName())
        .lastName(httpRequest.getLastName())
        .mobile(httpRequest.getMobile())
        .rg(httpRequest.getRg())
        .readers(httpRequest.getReaders())
        .cpf(CpfCnpjValidator.format(httpRequest.getCpf()))
        .birthDate(httpRequest.getBirthDate())
        .gender(httpRequest.getGender())
        .childrenAccounts(childrenAccounts)
        .address(address)
        .entityName(httpRequest.getEntityName())
        .entityNumber(httpRequest.getEntityNumber())
        .cnpj(httpRequest.getCnpj() != null ? CpfCnpjValidator.format(httpRequest.getCnpj()) : null)
        .tradingName(httpRequest.getTradingName())
        .isBusiness(httpRequest.isBusiness())
        .billing(billing)
        .email(httpRequest.getEmail())
        .build();
  }

  public static AccountRequest valueOf(final CredentialsEntity credentials, final AddressEntity address, final AccountEntity account, final List<ParentUserCreationRequest> childrenAccounts) {
	  
	  AddressCreationRequest addressReq = AddressCreationRequest.valueOf(new Address());
	  if (address != null) {
		addressReq = AddressCreationRequest.builder()
				.additionalInformation(address.getAdditionalInformation())
				.city(address.getCity())
				.district(address.getDistrict())
				.number(address.getNumber())
				.phone(address.getPhone())
				.state(address.getState())
				.street(address.getStreet())
				.type(address.getType())
				.zip(address.getZip())
				.id(address.getId().toString())
			.build();
		}
	return builder()
        .name(account.getName())
        .lastName(account.getLastName())
        .mobile(account.getMobile())
        .rg(account.getRg())
        .cpf(CpfCnpjValidator.format(account.getCpf()))
        .birthDate(account.getBirthDate())
        .gender(account.getGender())
        .address(addressReq)
        .readers(account.getReaders())
        .entityName(account.getEntityName())
        .entityNumber(account.getEntityNumber())
        .cnpj(account.getCnpj() != null ? CpfCnpjValidator.format(account.getCnpj()) : null)
        .tradingName(account.getTradingName())
        .childrenAccounts(childrenAccounts)
        .isBusiness(account.isBusiness())
        .credentials(CredentialsCreationRequest.builder()
        		.email(credentials.getEmail())
        		.accountId(credentials.getAccountId())
        		.build())
        .build();
  }
  
}
