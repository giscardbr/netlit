package br.com.netlit.library.infra.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public final class HttpRequest {

  public static Optional<UUID> getAuthenticatedAccountId() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(OAuth2Authentication.class::isInstance)
        .map(OAuth2Authentication.class::cast)
        .map(OAuth2Authentication::getUserAuthentication)
        .map(Authentication::getDetails)
        .filter(LinkedHashMap.class::isInstance)
        .map(LinkedHashMap.class::cast)
        .map(linkedHashMap -> linkedHashMap.get("id"))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .flatMap(UUIDs::fromString);
  }

  public static String getBaseUrl() {
    return Optional.of(RequestContextHolder.currentRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest)
        .map(request -> {
          final String path = request.getRequestURI();
          final String endpoint = request.getRequestURL().toString();
          return endpoint.replace(path, "");
        })
        .orElse("");
  }

  public static String getUrl() {
    return Optional.of(RequestContextHolder.currentRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest)
        .map(HttpServletRequest::getRequestURL)
        .map(StringBuffer::toString)
        .orElse("");
  }
}
