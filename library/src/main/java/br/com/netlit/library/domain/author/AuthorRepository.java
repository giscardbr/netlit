package br.com.netlit.library.domain.author;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, String>, JpaSpecificationExecutor<Author> {

    @Query("SELECT DISTINCT(a.name) FROM Author a ORDER BY a.name")
    List<String> findDistinctAuthors();
}

