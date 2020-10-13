package br.com.netlit.library.domain.book;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.library.domain.adapter.Adapter;
import br.com.netlit.library.domain.author.Author;
import br.com.netlit.library.domain.awards.Awards;
import br.com.netlit.library.domain.illustrator.Ilustrator;
import br.com.netlit.library.domain.page.Page;
import br.com.netlit.library.domain.translator.Translator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Relation(value = "book", collectionRelation = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"uuid",
    "title",
    "author",
    "cover_image_link",
    "trailer_link",
    "file_name",
    "isbn",
    "ebsa_code",
    "review",
    "translation",
    "illustrator",
    "collection",
    "disciplinary_contents",
    "subjects",
    "translator",
    "segment",
    "year",
    "age_group",
    "cross_cutting_themes",
    "special_dates",
    "awards",
    "guiding_theme",
    "percentage_read",
    "adapter",
    "file_book_name",
    "number_pages",
    "supplement"
})
public class Book extends ResourceSupport {

  @Id
  @Column(name = "id")
  private String uuid;

  @JsonProperty("age_group")
  private String ageGroup;

  @JoinTable(name = "book_author", catalog = "library", joinColumns = {
          @JoinColumn(name = "book_id", nullable = false, updatable = false) },
          inverseJoinColumns = { @JoinColumn(name = "author_id",
                  nullable = false, updatable = false) })
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonProperty("author")
  private Set<Author> author;

  @JsonProperty("collection")
  private String collection;

  @ElementCollection
  @JsonProperty("cross_cutting_themes")
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<String> crossCuttingThemes;

  @ElementCollection
  @JsonProperty("disciplinary_contents")
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<String> disciplinaryContents;

  @JsonProperty("ebsa_code")
  private String ebsaCode;

  @JsonProperty("guiding_theme")
  private String guidingTheme;

  @JoinTable(name = "book_illustrator", catalog = "library", joinColumns = {
          @JoinColumn(name = "book_id", referencedColumnName="id") },
          inverseJoinColumns = { @JoinColumn(name = "illustrator_id",
                  referencedColumnName="id") })
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonProperty("illustrator")
  private Set<Ilustrator> illustrator;

  @JsonProperty("isbn")
  private String isbn;

  @Column(length = 4000)
  @JsonProperty("review")
  private String review;

  @JsonProperty("segment")
  private String segment;

  @ElementCollection
  @JsonProperty("subjects")
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<String> subjects;

  @JsonProperty("title")
  private String title;

  @JoinTable(name = "book_translator", catalog = "library", joinColumns = {
          @JoinColumn(name = "book_id", referencedColumnName="id") },
          inverseJoinColumns = { @JoinColumn(name = "translator_id",
                  referencedColumnName="id") })
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonProperty("translator")
  private Set<Translator> translator;

  @JsonProperty("year")
  private String year;

  @JsonProperty("cover_image_link")
  private String coverImageLink;

  @JsonProperty("trailer_link")
  private String trailerLink;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "first_page_id")
  private Page firstPage;

  @Transient
  @JsonProperty("percentage_read")
  private Double percentageRead;

  @JsonIgnore
  private String highlight;

  @ElementCollection
  @JsonProperty("special_dates")
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<String> specialDates;

  @JoinTable(name = "book_awards", catalog = "library", joinColumns = {
          @JoinColumn(name = "book_id", referencedColumnName="id") },
          inverseJoinColumns = { @JoinColumn(name = "awards_id",
                  referencedColumnName="id") })
  @ElementCollection
  @JsonProperty("awards")
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Awards> awards;

  @JoinTable(name = "book_adapters", catalog = "library", joinColumns = {
          @JoinColumn(name = "book_id", referencedColumnName="id") },
          inverseJoinColumns = { @JoinColumn(name = "adapters_id",
                  referencedColumnName="id") })
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonProperty("adapter")
  private Set<Adapter> adapters;

  @JsonProperty("book_link")
  private String bookLink;

  @JsonProperty("rating")
  private int rating;

  @JsonProperty("number_pages")
  private Long numberOfPages;

  @JsonProperty("file_book_name")
  private String fileBookName;

  @JsonProperty("supplement")
  private String supplement;

  public Set<Awards> getAwards() {
    return this.awards == null || this.awards.isEmpty() ? null : this.awards;
  }

  public Set<String> getSpecialDates() {
    return this.specialDates == null || this.specialDates.isEmpty() ? null : this.specialDates;
  }

  public Set<String> getCrossCuttingThemes() {
    return this.crossCuttingThemes == null || this.crossCuttingThemes.isEmpty() ? null : this.crossCuttingThemes;
  }

  public Set<String> getDisciplinaryContents() {
    return this.disciplinaryContents == null || this.disciplinaryContents.isEmpty() ? null : this.disciplinaryContents;
  }

  public Set<String> getSubjects() {
    return this.subjects == null || this.subjects.isEmpty() ? null : this.subjects;
  }
}