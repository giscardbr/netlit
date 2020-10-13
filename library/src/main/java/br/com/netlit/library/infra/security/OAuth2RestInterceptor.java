package br.com.netlit.library.infra.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Log4j2
class OAuth2RestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
    log.info("Checking access token");
    ClientHttpResponse response = null;
    try {
      response = execution.execute(request, body);
    } finally {
      log.info("AuthorizationServer answered " + response.getStatusCode());
    }
    return response;
  }
}
