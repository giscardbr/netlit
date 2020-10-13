package br.com.netlit.checkout.domain.upload.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import br.com.netlit.accounts.domain.account.error.EmailAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.SchoolEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.checkout.domain.upload.event.UploadRequestEvent;
import br.com.netlit.checkout.domain.upload.model.UploadEntity;
import br.com.netlit.checkout.domain.upload.model.UploadStatus;
import br.com.netlit.checkout.domain.upload.model.UploadUserRowEntity;
import br.com.netlit.checkout.domain.upload.model.UserType;
import br.com.netlit.checkout.domain.upload.repository.UploadRepository;
import br.com.netlit.checkout.domain.upload.repository.UploadUserRowRepository;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
@Log4j2
public class UploadService {

	private final ApplicationEventPublisher publisher;

	private final CredentialsService credentialsService;

	@Autowired
	private UploadRepository repository;

	@Autowired
	private UploadUserRowRepository uploadUserRowRepository;
	
	private final AccountRepository accountRepo;

	private final SubAccountRepository subAccountRepo;

	public UploadService(
			final ApplicationEventPublisher publisher,
			final UploadRepository repository,
			final UploadUserRowRepository uploadUserRowRepository,
			final CredentialsService credentialsService,
			final AccountRepository accountRepo,
			final SubAccountRepository subAccountRepo
			) {
		this.publisher = publisher;
		this.repository = repository;
		this.uploadUserRowRepository = uploadUserRowRepository;
		this.credentialsService = credentialsService;
		this.accountRepo = accountRepo;
		this.subAccountRepo = subAccountRepo;
	}

	public UploadEntity byId(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Upload not found"));
	}

	public UploadEntity save(UploadEntity entity) {
		return repository.save(entity);
	}

	@EventListener
	void create(final UploadRequestEvent event) throws IOException {

	    event.getEntity().setData(event.getFile().getBytes());

	    this.save(event.getEntity());
	    
	    this.publisher.publishEvent(new UploadProcessRequestEvent(this, event.getEntity().getId(), event.isSendMail(), event.isGeneratePassword()));
	
	}

	@EventListener
	void process(final UploadProcessRequestEvent event) throws IOException {
		
		UploadEntity entity = this.byId(event.getEntityId());
		
		InputStream is = new ByteArrayInputStream(entity.getData());
		Workbook workbook = new XSSFWorkbook(is);

		Sheet sheet = workbook.getSheetAt(0);

		List<UploadUserRowEntity> listUsers = new ArrayList<UploadUserRowEntity>();
		
		for (Iterator<Row> iterator = sheet.iterator(); iterator.hasNext();) {
			Row row = iterator.next();
			
			if (row.getRowNum() == 0) continue;
			if (row.getCell(0) == null) break;

			String givenPassword = "";
			Cell passwordCell = row.getCell(5);
			switch (passwordCell.getCellTypeEnum()) {
			case NUMERIC:
				
				Double d = new Double(passwordCell.getNumericCellValue());
				givenPassword = String.valueOf(d.intValue());
				break;

			case STRING:
				
				givenPassword = passwordCell.getStringCellValue();
				break;
			default:
				
				givenPassword = RandomString.make(8);
				break;
			}
			
			String passwd = event.isGeneratePassword() ? RandomString.make(8) : givenPassword;
			
			
			listUsers.add(UploadUserRowEntity.builder()
					
					.uploadId(entity.getId())
					.accountId(entity.getAccountId())
					.userType(UserType.valueOf(row.getCell(0).getStringCellValue().toUpperCase()))
					.productId(String.valueOf(row.getCell(1).getNumericCellValue()))
					.name(row.getCell(2).getStringCellValue().toUpperCase())
					.lastname(row.getCell(3).getStringCellValue().toUpperCase())
					.login(row.getCell(4).getStringCellValue().toLowerCase())
					.password(passwd)
					.yearClass(row.getCell(6).getStringCellValue().toUpperCase())
					
					.status(UploadStatus.WAIT)
					.importedDate(LocalDateTime.now())
					.build());
			
		}
		
		listUsers.stream().forEach(uploadUserRowRepository::save);
		
	}

	public void createSubAccountAndUser(UploadUserRowEntity uu) {

		try {
			
			log.info("Creating son account");
			AccountEntity account = accountRepo.findById(UUID.fromString(uu.getAccountId())).orElseThrow(EntityNotFoundException::new);
			
			val credentials = CredentialsCreationRequest.builder().email(uu.getLogin()).password(uu.getPassword()).build();
			val email = Optional.of(credentials.getEmail()).orElseThrow(NullAccountEmailException::new);
			if (this.credentialsService.itsUnavailable(email))
				throw new EmailAlreadyUsedException(email);

			val schoolEntity = new SchoolEntity();
			schoolEntity.setName(account.getTradingName());
			schoolEntity.setYear(uu.getYearClass().split("/")[0]);

			val entity = new SubAccountEntity();
			entity.setType(AccountType.valueOf(uu.getUserType()));
			entity.setName(uu.getName());
			entity.setLastName(uu.getLastname());
			entity.setBirthDate(null);
			entity.setGender(br.com.netlit.accounts.domain.account.general.entity.Gender.OTHERS);
			entity.setSchool(schoolEntity);
			entity.setAccountId(account.getId());
			this.subAccountRepo.save(entity);

			val credentialsRequest = credentials.toBuilder().accountId(entity.getId()).build();
			this.credentialsService.create(credentialsRequest);

			val accountId = entity.getId();
			log.info("Created son account " + accountId);

		}catch (Exception e) {
			
			uu.setLog(e.getMessage());
			uu.setStatus(UploadStatus.REJECTED);
			
			uploadUserRowRepository.save(uu);
			return;
		}
		
		uu.setCreated(LocalDateTime.now());
		uu.setStatus(UploadStatus.IMPORTED);
		
		uploadUserRowRepository.save(uu);
	}

	public List<UploadUserRowEntity> findById(Long uploadId) {
		return this.uploadUserRowRepository.findByUploadId(uploadId);
	}

	public void updateUploa1dUserRowEntity(@Valid UploadUserRowEntity entity) {
		entity.setStatus(UploadStatus.WAIT);
		entity.setLog("");
		this.uploadUserRowRepository.save(entity);
	}

	public void importUpload(Long uploadId) {

		List<UploadUserRowEntity> uploadUserRows = this.uploadUserRowRepository.findByUploadId(uploadId);
		
		uploadUserRows.stream().filter(uu -> UploadStatus.WAIT.equals(uu.getStatus())).forEach(this::createSubAccountAndUser);
		
	}

	public void deleteUploadUserRow(long uploadUserRowId) {
		
		UploadUserRowEntity entity = this.uploadUserRowRepository.findById(uploadUserRowId).orElseThrow(EntityNotFoundException::new);
		
		if (!UploadStatus.WAIT.equals(entity.getStatus()))
				throw new OperationNotAvailableException("Não é possível remover o registro.");
		
		this.uploadUserRowRepository.delete(entity);
		
	}
	
}