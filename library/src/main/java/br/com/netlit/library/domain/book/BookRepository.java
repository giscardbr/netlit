package br.com.netlit.library.domain.book;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, String>, JpaSpecificationExecutor<Book> {

    Book findBookByFileBookName(String fileBookName);

    List<Book> findBooksByUuidIn(List<String> uuids);
    List<Book> findBooksByAgeGroupContains(String year);

    @Query("SELECT DISTINCT(b.year) FROM Book b ORDER BY b.year")
    List<String> findYearBooks();

    @Query("SELECT DISTINCT(b.collection) FROM Book b ORDER BY b.collection")
    List<String> findCollectionsBooks();

    @Query("SELECT DISTINCT(b.ageGroup) FROM Book b ORDER BY b.ageGroup")
    List<String> findAgeGroupBooks();

    @Query("SELECT DISTINCT(b.segment) FROM Book b ORDER BY b.segment")
    List<String> findSegmentBooks();

    @Query("SELECT DISTINCT(b.guidingTheme) FROM Book b ORDER BY b.guidingTheme")
    List<String> findGuidingThemeBooks();

    @Query("SELECT DISTINCT(s) FROM Book b JOIN b.subjects s ORDER BY s")
    List<String> findSubjectsBooks();

    @Query("SELECT DISTINCT(c) FROM Book b JOIN b.crossCuttingThemes c ORDER BY c")
    List<String> findCrossCuttingThemesBooks();

    @Query("SELECT DISTINCT(s) FROM Book b JOIN b.specialDates s ORDER BY s")
    List<String> findSpecialDatesBooks();

    @Query("SELECT DISTINCT(d) FROM Book b JOIN b.disciplinaryContents d ORDER BY d")
    List<String> findDisciplinaryContentsBooks();

}
