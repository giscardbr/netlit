package br.com.netlit.library.domain.latest_reading;

import br.com.netlit.library.domain.book.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/latest-readings")
public class LatestReadingRestController {

  private final LatestReadingResource latestReadingResource;

  public LatestReadingRestController(final LatestReadingResource latestReadingResource) {
    this.latestReadingResource = latestReadingResource;
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(produces = "application/hal+json")
  public PagedResources<Resource<Book>> findAll(final Pageable pageable, final PagedResourcesAssembler<Book> assembler) {
    return this.latestReadingResource.all(pageable, assembler);
  }
}
