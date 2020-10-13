package br.com.netlit.library.domain.awards;


import br.com.netlit.library.domain.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Relation(value = "book", collectionRelation = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name"
})
public class Awards {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long uuid;

    @JsonProperty("name")
    private String name;

    @ManyToMany(mappedBy = "awards")
    @JsonIgnore
    private List<Book> book;
}
