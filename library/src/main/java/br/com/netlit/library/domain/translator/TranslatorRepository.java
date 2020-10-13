package br.com.netlit.library.domain.translator;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslatorRepository extends PagingAndSortingRepository<Translator, String>, JpaSpecificationExecutor<Translator> {

}
