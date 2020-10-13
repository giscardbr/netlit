package br.com.netlit.library.domain.latest_reading;

import br.com.netlit.library.domain.book.Book;
import br.com.netlit.library.domain.book.BookRepository;
import br.com.netlit.library.domain.book.BookRestController;
import br.com.netlit.library.domain.read.book.ReadBook;
import br.com.netlit.library.domain.read.book.ReadBookRepository;
import br.com.netlit.library.infra.utils.HttpRequest;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
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

import static java.lang.String.format;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class LatestReadingResource {

  private final BookRepository bookRepository;
  private final ReadBookRepository readBookRepository;
  private final String bookAddress;

  public LatestReadingResource(final BookRepository bookRepository, final ReadBookRepository readBookRepository, @Value("${library.book-address}") final String bookAddress) {
    this.bookRepository = bookRepository;
    this.readBookRepository = readBookRepository;
    this.bookAddress = bookAddress;
  }

  public PagedResources<Resource<Book>> all(final Pageable pageable, final PagedResourcesAssembler<Book> assembler) {
    final Page<ReadBook> page = HttpRequest.getAuthenticatedAccountId()
        .map(id -> this.readBookRepository.findBookIdsByAccountId(id, pageable))
        .orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));

    final Map<String, Book> map = StreamSupport
        .stream(this.bookRepository.findAllById(page.map(ReadBook::getBookId).getContent().stream().map(UUID::toString).collect(Collectors.toList())).spliterator(), false)
        .collect(Collectors.toMap(Book::getUuid, Function.identity()));

    final Page<Book> books = page.map(read -> {
      final Book book = map.get(read.getBookId().toString());
      book.setPercentageRead(read.getPercentage());
      return book;
    });

    for (final Book book : books) {
      final val id = book.getUuid();
      book.setCoverImageLink(format("%s%s", this.bookAddress, book.getCoverImageLink()));
      book.setAgeGroup(null);
      book.setCollection(null);
      book.setCrossCuttingThemes(null);
      book.setDisciplinaryContents(null);
      book.setEbsaCode(null);
      book.setGuidingTheme(null);
      book.setIllustrator(null);
      book.setIsbn(null);
      book.setReview(null);
      book.setSegment(null);
      book.setSubjects(null);
      book.setTranslator(null);
      book.setYear(null);
      book.setSpecialDates(null);
      book.setAwards(null);
      book.add(linkTo(methodOn(BookRestController.class).findById(id)).withSelfRel());
      book.add(linkTo(methodOn(BookRestController.class).findById(id)).withRel("book"));
    }

    return assembler.toResource(books, linkTo(LatestReadingRestController.class).withSelfRel());
  }
}
