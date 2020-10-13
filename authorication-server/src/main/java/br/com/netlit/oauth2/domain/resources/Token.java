package br.com.netlit.oauth2.domain.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Log4j2
@RestController
@RequestMapping(value = "/oauth", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class Token {

  private final ObjectMapper mapper;
  private final DefaultTokenServices tokenServices;

  public Token(final DefaultTokenServices tokenServices, final ObjectMapper mapper) {
    this.tokenServices = tokenServices;
    this.mapper = mapper;
  }

  @GetMapping("/user")
  public ResponseEntity<Object> user(final Authentication authentication) throws IOException {
    final Object principal = authentication.getPrincipal();
    final String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
    final String json = this.mapper.writeValueAsString(principal);
    log.info("Token " + token + " verified as user " + json);
    final OAuth2AccessToken accessToken = this.tokenServices.getAccessToken((OAuth2Authentication) authentication);
    final int maxAge = accessToken.getExpiresIn();
    log.info("Token " + token + " expires in " + maxAge + "s was verified as user " + json);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS))
        .body(principal);
  }
}
