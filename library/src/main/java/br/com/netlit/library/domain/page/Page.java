package br.com.netlit.library.domain.page;

import br.com.netlit.library.domain.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.util.Optional;
import java.util.function.Consumer;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(value = "page", collectionRelation = "pages")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sequence",
    "content_link"
})
public class Page extends ResourceSupport {
  @Id
  @JsonIgnore
  @Column(name = "id")
  private String uuid;
  @JsonProperty("sequence")
  private Long sequence;
  @JsonProperty("content_link")
  private String contentLink;
  @OneToOne(cascade = CascadeType.ALL)
  @JsonIgnore
  @JoinColumn(name = "next_id")
  private Page next;
  @OneToOne(cascade = CascadeType.ALL)
  @JsonIgnore
  @JoinColumn(name = "previous_id")
  private Page previous;
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  public void ifHasNext(final Consumer<Page> consumer) {
    Optional.ofNullable(this.getNext()).ifPresent(consumer);
  }

  public void ifHasPrevious(final Consumer<Page> consumer) {
    Optional.ofNullable(this.getPrevious()).ifPresent(consumer);
  }
}

