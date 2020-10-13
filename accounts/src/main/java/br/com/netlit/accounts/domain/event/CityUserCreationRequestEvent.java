package br.com.netlit.accounts.domain.event;

import br.com.netlit.accounts.domain.account.city.service.CityUserCreationRequest;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class CityUserCreationRequestEvent extends ApplicationEvent {

  private final CityUserCreationRequest request;

  public CityUserCreationRequestEvent(final Object source, final CityUserCreationRequest request) {
    super(source);
    this.request = request;
  }
}
