package br.com.netlit.accounts.domain.account.mock.library;

import br.com.netlit.accounts.infra.utils.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/" + LibraryAccountResource.PATH)
public class LibraryAccountResource {

  public static final String PATH = "library-accounts";

  @PostMapping
  private ResponseEntity create(@RequestBody final @Valid LibraryAccountCreationHttpRequest request) {
    final UUID id = UUID.randomUUID();
    final String url = HttpRequest.getUrl().replace(PATH, "processes");
    return ResponseEntity.accepted()
        .header("Location", url + "/" + id)
        .build();
  }
}
