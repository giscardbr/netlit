package br.com.netlit.accounts.domain.account.city.service;

import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.infra.database.DynamoDBRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
public class SubParentParentUserHttpRequestWritingServiceTest extends DynamoDBRepositoryTest {

  @Autowired
  private CityUserWritingService writingService;

  @Test(expected = ConstraintViolationException.class)
  public void credentialsWithAccountIdShouldThrowError() {
    final CityUserCreationRequest creationRequest = CityUserCreationRequest.builder()
        .accountId(UUID.fromString("1fb39672-9dc6-4be6-b999-5d9a3be49cd5"))
        .name("Beatriz M Smith")
        .credentialsCreationRequest(CredentialsCreationRequest.builder()
            .email("velda.fishe1@yahoo.com")
            .password("Eseiy8wie")
            .accountId(UUID.fromString("1fb39672-9dc6-4be6-b999-5d9a3be49cd5"))
            .build())
        .build();
    this.writingService.create(creationRequest);
  }

  @Test
  public void shouldNotThrowError() {
    final CityUserCreationRequest creationRequest = CityUserCreationRequest.builder()
        .accountId(UUID.fromString("1fb39672-9dc6-4be6-b999-5d9a3be49cd5"))
        .name("Beatriz M Smith")
        .credentialsCreationRequest(CredentialsCreationRequest.builder()
            .email("velda.fishe1@yahoo.com")
            .password("Eseiy8wie")
            .build())
        .build();
    this.writingService.create(creationRequest);
  }
}