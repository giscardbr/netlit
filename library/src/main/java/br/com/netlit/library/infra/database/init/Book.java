package br.com.netlit.library.infra.database.init;

import br.com.netlit.library.domain.adapter.Adapter;
import br.com.netlit.library.domain.author.Author;
import br.com.netlit.library.domain.awards.Awards;
import br.com.netlit.library.domain.illustrator.Ilustrator;
import br.com.netlit.library.domain.translator.Translator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "year",
    "pages",
    "author",
    "cover_image_link",
    "trailer_link",
    "title",
    "isbn",
    "ebsa",
    "review",
    "illustrator",
    "segment",
    "age_group",
    "guiding_theme",
    "disciplines",
    "subjects",
    "transversal_themes"
})
public class Book {

  @JsonProperty("id")
  private UUID id;
  @JsonProperty("age_group")
  private String ageGroup;
  @JsonProperty("author")
  private List<Author> author = new ArrayList<>();;
  @JsonProperty("collection")
  private String collection;
  @JsonProperty("cover_image_link")
  private String coverImageLink;
  @JsonProperty("trailer_link")
  private String trailerLink;
  @JsonProperty("disciplines")
  private List<String> disciplines = new ArrayList<>();
  @JsonProperty("ebsa")
  private String ebsa;
  @JsonProperty("guiding_theme")
  private String guidingTheme;
  @JsonProperty("illustrator")
  private List<Ilustrator> illustrator = new ArrayList<>();;
  @JsonProperty("isbn")
  private String isbn;
  @JsonProperty("pages")
  private List<Page> pages = new ArrayList<>();
  @JsonProperty("review")
  private String review;
  @JsonProperty("segment")
  private String segment;
  @JsonProperty("subjects")
  private List<String> subjects = new ArrayList<>();
  @JsonProperty("translator")
  private List<Translator> translator = new ArrayList<>();
  @JsonProperty("title")
  private String title;
  @JsonProperty("transversal_themes")
  private List<String> transversalThemes = new ArrayList<>();
  @JsonProperty("year")
  private String year;
  @JsonProperty("highlight")
  private String highlight;
  @JsonProperty("special_dates")
  private List<String> specialDates = new ArrayList<>();
  @JsonProperty("awards")
  private List<Awards> awards = new ArrayList<>();
  @JsonProperty("awards")
  private List<Adapter> adapters = new ArrayList<>();

}
