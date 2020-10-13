package br.com.netlit.customerservice.domain.resource;

import br.com.netlit.customerservice.domain.resource.data.ContactUsRequest;
import br.com.netlit.customerservice.domain.service.ContactUsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Log4j2
@RestController
@RequestMapping()
public class ContactUsResource {

  private final ContactUsService contactUsService;

  public ContactUsResource(ContactUsService contactUsService) {
    this.contactUsService = contactUsService;
  }

  @PostMapping(value = "/contact-us")
  public void contactUs(@Valid @RequestBody ContactUsRequest contactUsRequest) {
    contactUsService.createdEmail(contactUsRequest);
  }

}
