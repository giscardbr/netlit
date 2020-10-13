package br.com.netlit.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class LoggingLayout extends JsonLayout {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  private static final String TIMESTAMP = "timestamp";

  @Override
  protected void addCustomDataToJsonMap(final Map<String, Object> map, final ILoggingEvent event) {
    Optional.of(map)
        .map(stringObjectMap -> map.get(TIMESTAMP))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(Long::valueOf)
        .map(Instant::ofEpochMilli)
        .map(instant -> LocalDateTime.ofInstant(instant, ZoneOffset.UTC))
        .map(FORMATTER::format)
        .ifPresent(dateTime -> map.put(TIMESTAMP, dateTime));
  }
}