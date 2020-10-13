package br.com.netlit.accounts.domain.account.general.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import br.com.netlit.accounts.domain.account.error.AccountNotFoundException;
import br.com.netlit.accounts.domain.account.error.SchoolGradeRoomNotFoundException;
import br.com.netlit.accounts.domain.account.general.entity.SchoolGradeRoomEntity;
import br.com.netlit.accounts.domain.account.general.entity.SchoolRoomStudentEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.SchoolGradeRoomRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.account.parent.service.SchoolRoomStudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class SchoolRoomStudentResource {

	public static final String PATH = SchoolGradeRoomResource.PATH + "/{schoolGradeRoomId}/students";
	
	@Autowired
	private SchoolGradeRoomRepository schoolGradeRoomService;

	@Autowired
	private SubAccountRepository subAccountRepository;

	@Autowired
	private SchoolRoomStudentService schoolRoomStudentService;

	public SchoolRoomStudentResource() {
	}
	
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = PATH, produces = "application/hal+json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<SchoolRoomStudentResponse> findAll(@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId) {

		PaginatedScanList<SchoolRoomStudentEntity> _result = this.schoolRoomStudentService.findBy(schoolGradeRoomId);

		List<SubAccountEntity> result = new ArrayList<SubAccountEntity>();

		_result.stream().forEach(e -> {
			
			result.add(subAccountRepository.findById(e.getSubAccountId()).orElseThrow(AccountNotFoundException::new));
			
		});
		
		return new ResponseEntity<SchoolRoomStudentResponse>(SchoolRoomStudentResponse.builder().students(result).build(), HttpStatus.OK);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping(value = PATH, consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> create(
			@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId,
			@RequestBody final SchoolRoomStudentRequest request) {

		this.schoolRoomStudentService.save(SchoolRoomStudentEntity.builder().schoolGradeRoomId(schoolGradeRoomId).subAccountId(request.getSubAccountIdStudent()).build());
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@PostMapping(value = SchoolGradeRoomResource.PATH + "/students", consumes = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> createByCode(
			@RequestBody final SchoolRoomStudentRequest request) {

		PaginatedScanList<SchoolGradeRoomEntity> result = this.schoolGradeRoomService.findBy(request.getCode());
		
		if (result.size() == 0) throw new SchoolGradeRoomNotFoundException();
		
		SchoolGradeRoomEntity schoolGradeRoomEntity = result.get(0);
		
		this.schoolRoomStudentService.save(SchoolRoomStudentEntity.builder().schoolGradeRoomId(schoolGradeRoomEntity.getId()).subAccountId(request.getSubAccountIdStudent()).build());
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = PATH + "/{subAccountIdStudent}", method = RequestMethod.DELETE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<?> remove(
			@PathVariable("schoolGradeRoomId") final UUID schoolGradeRoomId,
			@PathVariable("subAccountIdStudent") final UUID subAccountIdStudent) {
		
		this.schoolRoomStudentService.remove(SchoolRoomStudentEntity.builder().schoolGradeRoomId(schoolGradeRoomId).subAccountId(subAccountIdStudent).build());
	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = SchoolGradeRoomResource.PATH + "/students/{subAccountIdStudent}", produces = "application/hal+json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, value = "Bearer XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", dataType = "string", paramType = "header") })
	public ResponseEntity<SchoolGradeRoomResponse> findGradeRoomByStudent(
			@PathVariable("subAccountIdStudent") final UUID subAccountIdStudent
			) {

		PaginatedScanList<SchoolRoomStudentEntity> _result = this.schoolRoomStudentService.findBySubAccountId(subAccountIdStudent);

		List<SchoolGradeRoomRequest> result = new ArrayList<SchoolGradeRoomRequest>();

		_result.stream().forEach(e -> {
			
			result.add(SchoolGradeRoomRequest.valueOf(schoolGradeRoomService.findById(e.getSchoolGradeRoomId()).orElseThrow(SchoolGradeRoomNotFoundException::new)));
			
		});
		
		return new ResponseEntity<SchoolGradeRoomResponse>(SchoolGradeRoomResponse.builder().gradeRooms(result).build(), HttpStatus.OK);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Payload inválido!")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleException() {
		System.out.println("#############################################################");
		System.out.println("#PARSE ERROR #");
		System.out.println("#############################################################");
	}

}
