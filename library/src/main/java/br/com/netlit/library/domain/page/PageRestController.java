package br.com.netlit.library.domain.page;

import br.com.netlit.library.domain.book.Book;
import br.com.netlit.library.domain.book.BookResource;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pages")
public class PageRestController {

  private final BookResource bookResource;
  private final PageResource pageResource;

  public PageRestController(final BookResource bookResource, final PageResource pageResource) {
    this.bookResource = bookResource;
    this.pageResource = pageResource;
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(value = "/{pageId}", produces = "application/hal+json")
  public Resource<Page> findById(@PathVariable final String pageId) {
    return this.pageResource.byId(pageId);
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(value = "/{pageId}/previous", produces = "application/hal+json")
  public Resource<Page> findPrevious(@PathVariable final String pageId) {
    return this.pageResource.previousById(pageId);
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(value = "/{pageId}/next", produces = "application/hal+json")
  public Resource<Page> findNext(@PathVariable final String pageId) {
    return this.pageResource.nextById(pageId);
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(value = "/{pageId}/book", produces = "application/hal+json")
  public Resource<Book> findBook(@PathVariable final String pageId) {
    return this.bookResource.bookByPageId(pageId);
  }
}
