package br.com.netlit.oauth2.database;

import br.com.netlit.oauth2.AuthorizationServerApplication;
import br.com.netlit.oauth2.infra.database.DynamoDBTables;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthorizationServerApplication.class, DynamoDBEmbeddedConfig.class})
public abstract class DynamoDBRepositoryTest {

  @Autowired
  private DynamoDBTables dbTables;

  @Before
  public void createTables() {
    this.scanEntityClasses().forEach(this.dbTables::createTableForEntity);
  }

  @After
  public void deleteTables() {
    this.scanEntityClasses().forEach(this.dbTables::deleteTableForEntity);
  }

  @SneakyThrows(ClassNotFoundException.class)
  private Class<?> getBeanClass(final BeanDefinition beanDefinition) {
    final String className = beanDefinition.getBeanClassName();
    return Class.forName(className);
  }

  @SneakyThrows
  private Stream<Class<?>> scanEntityClasses() {
    final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(DynamoDBTable.class));
    return Optional.of("br.com.netlit")
        .map(scanner::findCandidateComponents)
        .map(Collection::stream)
        .orElseGet(Stream::empty)
        .map(this::getBeanClass);
  }
}
