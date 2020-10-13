package br.com.netlit.oauth2.infra;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class SimpleErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final boolean includeStackTrace) {
    final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
    errorAttributes.remove("path");
    errorAttributes.remove("status");
    errorAttributes.remove("timestamp");
    return errorAttributes;
  }
}