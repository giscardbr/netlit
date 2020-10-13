package br.com.netlit.accounts.domain.event;

import br.com.netlit.accounts.domain.account.parent.resource.ParentAdminCreationHttpRequest;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminCreationRequest;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class ParentAdminCreationRequestEvent extends ApplicationEvent {

  private final ParentAdminCreationRequest request;

  public ParentAdminCreationRequestEvent(final Object source, final ParentAdminCreationHttpRequest request) {
    super(source);
    this.request = ParentAdminCreationRequest.valueOf(request);
  }
}
