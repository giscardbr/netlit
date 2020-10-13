package br.com.netlit.library.domain.filter;

import br.com.netlit.library.domain.author.AuthorRepository;
import br.com.netlit.library.domain.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class FilterResource {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Filter getFilters() {
        return Filter.builder()
                .authors(authorRepository.findDistinctAuthors())
                .year(bookRepository.findYearBooks())
                .collections(bookRepository.findCollectionsBooks())
                .ageGroups(bookRepository.findAgeGroupBooks())
                .segments(bookRepository.findSegmentBooks())
                .guidingThemes(bookRepository.findGuidingThemeBooks())
                .subjects(bookRepository.findSubjectsBooks())
                .specialDates(bookRepository.findSpecialDatesBooks())
                .crossCuttingThemes(bookRepository.findCrossCuttingThemesBooks())
                .disciplinaryContents(bookRepository.findDisciplinaryContentsBooks())
                .build();
    }


}
