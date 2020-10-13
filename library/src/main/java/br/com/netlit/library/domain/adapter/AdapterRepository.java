package br.com.netlit.library.domain.adapter;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdapterRepository extends PagingAndSortingRepository<Adapter, String>, JpaSpecificationExecutor<Adapter> {

}

