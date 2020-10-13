package br.com.netlit.library.domain.bookmarks;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.netlit.library.domain.book.Book;
import br.com.netlit.library.domain.book.BookRepository;
import br.com.netlit.library.infra.utils.HttpRequest;
import br.com.netlit.library.infra.utils.UUIDs;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/bookmarks")
public class BookmarkRestController {

	private final BookRepository bookRepository;
	private final BookmarkEntityRepository bookmarkEntityRepository;
	private final BookmarkResource bookmarkResource;

	public BookmarkRestController(final BookRepository bookRepository,
			final BookmarkEntityRepository bookmarkEntityRepository, final BookmarkResource bookmarkResource) {
		this.bookRepository = bookRepository;
		this.bookmarkEntityRepository = bookmarkEntityRepository;
		this.bookmarkResource = bookmarkResource;
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(produces = "application/hal+json")
	public PagedResources<Resource<Book>> findAll(final Pageable pageable,
			final PagedResourcesAssembler<Book> assembler) {
		return this.bookmarkResource.all(pageable, assembler);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PutMapping(value = "/{bookId}")
	public ResponseEntity addToBookmarks(@PathVariable final String bookId) {

		final UUID accountId = this.authenticatedAccountId();

		if (!this.bookRepository.existsById(bookId)) {
			throw (new ResponseStatusException(BAD_REQUEST, "Book Not Found"));
		}

		BookmarkEntity bookmark = new BookmarkEntity();
		bookmark.setAccountId(accountId);
		bookmark.setBookId(UUID.fromString(bookId));

		this.bookmarkEntityRepository.save(bookmark);

		return ResponseEntity.noContent().build();

	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/{bookId}", produces = "application/hal+json")
	public Resource<Book> findById(@PathVariable final String bookId) {
		final UUID accountId = this.authenticatedAccountId();
		final UUID id = UUIDs.fromString(bookId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		this.bookmarkEntityRepository.findByIds(accountId, id)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		return this.bookmarkResource.byId(bookId);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@DeleteMapping(value = "/{bookId}")
	public ResponseEntity removeBookmark(@PathVariable final String bookId) {
		final UUID accountId = this.authenticatedAccountId();
		final UUID id = UUIDs.fromString(bookId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		this.bookmarkEntityRepository.delete(BookmarkEntity.builder().accountId(accountId).bookId(id).build());
		return ResponseEntity.noContent().build();
	}

	private UUID authenticatedAccountId() {
		return HttpRequest.getAuthenticatedAccountId()
				.orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Unauthorized"));
	}

}
