package br.com.netlit.library.domain.page;

import br.com.netlit.library.domain.event.PageReadingEvent;
import br.com.netlit.library.domain.read.page.PageReadingService;
import br.com.netlit.library.domain.read.page.ReadPage;
import br.com.netlit.library.infra.error.EntityNotFoundException;
import br.com.netlit.library.infra.utils.HttpRequest;
import br.com.netlit.library.infra.utils.UUIDs;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Log4j2
@Component
public class PageResource {

  private final PageReadingService pageReadingService;
  private final PageRepository pageRepository;
  private final ApplicationEventPublisher publisher;
  private final String bookAddress;

  public PageResource(final PageRepository pageRepository, final PageReadingService pageReadingService, final ApplicationEventPublisher publisher, @Value("${library.book-address}") final String bookAddress) {
    this.pageRepository = pageRepository;
    this.pageReadingService = pageReadingService;
    this.publisher = publisher;

    this.bookAddress = bookAddress;
  }

  public Resource<Page> byId(final String id) {
    final Page page = UUIDs.fromString(id).map(UUID::toString).flatMap(this.pageRepository::findById).orElseThrow(EntityNotFoundException::new);
    page.setContentLink(format("%s%s", this.bookAddress, page.getContentLink()));
    final String currentId = page.getUuid();
    final Resource<Page> resource = new Resource<>(page,
        linkTo(methodOn(PageRestController.class).findById(currentId)).withSelfRel(),
        linkTo(methodOn(PageRestController.class).findById(currentId)).withRel("page"),
        linkTo(methodOn(PageRestController.class).findBook(currentId)).withRel("book"));
    page.ifHasPrevious(next -> resource.add(linkTo(methodOn(PageRestController.class).findPrevious(currentId)).withRel("previous")));
    page.ifHasNext(next -> resource.add(linkTo(methodOn(PageRestController.class).findNext(currentId)).withRel("next")));

    HttpRequest.getAuthenticatedAccountId().ifPresent(accountId -> this.publishReading(accountId, page));

    return resource;
  }

  public Resource<Page> currentPageByBookId(final UUID bookId) {
    return HttpRequest.getAuthenticatedAccountId()
        .flatMap(accountId -> this.pageReadingService.lastPageRead(accountId, bookId))
        .map(ReadPage::getPageId)
        .map(UUID::toString)
        .map(this::byId)
        .orElseGet(() -> this.firstPageByBookId(bookId.toString()));
  }

  public Resource<Page> currentPageByBookId(final String id) {
    return UUIDs.fromString(id).map(this::currentPageByBookId).orElseThrow(EntityNotFoundException::new);
  }

  public Resource<Page> firstPageByBookId(final String id) {
    final Page page = UUIDs.fromString(id)
        .flatMap((UUID uuid) -> this.pageRepository.findByBookUuidAndSequence(uuid.toString(), 1L))
        .orElseThrow(EntityNotFoundException::new);
    final String currentId = page.getUuid();
    return this.byId(currentId);
  }

  public Resource<Page> nextById(final String id) {
    final Page page = UUIDs.fromString(id).map(UUID::toString).flatMap(this.pageRepository::findById).map(Page::getNext).orElseThrow(EntityNotFoundException::new);
    final String currentId = page.getUuid();
    return this.byId(currentId);
  }

  public Resource<Page> previousById(final String id) {
    final Page page = UUIDs.fromString(id).map(UUID::toString).flatMap(this.pageRepository::findById).map(Page::getPrevious).orElseThrow(EntityNotFoundException::new);
    final String currentId = page.getUuid();
    return this.byId(currentId);
  }

  private void publishReading(final UUID accountId, final Page page) {
    log.info("The account " + accountId + " is reading page " + page.getSequence() + " (" + page.getUuid() + ") of '" + page.getBook().getTitle() + "'");
    this.publisher.publishEvent(new PageReadingEvent(this, accountId.toString(), page.getUuid(), page.getBook().getUuid()));
  }
}
