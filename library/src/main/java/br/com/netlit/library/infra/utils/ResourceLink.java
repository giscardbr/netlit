package br.com.netlit.library.infra.utils;

import lombok.Getter;

import static java.lang.String.format;

public class ResourceLink {

  @Getter
  private final String identifier;
  private final String resource;

  private ResourceLink(final String identifier, final String resource) {
    this.identifier = identifier;
    this.resource = resource;
  }

  public static ResourceLink of(final String endpoint, final String resource) {

    final String identifier = resource.replace(format("%s%s/", HttpRequest.getBaseUrl(), endpoint), "");
    return new ResourceLink(identifier, resource);
  }
}
