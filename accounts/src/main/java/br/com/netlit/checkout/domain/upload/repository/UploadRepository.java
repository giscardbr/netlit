package br.com.netlit.checkout.domain.upload.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.netlit.checkout.domain.upload.model.UploadEntity;

public interface UploadRepository extends CrudRepository<UploadEntity, Long> {

}
