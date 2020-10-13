package br.com.netlit.library.infra.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.library.domain.book.BookRepository;
import br.com.netlit.library.domain.page.Page;
import br.com.netlit.library.domain.page.PageRepository;
import br.com.netlit.library.infra.database.init.Book;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
//@Component
public class Initializer implements ApplicationRunner {

  private final ObjectMapper mapper;
  private final BookRepository bookRepository;
  private final PageRepository pageRepository;
  private final Resource resource;

  public Initializer(final ObjectMapper mapper, final BookRepository bookRepository, final PageRepository pageRepository, @Value("classpath:new-books.json") final Resource resource) {
    this.mapper = mapper;
    this.bookRepository = bookRepository;
    this.pageRepository = pageRepository;
    this.resource = resource;
  }

  @Override
  @Transactional
  @SneakyThrows(IOException.class)
  public void run(final ApplicationArguments args) {
    final InputStream inputStream = this.resource.getInputStream();
    final Book[] books = this.mapper.readValue(inputStream, Book[].class);
    final int length = books.length;
    int counter = 0;
    log.info("Inserting {} books", length);
    for (final Book book : books) {
      log.info("Inserting book '{}'", book.getTitle());
      final br.com.netlit.library.domain.book.Book bookEntity = this.bookRepository.save(br.com.netlit.library.domain.book.Book.builder()
          .uuid(book.getId().toString())
          .ageGroup(book.getAgeGroup())
//          .author(book.getAuthor())
          .collection(book.getCollection())
//          .crossCuttingThemes(book.getTransversalThemes())
//          .disciplinaryContents(book.getDisciplines())
          .ebsaCode(book.getEbsa())
          .guidingTheme(book.getGuidingTheme())
//          .illustrator(book.getIllustrator())
          .isbn(book.getIsbn())
          .review(book.getReview())
          .segment(book.getSegment())
//          .subjects(book.getSubjects())
          .title(book.getTitle())
//          .translator(book.getTranslator())
          .year(book.getYear())
          .coverImageLink(book.getCoverImageLink())
          .trailerLink(book.getTrailerLink())
          .highlight(book.getHighlight())
//          .specialDates(book.getSpecialDates())
//          .awards(book.getAwards())
//          .adapters(book.getAdapters())
          .build());
      final List<Page> pageEntities = book.getPages().stream()
          .map(page -> Page.builder()
              .uuid(page.getId().toString())
              .sequence(page.getSequence())
              .contentLink(page.getContentLink())
              .book(bookEntity)
              .build())
          .map(this.pageRepository::save)
          .collect(Collectors.toList());
      bookEntity.setFirstPage(pageEntities.get(0));
      this.bookRepository.save(bookEntity);
      final int numberOfPages = pageEntities.size();
      for (int i = 0; i < numberOfPages; i++) {
        final Page page = pageEntities.get(i);
        if (i != 0) page.setPrevious(pageEntities.get(i - 1));
        if ((i + 1) != numberOfPages) page.setNext(pageEntities.get(i + 1));
      }
      this.pageRepository.saveAll(pageEntities);
      counter++;
      log.info("The book '{}' was inserted successfully! Missing {}", book.getTitle(), length - counter);
    }
  }
}
