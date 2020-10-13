package br.com.netlit.accounts.domain.account.parent.resource;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.netlit.accounts.domain.event.ParentAdminCreationRequestEvent;

@RestController
@RequestMapping("/parent-accounts")
public class ParentAccountResource {

	private final ApplicationEventPublisher publisher;

	public ParentAccountResource(final ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody final @Valid ParentAdminCreationHttpRequest request) {
		this.publisher.publishEvent(new ParentAdminCreationRequestEvent(this, request));

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(UUID.randomUUID()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri.toString().replace("/parent-accounts", "processes"));

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Payload inválido!")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleException() {
		System.out.println("#############################################################");
		System.out.println("#PARSE ERROR #");
		System.out.println("#############################################################");
	}

}
