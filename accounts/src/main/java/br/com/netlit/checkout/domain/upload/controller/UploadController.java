package br.com.netlit.checkout.domain.upload.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.netlit.checkout.domain.upload.event.UploadRequestEvent;
import br.com.netlit.checkout.domain.upload.model.UploadEntity;
import br.com.netlit.checkout.domain.upload.model.UploadUserRowEntity;
import br.com.netlit.checkout.domain.upload.service.UploadService;

@RestController
@RequestMapping("/uploads")
public class UploadController {

	private final ApplicationEventPublisher publisher;

	private UploadService service;

	public UploadController(ApplicationEventPublisher publisher, UploadService service) {
		super();
		this.publisher = publisher;
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> uploadFile(
			@RequestParam("account_id") final String accountId, 
			final MultipartFile file,
			@RequestParam("send_mail") final boolean sendMail,
			@RequestParam("generate_password") final boolean generatePassword) throws IOException {

		UploadEntity entity = service.save(UploadEntity.builder().accountId(accountId).build());

		this.publisher.publishEvent(new UploadRequestEvent(this, entity, accountId, file, sendMail, generatePassword));

		URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();

		return ResponseEntity.created(url).build();
	}

	@GetMapping(value = "/{uploadId}", produces = "application/hal+json")
	public ResponseEntity<List<UploadUserRowEntity>> findById(@PathVariable("uploadId") final Long uploadId) {
		return new ResponseEntity<List<UploadUserRowEntity>>(this.service.findById(uploadId), HttpStatus.OK);
	}

	@PutMapping(value = "/row/{uploadUserRowId}", consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable("uploadUserRowId") final Long uploadUserRowId,
			@RequestBody final @Valid UploadUserRowEntity request) {

		this.service.updateUploa1dUserRowEntity(request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/import/{uploadId}", produces = "application/hal+json")
	public ResponseEntity<?> importUpload(@PathVariable final Long uploadId) {
		
		this.service.importUpload(uploadId);
		
		return new ResponseEntity<List<UploadUserRowEntity>>(HttpStatus.OK);
	}

	@RequestMapping(value = "/row/{uploadUserRowId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("uploadUserRowId") long uploadUserRowId) {
		
		this.service.deleteUploadUserRow(uploadUserRowId);
	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
