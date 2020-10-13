package br.com.netlit.checkout.domain.upload.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.netlit.checkout.domain.upload.model.UploadUserRowEntity;

public interface UploadUserRowRepository extends CrudRepository<UploadUserRowEntity, Long> {
	
	@Query("select e from UploadUserRowEntity e where e.uploadId = ?1")
	List<UploadUserRowEntity> findByUploadId(Long uploadId);

}
