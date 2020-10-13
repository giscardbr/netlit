package br.com.netlit.accounts.domain.account.general.resource;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.netlit.accounts.domain.account.parent.service.AccountRequest;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminCreationRequest;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminReadingService;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminWritingService;
import br.com.netlit.accounts.domain.account.parent.service.TimeReadingCreationRequest;
import br.com.netlit.accounts.domain.account.parent.service.UserDetail;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/accounts")
public class AccountResource {

	private final ParentAdminReadingService readingService;
	private final ParentAdminWritingService writingService;

	public AccountResource(
			final ParentAdminReadingService readingService,
			final ParentAdminWritingService writingService) {
		this.readingService = readingService;
		this.writingService = writingService;
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/{accountId}", produces = "application/hal+json")
	public Resource<ParentAdminCreationRequest> findById(final Authentication autentication,
			@PathVariable final String accountId) {
		return this.readingService.byId(accountId);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PutMapping(value = "/{accountId}", consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable final UUID accountId,
			@RequestBody final @Valid AccountRequest request) {

		this.writingService.update(accountId, request);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/users", produces = "application/hal+json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public Resource<UserDetail> findUserDetails() {

        OAuth2Authentication oauth2 = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();		
		
		String email = (String) oauth2.getPrincipal();
		
		return new Resource<>(this.readingService.findUserDetailByEmail(email));

	
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/users/check-email", produces = "application/hal+json")
	public ResponseEntity<Void> checkEmail(@RequestParam(required = true, value = "email") final String email) {

		this.readingService.checkEmail(email);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/users/check-document", produces = "application/hal+json")
	public ResponseEntity<Void> checkDocument(
			@RequestParam(required = true, value = "document") final String document,
			@RequestParam(required = true, value = "is_business") final Boolean isBusiness) {

		this.readingService.checkDocument(document, isBusiness);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/{accountId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deactivate(@PathVariable("accountId") String accountId) {
		
		this.writingService.deactivate(accountId);
	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping(value = "/{accountId}/book/{bookId}/time-reading", consumes = "application/json")
	public ResponseEntity<?> saveTimeReading(
			@PathVariable("accountId") final String accountId,
			@PathVariable("bookId") final String bookId,
			@RequestBody final @Valid TimeReadingCreationRequest request) {

		return ResponseEntity.noContent().build();
	}

}
