package br.com.netlit.accounts.domain.account.general.address;

import org.springframework.stereotype.Service;

import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.repository.AddressRepository;
import lombok.val;

@Service
class AddressWritingService {

	private final AddressRepository repo;

	AddressWritingService(final AddressRepository repo) {

		this.repo = repo;
	}

	void create(final AddressCreationRequest addressRequest) {

		val entity = new AddressEntity();
		entity.setAdditionalInformation(addressRequest.getAdditionalInformation());
		entity.setCity(addressRequest.getCity());
		entity.setDistrict(addressRequest.getDistrict());
		entity.setNumber(addressRequest.getNumber());
		entity.setPhone(addressRequest.getPhone());
		entity.setState(addressRequest.getState());
		entity.setStreet(addressRequest.getStreet());
		entity.setZip(addressRequest.getZip());
		entity.setType(addressRequest.getType());

		this.repo.save(entity);
	}
}
