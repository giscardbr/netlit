package br.com.netlit.accounts.domain.account.parent.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TimeReadingCreationRequest {

  private Integer seconds;
  
}
