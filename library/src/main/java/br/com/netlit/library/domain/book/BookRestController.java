package br.com.netlit.library.domain.book;

import br.com.netlit.library.domain.page.Page;
import br.com.netlit.library.domain.page.PageResource;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/books")
public class BookRestController {

    private final BookResource bookResource;
    private final PageResource pageResource;

    public BookRestController(final BookResource bookResource, final PageResource pageResource) {
        this.bookResource = bookResource;
        this.pageResource = pageResource;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(produces = "application/hal+json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public PagedResources<Resource<Book>> findAll(
            @RequestParam(required = false, value = "years") final List<String> years,
            @RequestParam(required = false, value = "authors") final List<String> authors,
            @RequestParam(required = false, value = "collections") final List<String> collections,
            @RequestParam(required = false, value = "age_groups") final List<String> ageGroups,
            @RequestParam(required = false, value = "segments") final List<String> segments,
            @RequestParam(required = false, value = "guiding_themes") final List<String> guidingThemes,
            @RequestParam(required = false, value = "subjects") final List<String> subjects,
            @RequestParam(required = false, value = "textual_genres") final List<String> textualGenres,
            @RequestParam(required = false, value = "special_dates") final List<String> specialDates,
            @RequestParam(required = false, value = "cross-cutting_themes") final List<String> crossCuttingThemes,
            @RequestParam(required = false, value = "disciplinary_contents") final List<String> disciplinaryContents,
            @RequestParam(required = false, value = "title") final String title,
            @RequestParam(required = false, value = "highlight") final String highlight,
            final Pageable pageable,
            final PagedResourcesAssembler<Book> assembler) {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("years = {}", years);
        log.info("authors = {}", authors);
        log.info("collections = {}", collections);
        log.info("ageGroups = {}", ageGroups);
        log.info("segments = {}", segments);
        log.info("guidingThemes = {}", guidingThemes);
        log.info("subjects = {}", subjects);
        log.info("textualGenres = {}", textualGenres);
        log.info("specialDates = {}", specialDates);
        log.info("crossCuttingThemes = {}", crossCuttingThemes);
        log.info("disciplinaryContents = {}", disciplinaryContents);
        log.info("title = {}", title);
        log.info("highlight = {}", highlight);

        final BookQuery query = new BookQuery(
                years,
                authors,
                collections,
                ageGroups,
                segments,
                guidingThemes,
                subjects,
                textualGenres,
                specialDates,
                crossCuttingThemes,
                disciplinaryContents,
                title,
                highlight
        );

        return this.bookResource.all(query, pageable, assembler);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/{bookId}", produces = "application/hal+json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public Resource<Book> findById(@PathVariable final String bookId) {
        return this.bookResource.byId(bookId);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/{bookId}/current-page", produces = "application/hal+json")
    public Resource<Page> findCurrentPage(@PathVariable final String bookId) {
        return this.pageResource.currentPageByBookId(bookId);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/{bookId}/first-page", produces = "application/hal+json")
    public Resource<Page> findFirstPage(@PathVariable final String bookId) {
        return this.pageResource.firstPageByBookId(bookId);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public ResponseEntity<?> create(@RequestBody final Book book) {
        bookResource.createBook(book);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public ResponseEntity<?> uploadFile(@RequestParam final MultipartFile epub) throws IOException {
        bookResource.uploadBook(epub);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/uploadSupplement")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public ResponseEntity<?> uploadSupplement(@RequestParam final MultipartFile pdf) throws IOException {
        bookResource.uploadSupplement(pdf);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/findByIds", produces = "application/hal+json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header")})
    public List<Book> findByAgeGroup(@RequestParam(value = "uuids", required = true) List<String> uuids) {
        return bookResource.findBooksByListId(uuids);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Payload inválido!")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleException() {
        System.out.println("#############################################################");
        System.out.println("#PARSE ERROR #");
        System.out.println("#############################################################");
    }

}
