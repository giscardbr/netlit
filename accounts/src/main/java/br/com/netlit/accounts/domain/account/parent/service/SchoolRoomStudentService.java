package br.com.netlit.accounts.domain.account.parent.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.error.AccountNotFoundException;
import br.com.netlit.accounts.domain.account.error.SchoolGradeRoomNotFoundException;
import br.com.netlit.accounts.domain.account.general.entity.SchoolRoomStudentEntity;
import br.com.netlit.accounts.domain.account.general.repository.SchoolGradeRoomRepository;
import br.com.netlit.accounts.domain.account.general.repository.SchoolRoomStudentRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class SchoolRoomStudentService {
	
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

	public SchoolRoomStudentService() {}
	
	public void save(final @Valid @NotNull SchoolRoomStudentEntity entity) {
		
		// check if account exists
		this.subAccountRepository.findById(entity.getSubAccountId()).orElseThrow(AccountNotFoundException::new);

		// check if gradeRoom exists
		this.gradeRoomRepository.findById(entity.getSchoolGradeRoomId()).orElseThrow(SchoolGradeRoomNotFoundException::new);

		this.roomStudentRepository.save(entity);
	}

	public void update(final UUID id, final @Valid @NotNull SchoolRoomStudentEntity newEntityValues) {

	}

	public void remove(SchoolRoomStudentEntity entity) {
		
		List<SchoolRoomStudentEntity> result = this.roomStudentRepository.findBy(entity.getSchoolGradeRoomId()).stream().filter(e -> e.getSubAccountId().equals(entity.getSubAccountId())).collect(Collectors.toList());
		
		this.roomStudentRepository.delete(result);
	}
	
	public PaginatedScanList<SchoolRoomStudentEntity> findBy(final UUID schoolGradeRoomId) {
		
		return this.roomStudentRepository.findBy(schoolGradeRoomId);
	}

	public PaginatedScanList<SchoolRoomStudentEntity> findBySubAccountId(final UUID subAccountId) {
		
		return this.roomStudentRepository.findBySubAccountId(subAccountId);
	}
}
