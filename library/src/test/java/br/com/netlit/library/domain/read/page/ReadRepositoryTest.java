package br.com.netlit.library.domain.read.page;

import br.com.netlit.library.domain.read.page.ReadPage;
import br.com.netlit.library.domain.read.page.ReadPageRepository;
import br.com.netlit.library.infra.database.DynamoDBRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@ActiveProfiles("test")
public class ReadRepositoryTest extends DynamoDBRepositoryTest {

  @Autowired
  private ReadPageRepository repository;

  @Test
  public void shouldFindLatestRead() {
    final UUID accountId = UUID.fromString("1256f97f-5520-44a4-bf3b-b60de36479ee");
    final UUID bookId = UUID.fromString("9e101039-96b6-41c7-8e33-7d53a33ad46c");

    final LocalDateTime third = LocalDateTime.of(2019, 4, 6, 0, 3);
    final LocalDateTime fourth = LocalDateTime.of(2019, 4, 6, 0, 4);
    final LocalDateTime first = LocalDateTime.of(2019, 4, 6, 0, 1);
    final LocalDateTime second = LocalDateTime.of(2019, 4, 6, 0, 2);

    this.repository.save(ReadPage.builder()
        .accountId(accountId)
        .time(fourth)
        .bookId(bookId)
        .pageId(UUID.fromString("abc84a64-9f3d-4f05-b705-dc77f8fd9651"))
        .build());
    this.repository.save(ReadPage.builder()
        .accountId(accountId)
        .time(third)
        .bookId(bookId)
        .pageId(UUID.fromString("abc84a64-9f3d-4f05-b705-dc77f8fd9651"))
        .build());
    this.repository.save(ReadPage.builder()
        .accountId(accountId)
        .time(first)
        .bookId(bookId)
        .pageId(UUID.fromString("9ecc35ed-dd8e-4a66-a42f-b367a340ebb0"))
        .build());
    this.repository.save(ReadPage.builder()
        .accountId(accountId)
        .time(second)
        .bookId(bookId)
        .pageId(UUID.fromString("dca4915c-1775-4739-9b03-8c73f42feda7"))
        .build());

    final ReadPage read = this.repository.findLatestByAccountAndBook(accountId, bookId).get();
    Assertions.assertThat(read).isNotNull();
    Assertions.assertThat(read.getTime()).isEqualTo(fourth);
    Assertions.assertThat(read.getPageId()).isNotNull();
  }
}