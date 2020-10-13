package br.com.netlit.library.domain.highlight;

import br.com.netlit.library.domain.book.Book;
import br.com.netlit.library.domain.book.BookQuery;
import br.com.netlit.library.domain.book.BookResource;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/highlights")
public class HighlightRestController {

  private final BookResource bookResource;

  public HighlightRestController(final BookResource bookResource) {
    this.bookResource = bookResource;
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping(produces = "application/hal+json")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
  public List<Book> findByAgeGroup( @RequestParam(value="page", required=true) Integer age) {
        return bookResource.findByAgeGroup(age);
  }

}
