package br.com.netlit.accounts.domain.password;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/passwords")
public class PasswordResource {

  private final PasswordService passwordService;

  public PasswordResource(final PasswordService passwordService) {
    this.passwordService = passwordService;
  }

  @PostMapping
  private ResponseEntity create(@RequestBody final @Valid PasswordResetHttpRequest request) {
    this.passwordService.reset(request.getUsername());
    return ResponseEntity.accepted().build();
  }
}
