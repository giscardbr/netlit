package br.com.netlit.accounts.domain.account.city.resource;

import br.com.netlit.accounts.domain.event.CityAdminCreationRequestEvent;
import br.com.netlit.accounts.infra.utils.HttpRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/" + CityAccountResource.PATH)
public class CityAccountResource {

  public static final String PATH = "city-accounts";

  private final ApplicationEventPublisher publisher;

  public CityAccountResource(final ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @PostMapping
  private ResponseEntity create(@RequestBody final @Valid CityAdminCreationHttpRequest request) {
    this.publisher.publishEvent(new CityAdminCreationRequestEvent(this, request));

    final UUID id = UUID.randomUUID();
    final String url = HttpRequest.getUrl().replace(PATH, "processes");
    return ResponseEntity.accepted()
        .header("Location", url + "/" + id)
        .build();
  }
}
