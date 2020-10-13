package br.com.netlit.library.infra.database;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {
  @Override
  public String convert(final LocalDate date) {
    return date.format(DateTimeFormatter.ISO_DATE);
  }

  @Override
  public LocalDate unconvert(final String date) {
    return LocalDate.parse(date);
  }
}
