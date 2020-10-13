package br.com.netlit.library;

import br.com.netlit.library.infra.database.DynamoDBTables;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

//@Component
@Profile("dev")
public class DatabaseInitializer implements ApplicationRunner {

  private final DynamoDBTables dbTables;

  public DatabaseInitializer(final DynamoDBTables dbTables) {
    this.dbTables = dbTables;
  }

  @Override
  public void run(final ApplicationArguments args) {
    this.scanEntityClasses().forEach(this.dbTables::createTableForEntity);
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
