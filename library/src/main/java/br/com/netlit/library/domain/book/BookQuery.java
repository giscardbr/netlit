package br.com.netlit.library.domain.book;

import br.com.netlit.library.domain.author.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookQuery {

  @Singular
  private List<String> years;
  @Singular
  private List<String> authors;
  @Singular
  private List<String> collections;
  @Singular
  private List<String> ageGroups;
  @Singular
  private List<String> segments;
  @Singular
  private List<String> guidingThemes;
  @Singular
  private List<String> subjects;
  @Singular
  private List<String> textualGenres;
  @Singular
  private List<String> specialDates;
  @Singular
  private List<String> crossCuttingThemes;
  @Singular
  private List<String> disciplinaryContents;
  private String title;
  private String highlight;

  public Specification<Book> toSpecification() {
    Specification<Book> specification = Specification.where(null);

    specification = this.singleSpecificationOf("year", this.years, specification);
    specification = this.joinSpecificationOf("author", "name", this.authors, specification);
    specification = this.singleSpecificationOf("collection", this.collections, specification);
    specification = this.singleSpecificationOf("ageGroup", this.ageGroups, specification);
    specification = this.singleSpecificationOf("segment", this.segments, specification);
    specification = this.singleSpecificationOf("guidingTheme", this.guidingThemes, specification);
    specification = this.collectionSpecificationOf("subjects", this.subjects, specification);
//    specification = this.collectionSpecificationOf("FALTA DEFINIR", this.textualGenres, specification);
    specification = this.collectionSpecificationOf("specialDates", this.specialDates, specification);
    specification = this.collectionSpecificationOf("crossCuttingThemes", this.crossCuttingThemes, specification);
    specification = this.collectionSpecificationOf("disciplinaryContents", this.disciplinaryContents, specification);
    specification = this.singleSpecificationOf("title", this.title, specification);
    specification = this.singleSpecificationOf("highlight", this.highlight, specification);

    return specification;
  }

  private Specification<Book> joinSpecificationOf(final String joinObject, final String field, final List<String> elements, final Specification<Book> specification) {
    if(isNull(elements) || elements.isEmpty())
      return specification;

    return specification.and((root, criteriaQuery, criteriaBuilder) -> {
      Join<Book, Author> join = root.join(joinObject);
      Path<String> fieldJoin = join.get(field);
      return criteriaBuilder.isTrue(fieldJoin.in(elements));
    });
  }

  private Specification<Book> collectionSpecificationOf(final String field, final List<String> elements, final Specification<Book> specification) {
    if(isNull(elements) || elements.isEmpty())
      return specification;

    return specification.and((root, criteriaQuery, criteriaBuilder) -> {
      final Predicate[] predicates = elements.stream()
              .map(element -> criteriaBuilder.isMember(element, root.get(field)))
              .toArray(Predicate[]::new);
      return criteriaBuilder.or(predicates);
    });
  }

  private Specification<Book> singleSpecificationOf(final String field, final List<String> elements, final Specification<Book> specification) {
    if(isNull(elements) || elements.isEmpty())
      return specification;

    return specification.and((root, criteriaQuery, criteriaBuilder) -> {
      final CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(field));
      for (final String element : elements) {
        in.value(element);
      }
      return in;
    });
  }

  private Specification<Book> singleSpecificationOf(final String field, final String element, final Specification<Book> specification) {
    if(isNull(element) || element.isEmpty())
      return specification;

    return specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(field), "%" + element + "%"));
  }

}
