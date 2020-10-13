package br.com.netlit.accounts.domain.event;

import org.springframework.context.ApplicationEvent;

import br.com.netlit.accounts.domain.account.city.resource.CityAdminCreationHttpRequest;
import lombok.Value;

@Value
public class CityAdminCreationRequestEvent extends ApplicationEvent {

  private final CityAdminCreationHttpRequest request;

  public CityAdminCreationRequestEvent(final Object source, final CityAdminCreationHttpRequest request) {
    super(source);
    this.request = request;
  }
}
