package br.com.netlit.library.domain.read.book;

import br.com.netlit.library.domain.event.PageReadingEvent;
import br.com.netlit.library.domain.page.PageRepository;
import br.com.netlit.library.domain.read.page.ReadPageRepository;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Log4j2
@Service
public class BookReadingService {

  private final ReadBookRepository readBookRepository;
  private final ReadPageRepository readPageRepository;
  private final PageRepository pageRepository;

  public BookReadingService(final ReadBookRepository readBookRepository, final ReadPageRepository readPageRepository, final PageRepository pageRepository) {
    this.readBookRepository = readBookRepository;
    this.readPageRepository = readPageRepository;
    this.pageRepository = pageRepository;
  }

  @EventListener
  void count(final PageReadingEvent event) {
    val accountId = event.getAccountId();
    val bookId = event.getBookId();
    val percentage = this.calculateRead(accountId, bookId);

    this.readBookRepository.save(ReadBook.builder()
        .accountId(accountId)
        .bookId(bookId)
        .time(event.getTime())
        .percentage(percentage)
        .build());
  }

  public double calculateRead(final UUID accountId, final UUID bookId) {
    val pages = this.pageRepository.countByBookUuid(bookId.toString());
    val read = this.readPageRepository.findReadPageIdsByAccountAndBook(accountId, bookId).size();
    val percentage = BigDecimal.valueOf(read)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(pages), 2, RoundingMode.HALF_EVEN)
        .doubleValue();
    log.info("The book {} have {} pages and {} was read by {}", bookId, pages, read, accountId);
    return percentage;
  }
}
