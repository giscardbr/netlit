package br.com.netlit.accounts.domain.process;

import br.com.netlit.accounts.infra.error.EntityNotFoundException;
import br.com.netlit.accounts.infra.utils.HttpRequest;
import br.com.netlit.accounts.infra.utils.UUIDs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processes")
public class ProcessResource {

  @GetMapping("/{id}")
  public ResponseEntity<String> findById(@PathVariable final String id) {
    UUIDs.fromString(id).orElseThrow(EntityNotFoundException::new);
    final String url = HttpRequest.getUrl();
    return ResponseEntity.ok()
        .header("Content-Type", "application/hal+json")
        .body("{\"status\":\"IN_PROGRESS\",\"_links\":{\"self\":{\"href\":\"" + url + "\"},\"process\":{\"href\":\"" + url + "\"}}}");
  }
}
