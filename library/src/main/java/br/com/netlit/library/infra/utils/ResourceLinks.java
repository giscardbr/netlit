package br.com.netlit.library.infra.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceLinks {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private final List<ResourceLink> links;

  private ResourceLinks(final List<ResourceLink> links) {
    this.links = links;
  }

  public static ResourceLinks of(final String endpoint, final String resources) {
    final List<ResourceLink> links = Stream.of(resources.split(LINE_SEPARATOR)).map(resource -> ResourceLink.of(endpoint, resource)).collect(Collectors.toList());
    return new ResourceLinks(links);
  }

  public Stream<ResourceLink> stream() {
    return this.links.stream();
  }
}
