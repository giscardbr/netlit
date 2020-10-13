package br.com.netlit.library.domain.page;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends CrudRepository<Page, String> {

  Optional<Page> findByBookUuidAndSequence(String uuid, Long sequence);

  long countByBookUuid(String uuid);

}
