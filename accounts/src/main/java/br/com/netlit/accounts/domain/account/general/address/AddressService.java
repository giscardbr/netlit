package br.com.netlit.accounts.domain.account.general.address;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddressService {

  private final AddressReadingService readingService;
  private final AddressWritingService writingService;

  public AddressService(final AddressReadingService readingService, final AddressWritingService writingService) {
    this.readingService = readingService;
    this.writingService = writingService;
  }

  public void create(final @Valid @NotNull AddressCreationRequest credentialsRequest) {
    this.writingService.create(credentialsRequest);
  }
}
