package br.com.netlit.accounts.domain.account.general.address;

import org.springframework.stereotype.Service;

import br.com.netlit.accounts.domain.account.general.repository.AddressRepository;

@Service
class AddressReadingService {

  private final AddressRepository addressRepo;

  AddressReadingService(final AddressRepository addressRepo) {
    this.addressRepo = addressRepo;
  }

//  boolean itsUnavailable(final String email) {
//    return this.addressRepo.exists(email);
//  }
}
