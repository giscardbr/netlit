package br.com.netlit.library.domain.review;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.netlit.library.domain.book.BookRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/review")
public class ReviewRestController {

	private final ReviewResource reviewkResource;
	private final BookRepository bookRepository;

	public ReviewRestController(
			final BookRepository booksRepository, 
			final ReviewResource bookmarkResource) {
		this.bookRepository = booksRepository;
		this.reviewkResource = bookmarkResource;
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<?> create(final Authentication authentication, @RequestBody final ReviewCreateRequest request) {

		String email = this.getEmail(authentication);

		if (!this.bookRepository.existsById(request.getBookId().toString())) {
			throw (new ResponseStatusException(BAD_REQUEST, "Book Not Found"));
		}

		ReviewEntity reviewEntity = ReviewEntity.builder()
				.email(email)
				.bookId(request.getBookId())
				.comment(request.getComment())
				.rating(request.getRating())
				.created(LocalDateTime.now())
				.build();
		
		this.reviewkResource.save(reviewEntity);

		return ResponseEntity.noContent().build();

	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/book/{bookId}", produces = "application/hal+json")
	public ResponseEntity<ReviewCreateResponse> findById(final Authentication authentication, @PathVariable final String bookId) {

		 ReviewEntity reviewEntity = this.reviewkResource.byId(
				 this.getEmail(authentication), 
				 UUID.fromString(bookId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		 
		 ReviewCreateResponse response = ReviewCreateResponse.builder()
		 	.email(reviewEntity.getEmail())
		 	.bookId(reviewEntity.getBookId())
		 	.comment(reviewEntity.getComment())
		 	.created(reviewEntity.getCreated())
		 	.ratng(reviewEntity.getRating())
		 	.build();
		 
		 return new ResponseEntity<ReviewCreateResponse>(response, HttpStatus.OK);
	}

	private String getEmail(final Authentication authentication) {
		OAuth2Authentication oauth2 = (OAuth2Authentication) authentication;
		String email = (String) oauth2.getPrincipal();
		return email;
	}

}
