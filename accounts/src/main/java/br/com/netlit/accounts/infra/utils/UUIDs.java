package br.com.netlit.accounts.infra.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UUIDs {

  private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

  private UUIDs() {
  }

  public static Optional<UUID> fromString(final String id) {
    try {
      return Optional.ofNullable(id)
          .filter(UUIDs::isValid)
          .map(UUID::fromString);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public static List<UUID> fromSetString(final Set<String> ids) {
    return Optional.ofNullable(ids)
        .map(Collection::stream)
        .orElseGet(Stream::empty)
        .map(UUIDs::fromString)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  public static boolean isValid(final String... uuid) {
    return Stream.of(uuid).map(UUID_PATTERN::matcher).allMatch(Matcher::matches);
  }

  public static boolean isValid(final Collection<String> uuid) {
    return isValid(uuid.toArray(new String[0]));
  }
}
