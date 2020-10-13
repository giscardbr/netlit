package br.com.netlit.customerservice.infra.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsConfig {

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    final FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new SimpleCorsFilter(source));
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return registrationBean;
  }
}

class SimpleCorsFilter extends CorsFilter {

  public SimpleCorsFilter(final CorsConfigurationSource configSource) {
    super(configSource);
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    final String method = request.getMethod();
    final String uri = request.getRequestURI();
    if (HttpMethod.OPTIONS.name().equalsIgnoreCase(method)) {
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Methods", "*");
      response.setHeader("Access-Control-Allow-Headers", "*");
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      super.doFilterInternal(request, response, filterChain);
    }
  }
}
