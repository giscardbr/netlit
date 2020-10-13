package br.com.netlit.accounts.domain.account.general.resource;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminReadingService;
import br.com.netlit.accounts.domain.account.parent.service.ParentUserCreationRequest;
import br.com.netlit.accounts.domain.account.parent.service.SubAccountService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(SubAccountResource.PATH)
public class SubAccountResource {

	public static final String PATH = "/accounts/{accountId}/sub-accounts";
	private final ParentAdminReadingService readingService;
	private final SubAccountService service;

	public SubAccountResource(
			final ParentAdminReadingService readingService,
			final SubAccountService service) {
		this.readingService = readingService;
		this.service = service;
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(produces = "application/hal+json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public PaginatedScanList<SubAccountEntity> findAll(@PathVariable final UUID accountId) {

		return this.service.findBy(accountId);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/{subAccountId}", produces = "application/hal+json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<ParentUserCreationRequest> findById(
			@PathVariable final UUID accountId,
			@PathVariable final UUID subAccountId) {
		
		ParentUserCreationRequest response = this.service.findById(subAccountId);
		
		return ResponseEntity.ok().body(response);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PutMapping(value = "/{subAccountId}", consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> update(
			@PathVariable final UUID accountId,
			@PathVariable final UUID subAccountId,
			@RequestBody final @Valid ParentUserCreationRequest request) {

		this.service.update(subAccountId, request);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping(consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> create(
			@PathVariable final UUID accountId,
			@RequestBody final @Valid ParentUserCreationRequest request) {

		this.service.create(request);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/users/check-email", produces = "application/hal+json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<Void> checkEmail(@RequestParam(required = true, value = "email") final String email) {

		this.readingService.checkEmail(email);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/{subAccountId}", method = RequestMethod.DELETE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> remove(
			@PathVariable final UUID accountId,
			@PathVariable("subAccountId") UUID subAccountId) {
		
		this.service.deactivate(subAccountId);
	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Payload inválido!")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleException() {
		System.out.println("#############################################################");
		System.out.println("#PARSE ERROR #");
		System.out.println("#############################################################");
	}

}
