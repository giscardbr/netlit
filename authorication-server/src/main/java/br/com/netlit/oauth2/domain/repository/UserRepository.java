package br.com.netlit.oauth2.domain.repository;

import br.com.netlit.oauth2.domain.entity.UserEntity;
import br.com.netlit.oauth2.infra.database.DynamoDBRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends DynamoDBRepository<UserEntity, String> {

  public UserRepository(final DynamoDBMapper mapper) {
    super(mapper, UserEntity.class);
  }
}
