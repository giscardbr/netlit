package br.com.netlit.accounts.domain.event;

import br.com.netlit.accounts.domain.account.parent.service.ParentUserCreationRequest;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class ParentUserCreationRequestEvent extends ApplicationEvent {

  private final ParentUserCreationRequest request;

  public ParentUserCreationRequestEvent(final Object source, final ParentUserCreationRequest request) {
    super(source);
    this.request = request;
  }
}
