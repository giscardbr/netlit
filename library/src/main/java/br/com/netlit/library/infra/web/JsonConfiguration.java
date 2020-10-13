package br.com.netlit.library.infra.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

@Configuration
public class JsonConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
    return builder -> {
      builder.propertyNamingStrategy(SNAKE_CASE);
      builder.serializationInclusion(JsonInclude.Include.NON_NULL);
      builder.featuresToDisable(FAIL_ON_UNKNOWN_PROPERTIES);
    };
  }
}
