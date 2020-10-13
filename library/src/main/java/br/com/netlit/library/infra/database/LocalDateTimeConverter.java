package br.com.netlit.library.infra.database;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

  @Override
  public String convert(final LocalDateTime dateTime) {
    return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  @Override
  public LocalDateTime unconvert(final String dateTime) {
    return LocalDateTime.parse(dateTime);
  }
}
