package br.com.netlit.library.domain.bookmarks;

import br.com.netlit.library.domain.book.Book;
import br.com.netlit.library.domain.book.BookResource;
import br.com.netlit.library.infra.utils.HttpRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class BookmarkResource {

  private final BookResource bookResource;
  private final BookmarkEntityRepository bookmarkEntityRepository;

  public BookmarkResource(final BookResource bookResource, final BookmarkEntityRepository bookmarkEntityRepository) {
    this.bookResource = bookResource;
    this.bookmarkEntityRepository = bookmarkEntityRepository;
  }

  public PagedResources<Resource<Book>> all(final Pageable pageable, final PagedResourcesAssembler<Book> assembler) {
    final Page<BookmarkEntity> page = HttpRequest.getAuthenticatedAccountId()
        .map(id -> this.bookmarkEntityRepository.findByAccountId(id, pageable))
        .orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));

    final Map<String, Book> map = StreamSupport
        .stream(this.bookResource.all(page.map(BookmarkEntity::getBookId).getContent().stream().map(UUID::toString).collect(Collectors.toList())).spliterator(), false)
        .collect(Collectors.toMap(Book::getUuid, Function.identity()));

    final Page<Book> books = page.map(read -> map.get(read.getBookId().toString()))
        .map(book -> {
          book.getLinks().removeIf(link -> link.getRel().equals("self"));
          book.add(linkTo(methodOn(BookmarkRestController.class).findById(book.getUuid())).withSelfRel());
          return book;
        });

    return assembler.toResource(books, linkTo(BookmarkRestController.class).withSelfRel());
  }

  public Resource<Book> byId(final String bookId) {
    final Resource<Book> book = this.bookResource.byId(bookId);
    book.getLinks().removeIf(link -> link.getRel().equals("self"));
    book.add(linkTo(methodOn(BookmarkRestController.class).findById(bookId)).withSelfRel());
    return book;
  }
}
