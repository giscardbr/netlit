package br.com.netlit.library.domain.event;

import br.com.netlit.library.infra.utils.UUIDs;
import lombok.NonNull;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Value
public class PageReadingEvent extends ApplicationEvent {

  private final UUID accountId;
  private final UUID pageId;
  private final UUID bookId;

  public PageReadingEvent(@NonNull final Object source, @NonNull final String accountId, @NonNull final String pageId, @NonNull final String bookId) {
    super(source);
    this.accountId = UUIDs.fromString(accountId).orElseThrow(IllegalArgumentException::new);
    this.pageId = UUIDs.fromString(pageId).orElseThrow(IllegalArgumentException::new);
    this.bookId = UUIDs.fromString(bookId).orElseThrow(IllegalArgumentException::new);
  }

  public PageReadingEvent(@NonNull final Object source, @NonNull final UUID accountId, @NonNull final UUID pageId, @NonNull final UUID bookId) {
    super(source);
    this.accountId = accountId;
    this.pageId = pageId;
    this.bookId = bookId;
  }

  public LocalDateTime getTime() {
    final long timestamp = this.getTimestamp();
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
  }

}
