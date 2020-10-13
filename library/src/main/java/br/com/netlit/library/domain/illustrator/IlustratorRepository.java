package br.com.netlit.library.domain.illustrator;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IlustratorRepository extends PagingAndSortingRepository<Ilustrator, String>, JpaSpecificationExecutor<Ilustrator> {

}

