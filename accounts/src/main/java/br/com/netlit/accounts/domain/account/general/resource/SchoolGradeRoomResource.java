package br.com.netlit.accounts.domain.account.general.resource;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.netlit.accounts.domain.account.general.entity.SchoolGradeRoomEntity;
import br.com.netlit.accounts.domain.account.parent.service.SchoolGradeRoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(SchoolGradeRoomResource.PATH)
public class SchoolGradeRoomResource {

	public static final String PATH = "/school-grade-rooms";
	
	@Autowired
	private SchoolGradeRoomService service;

	public SchoolGradeRoomResource() {
	}
	
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(produces = "application/hal+json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public PaginatedScanList<SchoolGradeRoomEntity> findAll(
			@RequestParam(value = "subAccountId", required = false) final UUID subAccountId) {

		return this.service.findBy(subAccountId);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/{schoolGradeRoomId}", produces = "application/hal+json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<SchoolGradeRoomRequest> findById(
			@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId) {
		
		SchoolGradeRoomRequest response = SchoolGradeRoomRequest.valueOf(this.service.findById(schoolGradeRoomId));
		
		return ResponseEntity.ok().body(response);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PutMapping(value = "/{schoolGradeRoomId}", consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> update(
			@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId,
			@RequestBody final @Valid SchoolGradeRoomRequest request) {

		this.service.update(schoolGradeRoomId, SchoolGradeRoomEntity.valueOf(request));
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping(consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> create(
			@RequestBody final @Valid SchoolGradeRoomRequest request) {

		this.service.save(SchoolGradeRoomEntity.valueOf(request));
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/{schoolGradeRoomId}", method = RequestMethod.DELETE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> remove(
			@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId) {
		
		this.service.remove(schoolGradeRoomId);
	
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
