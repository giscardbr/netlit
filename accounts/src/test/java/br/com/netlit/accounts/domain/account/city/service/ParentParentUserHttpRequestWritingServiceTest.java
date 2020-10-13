//package br.com.netlit.accounts.domain.account.city.service;
//
//import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
//import br.com.netlit.accounts.infra.database.DynamoDBRepositoryTest;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.validation.ConstraintViolationException;
//import java.util.UUID;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ActiveProfiles(profiles = "test")
//public class ParentParentUserHttpRequestWritingServiceTest extends DynamoDBRepositoryTest {
//
//  @Autowired
//  private CityAdminWritingService writingService;
//
//  @Test
//  public void shouldAllowEmptySubAccounts() {
//    final CityAdminCreationRequest creationRequest =
//        CityAdminCreationRequest.builder()
//            .name("Limeira/SP")
//            .mobile("19 99978-7898")
//            .cnpj("32.289.225/0001-04")
//            .credentials(CredentialsCreationRequest.builder()
//                .email("salina.steuber@hotmail.com")
//                .password("ZhCH8vrD")
//                .build())
//            .build();
//    this.writingService.create(creationRequest);
//  }
//
//  @Test(expected = ConstraintViolationException.class)
//  public void subAccountCredentialsWithAccountIdShouldThrowError() {
//    final CityAdminCreationRequest creationRequest =
//        CityAdminCreationRequest.builder()
//            .name("Limeira/SP")
//            .mobile("19 99978-7898")
//            .cnpj("32.289.225/0001-04")
//            .account(CityUserCreationRequest.builder()
//                .accountId(UUID.fromString("1fb39672-9dc6-4be6-b999-5d9a3be49cd5"))
//                .name("Beatriz M Smith")
//                .credentialsCreationRequest(CredentialsCreationRequest.builder()
//                    .email("velda.fishe1@yahoo.com")
//                    .password("Eseiy8wie")
//                    .build())
//                .build())
//            .credentials(CredentialsCreationRequest.builder()
//                .email("salina.steuber@hotmail.com")
//                .password("ZhCH8vrD")
//                .build())
//            .build();
//    this.writingService.create(creationRequest);
//  }
//
//  @Test(expected = ConstraintViolationException.class)
//  public void subAccountsWithAccountIdShouldThrowError() {
//    final CityAdminCreationRequest creationRequest =
//        CityAdminCreationRequest.builder()
//            .name("Limeira/SP")
//            .mobile("19 99978-7898")
//            .cnpj("32.289.225/0001-04")
//            .account(CityUserCreationRequest.builder()
//                .accountId(UUID.fromString("1fb39672-9dc6-4be6-b999-5d9a3be49cd5"))
//                .name("Beatriz M Smith")
//                .credentialsCreationRequest(CredentialsCreationRequest.builder()
//                    .email("velda.fishe1@yahoo.com")
//                    .password("Eseiy8wie")
//                    .build())
//                .build())
//            .credentials(CredentialsCreationRequest.builder()
//                .email("salina.steuber@hotmail.com")
//                .password("ZhCH8vrD")
//                .build())
//            .build();
//    this.writingService.create(creationRequest);
//  }
//}