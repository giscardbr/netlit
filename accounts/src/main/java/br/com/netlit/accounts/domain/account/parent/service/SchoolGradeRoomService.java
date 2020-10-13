package br.com.netlit.accounts.domain.account.parent.service;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.AccountNotFoundException;
import br.com.netlit.accounts.domain.account.error.SchoolGradeRoomAlreadyExistsException;
import br.com.netlit.accounts.domain.account.error.SchoolGradeRoomNotFoundException;
import br.com.netlit.accounts.domain.account.general.entity.SchoolGradeRoomEntity;
import br.com.netlit.accounts.domain.account.general.repository.SchoolGradeRoomRepository;
import br.com.netlit.accounts.domain.account.general.repository.SchoolRoomStudentRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class SchoolGradeRoomService {
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private SchoolGradeRoomRepository gradeRoomRepository;

	@Autowired
	private SchoolRoomStudentRepository roomStudentRepository;

	@Autowired
	private SubAccountRepository subAccountRepository;

	public SchoolGradeRoomService() {}
	
	public SchoolGradeRoomEntity findById(final UUID id) {
		
		return this.gradeRoomRepository.findById(id).orElseThrow(SchoolGradeRoomNotFoundException::new);
		
	}

	public void save(final @Valid @NotNull SchoolGradeRoomEntity entity) {
		
		// check if account exists
		this.subAccountRepository.findById(entity.getSubAccountId()).orElseThrow(AccountNotFoundException::new);

		// check if code exists
		PaginatedScanList<SchoolGradeRoomEntity> result = this.gradeRoomRepository.findBy(entity.getCode());
		if (result.size() != 0) throw new SchoolGradeRoomAlreadyExistsException();
		

		this.gradeRoomRepository.save(entity);
	}

	public void update(final UUID id, final @Valid @NotNull SchoolGradeRoomEntity newEntityValues) {
		log.info("Updating ");

		// check if account exists
		this.subAccountRepository.findById(newEntityValues.getSubAccountId()).orElseThrow(AccountNotFoundException::new);

		SchoolGradeRoomEntity entity = this.findById(id);
			
		entity.setCode(newEntityValues.getCode());
		entity.setName(newEntityValues.getName());
		entity.setSubAccountId(newEntityValues.getSubAccountId());
		
		this.gradeRoomRepository.save(entity);
		
		log.info("Updated " + newEntityValues);

	}

	public void remove(UUID id) {

		SchoolGradeRoomEntity entity = this.findById(id);
		
		this.gradeRoomRepository.delete(entity);

	}
	
	public PaginatedScanList<SchoolGradeRoomEntity> findBy(final UUID subAccountId) {
		return this.gradeRoomRepository.findBy(subAccountId);
	}
}
