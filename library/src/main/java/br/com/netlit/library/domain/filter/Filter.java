package br.com.netlit.library.domain.filter;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Filter {
    private List<String> year;
    private List<String> authors;
    private List<String> collections;
    private List<String> ageGroups;
    private List<String> segments;
    private List<String> guidingThemes;
    private List<String> subjects;
    private List<String> textualGenre;
    private List<String> specialDates;
    private List<String> crossCuttingThemes;
    private List<String> disciplinaryContents;
}
