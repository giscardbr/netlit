package br.com.netlit.checkout.domain.upload.service.email;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.netlit.accounts.domain.event.EmailEvent;

@RestController
@RequestMapping("/tests")
public class TestController {

	private final ApplicationEventPublisher publisher;

	public TestController(ApplicationEventPublisher publisher) {
		super();
		this.publisher = publisher;
	}

	@GetMapping(produces = "application/hal+json")
	public ResponseEntity<?> doTest() {
		
		publisher.publishEvent(new EmailEvent(this, "corpo do email", null, new String[] {"giscard.ti@gmail.com"}, "assunto", false));
		
		return ResponseEntity.ok().build(); 
	}

}
