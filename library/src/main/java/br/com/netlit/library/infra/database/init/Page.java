package br.com.netlit.library.infra.database.init;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "content_link",
    "sequence"
})
public class Page {

  @JsonProperty("id")
  private UUID id;
  @JsonProperty("content_link")
  private String contentLink;
  @JsonProperty("sequence")
  private Long sequence;

}
