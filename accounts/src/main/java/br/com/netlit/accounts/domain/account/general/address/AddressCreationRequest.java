package br.com.netlit.accounts.domain.account.general.address;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netlit.accounts.domain.account.general.entity.AddressType;
import br.com.netlit.accounts.domain.account.mock.Address;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AddressCreationRequest {

	private String additionalInformation;
	private String city;
	private String district;
	private Long number;
	private String phone;
	private String state;
	private String street;
	private String zip;
	private String id;
	
	@JsonProperty("city_code")
	private String cityCode;

	private AddressType type;
	
	public static AddressCreationRequest valueOf(final @NonNull Address address) {
	    return builder()
		.city(address.getCity())
		.district(address.getDistrict())
		.number(address.getNumber())
		.phone(address.getPhone())
		.state(address.getState())
		.street(address.getStreet())
		.zip(address.getZip())
		.id(address.getId())
		.cityCode(address.getCityCode())
		.build();
  }
}
