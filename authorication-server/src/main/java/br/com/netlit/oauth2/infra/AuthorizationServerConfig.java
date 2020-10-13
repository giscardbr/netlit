package br.com.netlit.oauth2.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  private static final String AUTHORIZATION_CODE = "authorization_code";
  private static final String GRANT_TYPE_PASSWORD = "password";
  private static final String REFRESH_TOKEN = "refresh_token";
  private static final String SCOPE_READ = "read";
  private static final String SCOPE_WRITE = "write";
  private static final String TRUST = "trust";

  private final AuthenticationManager authManager;
  private final TokenStore tokenStore;

  public AuthorizationServerConfig(final AuthenticationManager authManager, final TokenStore tokenStore) {
    this.authManager = authManager;
    this.tokenStore = tokenStore;
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient(ClientCredentials.CLIENT_ID)
        .secret(ClientCredentials.CLIENT_SECRET)
        .authorizedGrantTypes(GRANT_TYPE_PASSWORD)
        .scopes(SCOPE_READ, SCOPE_WRITE);
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenStore(this.tokenStore)
        .authenticationManager(this.authManager);
  }
}
