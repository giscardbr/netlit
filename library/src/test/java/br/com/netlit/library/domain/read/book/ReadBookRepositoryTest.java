package br.com.netlit.library.domain.read.book;

import br.com.netlit.library.infra.database.DynamoDBRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@ActiveProfiles("test")
public class ReadBookRepositoryTest extends DynamoDBRepositoryTest {

  @Autowired
  private ReadBookRepository repository;

  @Test
  public void a() {
    final UUID accountId = UUID.fromString("1256f97f-5520-44a4-bf3b-b60de36479ee");
    final UUID bookId = UUID.fromString("9e101039-96b6-41c7-8e33-7d53a33ad46c");

    final LocalDateTime second = LocalDateTime.of(2019, 4, 6, 0, 2);
    final LocalDateTime first = LocalDateTime.of(2019, 4, 6, 0, 1);

    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(second)
        .bookId(bookId)
        .build());

    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(first)
        .bookId(bookId)
        .build());

    final Page<ReadBook> page = this.repository.findBookIdsByAccountId(accountId, PageRequest.of(0, 22));
    page.forEach(log::info);
    assertThat(page).hasSize(1);
  }

  @Test
  public void b() {
    final UUID accountId = UUID.fromString("1256f97f-5520-44a4-bf3b-b60de36479ee");
    final UUID bookId = UUID.fromString("9e101039-96b6-41c7-8e33-7d53a33ad46c");

    final LocalDateTime third = LocalDateTime.of(2019, 4, 6, 0, 3);
    final LocalDateTime fourth = LocalDateTime.of(2019, 4, 6, 0, 4);
    final LocalDateTime first = LocalDateTime.of(2019, 4, 6, 0, 1);
    final LocalDateTime second = LocalDateTime.of(2019, 4, 6, 0, 2);

    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(third)
        .bookId(UUID.randomUUID())
        .build());
    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(fourth)
        .bookId(UUID.randomUUID())
        .build());
    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(second)
        .bookId(bookId)
        .build());
    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(first)
        .bookId(bookId)
        .build());
    this.repository.save(ReadBook.builder()
        .accountId(accountId)
        .time(first)
        .bookId(UUID.randomUUID())
        .build());

    final Page<ReadBook> page = this.repository.findBookIdsByAccountId(accountId, PageRequest.of(0, 22));
    page.forEach(log::info);
    assertThat(page).hasSize(4);
  }
}