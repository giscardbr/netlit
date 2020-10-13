package br.com.netlit.oauth2.domain.repository;

import br.com.netlit.oauth2.database.DynamoDBRepositoryTest;
import br.com.netlit.oauth2.domain.entity.Role;
import br.com.netlit.oauth2.domain.entity.UserEntity;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@ActiveProfiles("test")
public class UserRepositoryTest extends DynamoDBRepositoryTest {

  @Autowired
  private UserRepository repository;

  @Test
  public void a() {
    final String email = "eugene_anders@gmail.com";

    final UserEntity userEntity = new UserEntity();
    userEntity.setEmail(email);
    userEntity.setPassword("a08cd3b813e84cfd66fc9e328ddcbd31");
    userEntity.setAccountId(UUID.randomUUID());
    userEntity.setRole(Role.ADMIN);
    this.repository.save(userEntity);

    final UserEntity entity = this.repository.findById(email).get();
    assertThat(entity).isNotNull();
  }
}