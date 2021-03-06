package br.com.netlit.library.infra.web;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Configuration
public class TraceRequestInterceptor {

  private final String path;

  public TraceRequestInterceptor(@Value("${server.servlet.context-path}") final String path) {
    this.path = path;
  }

  @Bean
  public CommonsRequestLoggingFilter traceLogFilter() {
    val filter = new RequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(false);
    filter.setIncludeHeaders(false);
    return filter;
  }

  private class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected String createMessage(final HttpServletRequest request, final String prefix, final String suffix) {
      val msg = new StringBuilder();
      msg.append(prefix);
      msg.append(request.getMethod()).append(" ").append(request.getRequestURI());

      if (this.isIncludeQueryString()) {
        val queryString = request.getQueryString();
        if (queryString != null) {
          msg.append('?').append(queryString);
        }
      }

      if (this.isIncludeClientInfo()) {
        val client = request.getRemoteAddr();
        if (StringUtils.hasLength(client)) {
          msg.append(";client=").append(client);
        }
        val session = request.getSession(false);
        if (session != null) {
          msg.append(";session=").append(session.getId());
        }
        val user = request.getRemoteUser();
        if (user != null) {
          msg.append(";user=").append(user);
        }
      }

      if (this.isIncludeHeaders()) {
        msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
      }

      if (this.isIncludePayload()) {
        val payload = this.getMessagePayload(request);
        if (payload != null) {
          msg.append(";payload=").append(payload);
        }
      }

      msg.append(suffix);
      return msg.toString();
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
      return !request.getRequestURI().startsWith(path + "/actuator") && this.logger.isDebugEnabled();
    }
  }
}

