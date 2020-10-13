package br.com.netlit.accounts.infra.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class HttpRequest {

  public static String getUrl() {
    return Optional.ofNullable(RequestContextHolder.currentRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest)
        .map(HttpServletRequest::getRequestURL)
        .map(StringBuffer::toString)
        .orElse("");
  }
}
