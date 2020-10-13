package br.com.netlit.library.infra.web;

import lombok.extern.java.Log;
import lombok.val;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Log
@Component
public class EndpointsListener {

  @EventListener
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    val context = event.getApplicationContext();
    context.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach(this::logEndpoint);
  }

  private void logEndpoint(final RequestMappingInfo requestMappingInfo, final HandlerMethod handlerMethod) {
    log.info(requestMappingInfo.toString());
  }
}
