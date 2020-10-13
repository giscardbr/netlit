package br.com.netlit.library.domain.awards;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardsRepository extends PagingAndSortingRepository<Awards, String>, JpaSpecificationExecutor<Awards> {

}
