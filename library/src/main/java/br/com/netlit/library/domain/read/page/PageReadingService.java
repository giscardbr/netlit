package br.com.netlit.library.domain.read.page;

import br.com.netlit.library.domain.event.PageReadingEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class PageReadingService {

  private final ReadPageRepository readPageRepository;

  public PageReadingService(final ReadPageRepository readPageRepository) {
    this.readPageRepository = readPageRepository;
  }

  @EventListener
  void register(final PageReadingEvent event) {
    this.readPageRepository.save(ReadPage.builder()
        .accountId(event.getAccountId())
        .time(event.getTime())
        .bookId(event.getBookId())
        .pageId(event.getPageId())
        .build());
  }

  public Optional<ReadPage> lastPageRead(final UUID accountId, final UUID bookId) {
    final Optional<ReadPage> reading = this.readPageRepository.findLatestByAccountAndBook(accountId, bookId);
    reading.ifPresent(read -> log.info("Latest found page {} at {}", read.getPageId(), read.getTime()));
    if (!reading.isPresent()) log.info("This book has not been read yet");
    return reading;
  }
}
