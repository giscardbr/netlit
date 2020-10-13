package br.com.netlit.accounts.domain.account.parent.service;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.editoradobrasil.totvs.netlit.stub.PEDIDOSNETLIT;
import br.com.editoradobrasil.totvs.netlit.stub.PEDIDOSNETLITSOAP;
import br.com.editoradobrasil.totvs.netlit.stub.RECEBEPV;
import br.com.netlit.LogMessagetHandler;
import br.com.netlit.accounts.domain.account.error.AccountNotFoundException;
import br.com.netlit.accounts.domain.account.error.AddressNotFoundException;
import br.com.netlit.accounts.domain.account.error.CPFAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountCPFException;
import br.com.netlit.accounts.domain.account.error.NullSubAccountPasswordException;
import br.com.netlit.accounts.domain.account.error.UserNotFoundException;
import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AccountStatus;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressType;
import br.com.netlit.accounts.domain.account.general.entity.SchoolEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.AddressRepository;
import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import br.com.netlit.accounts.domain.event.CreditCardRegistrationEvent;
import br.com.netlit.accounts.domain.event.ParentAdminCreationRequestEvent;
import br.com.netlit.accounts.domain.event.ParentUserCreationRequestEvent;
import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.model.OrderProduct;
import br.com.netlit.checkout.domain.checkout.model.OrderProductPK;
import br.com.netlit.checkout.domain.checkout.model.OrderStatus;
import br.com.netlit.checkout.domain.checkout.model.Product;
import br.com.netlit.checkout.domain.checkout.repository.OrderRepository;
import br.com.netlit.checkout.domain.checkout.service.ProductService;
import br.com.netlit.integration.util.IntegrationProtheus;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class ParentAdminWritingService {
	
	private static final Long LENDO_MUITO_READERS_PRODUCT = 2L; 
	private static final Integer LENDO_MUITO_PRODUCT_ID = 1; 
	private static final Long LENDO_ADOIDADO_READERS_PRODUCT = 4L; 
	private static final Integer LENDO_ADOIDADO_PRODUCT_ID = 2; 

	private final AccountRepository accountRepo;
	private final AddressRepository addressRepo;
	private final CredentialsService credentialsService;
	private final CredentialsRepository credentialsRepo;  
	private final ApplicationEventPublisher publisher;
	private final ObjectMapper mapper;
	private final UserRepository userRepository;
	private final SubAccountRepository subAccountRepository;
	private final OrderRepository orderRepository;
	
	@Autowired
    private ProductService productService;

	@Autowired
	private IntegrationProtheus integrationProtheus;

	public ParentAdminWritingService(
			final ApplicationEventPublisher publisher, 
			final AccountRepository accountRepo,
			final AddressRepository addressRepo, 
			final CredentialsService credentialsService, 
			final ObjectMapper mapper,
			final UserRepository userRepository, 
			final SubAccountRepository subAccountRepository,
			final OrderRepository orderRepository,
			final CredentialsRepository credentialsRepo
			
			) {

		this.publisher = publisher;
		this.accountRepo = accountRepo;
		this.addressRepo = addressRepo;
		this.credentialsService = credentialsService;
		this.mapper = mapper;
		this.userRepository = userRepository;
		this.subAccountRepository = subAccountRepository;
		this.orderRepository = orderRepository;
		this.credentialsRepo = credentialsRepo;
	}

	@Validated(OnAdminAccountCreation.class)
	public void create(final @Valid @NotNull ParentAdminCreationRequest accountRequest) {

		log.info("Creating parent account");
		UserEntity user = userRepository.findById(accountRequest.getEmail()).orElseThrow(UserNotFoundException::new);
		
		Order order = Order.builder()
				.dateTimeCreated(LocalDateTime.now())
				.email(user.getEmail())
				.status(OrderStatus.CREATED)
				.build();

		Product product = 
			productService.getProduct(accountRequest.getReaders().equals(LENDO_MUITO_READERS_PRODUCT) ? LENDO_MUITO_PRODUCT_ID : LENDO_MUITO_PRODUCT_ID );

		order.setOrderProducts(
				Arrays.asList(
						OrderProduct
							.builder()
							.pk(OrderProductPK.builder()
									.order(order)
									.product(product)
									.build())
							.quantity(1)
							.totalPrice(product.getPrice().multiply(BigDecimal.ONE))
							.build()
							)
				);
		
		order.setTotalPrice(order.calculateOrderAmount());
		
		orderRepository.save(order);

		val entity = new AccountEntity();
		entity.setType(AccountType.PARENT);
		entity.setName(accountRequest.getName());
		entity.setLastName(accountRequest.getLastName());
		entity.setBirthDate(accountRequest.getBirthDate());
		entity.setMobile(accountRequest.getMobile());
		entity.setGender(accountRequest.getGender());
		entity.setRg(accountRequest.getRg());
		entity.setCpf(accountRequest.getCpf());
		entity.setReaders(accountRequest.getReaders());
		entity.setCreated(LocalDate.now());
		entity.setEntityName(accountRequest.getEntityName());
		entity.setEntityNumber(accountRequest.getEntityNumber());
		entity.setTradingName(accountRequest.getTradingName());
		entity.setCnpj(accountRequest.getCnpj());
		entity.setBusiness(accountRequest.isBusiness());
		entity.setStatus(AccountStatus.ACTIVATED);

		val document = Optional.of(entity).map(AccountEntity::getCpf).orElseThrow(NullAccountCPFException::new);
		if (this.accountRepo.exists(entity.isBusiness(), document))
			throw new CPFAlreadyUsedException(document);

		this.accountRepo.save(entity);

		this.credentialsService.create(
				CredentialsCreationRequest
					.builder()
						.email(user.getEmail())
						.password(user.getPassword())
						.accountId(entity.getId())
					.build());

		val address = new AddressEntity();
		address.setAdditionalInformation(accountRequest.getAddress().getAdditionalInformation());
		address.setCity(accountRequest.getAddress().getCity());
		address.setDistrict(accountRequest.getAddress().getDistrict());
		address.setNumber(accountRequest.getAddress().getNumber());
		address.setPhone(accountRequest.getAddress().getPhone());
		address.setState(accountRequest.getAddress().getState());
		address.setStreet(accountRequest.getAddress().getStreet());
		address.setZip(accountRequest.getAddress().getZip());
		address.setType(AddressType.BILLING);
		address.setAccountId(entity.getId());
		this.addressRepo.save(address);

		val accountId = entity.getId();
		log.info("Created parent account " + accountId);
		accountRequest.getChildrenAccounts().stream()
			.map(request -> request.toBuilder().accountId(accountId).build())
				.map(childrenAccount -> new ParentUserCreationRequestEvent(this, childrenAccount))
				.forEach(this.publisher::publishEvent);

		accountRequest.getBilling().setOrderId(order.getId());
		this.publisher.publishEvent(new CreditCardRegistrationEvent(this, accountId, accountRequest.getEmail(), accountRequest.getBilling()));
		
		String recebepv = null;
		
		try {
			
			RECEBEPV resquest = integrationProtheus.buildRequest(accountRequest, order);
			
			PEDIDOSNETLIT pedidosnetlit = new PEDIDOSNETLIT(
					new URL(integrationProtheus.getWsdlLocation()),
					new QName("http://200.201.128.38:18238/", "PEDIDOSNETLIT"));
			HandlerResolver handlerResolver = new HandlerResolver() {
				
				@Override
				public List<Handler> getHandlerChain(PortInfo portInfo) {

					return Arrays.asList(new LogMessagetHandler());
				}
			};
			pedidosnetlit.setHandlerResolver(handlerResolver);
			
			PEDIDOSNETLITSOAP pedidosnetlitsoap = pedidosnetlit.getPEDIDOSNETLITSOAP();
			
			recebepv = pedidosnetlitsoap.recebepv(resquest.getDADOSREC(), resquest.getDADOSCLI());
			
			
		} catch (Exception e) {
			
			log.debug("Error call PROTHEUS WebService", e);
			
			recebepv = "ERROR";
		}
		
		order.setProtheusStatus(recebepv);
//		orderRepository.save(order);
		
	}

	public void update(final UUID accountId, final @Valid @NotNull AccountRequest accountRequest) {
		log.info("Updating parent account");

		List<ParentUserCreationRequest> lSubAccToCreate = new ArrayList<ParentUserCreationRequest>();
		
		accountRequest.getChildrenAccounts().stream()
				.forEach(sa -> {

					if (sa.getId() == null) {
						
						if (sa.getCredentialsCreationRequest().getPassword() == null)
							throw new NullSubAccountPasswordException("A senha deve ser informada. [" + sa.getCredentialsCreationRequest().getEmail() +"]");
						
						lSubAccToCreate.add(sa);

					} else {

						try {
							SubAccountEntity saEntity = subAccountRepository.findById(UUID.fromString(sa.getId()))
									.orElseThrow(AccountNotFoundException::new);
							saEntity.setBirthDate(sa.getBirthDate());
							saEntity.setGender(sa.getGender());
							saEntity.setLastName(sa.getLastName());
							saEntity.setName(sa.getName());

							SchoolEntity sEntity = new SchoolEntity();
							sEntity.setName(sa.getSchool().getName());
							sEntity.setYear(sa.getSchool().getYear());
							saEntity.setSchool(sEntity);
							saEntity.setType(AccountType.PARENT);
							subAccountRepository.save(saEntity);
							
							if (sa.getCredentialsCreationRequest().getPassword() != null) {
								UserEntity user = userRepository.findById(sa.getCredentialsCreationRequest().getEmail()).orElseThrow(UserNotFoundException::new);
								user.setPassword(sa.getCredentialsCreationRequest().getPassword());
								userRepository.save(user);
							}
							
						} catch (Exception e) {
							log.error("SubAccount não encontrada", e);
						}
					}

				});
		
		val entity = this.accountRepo.findById(accountId).orElseThrow(AccountNotFoundException::new);
		entity.setType(AccountType.PARENT);
		entity.setName(accountRequest.getName());
		entity.setLastName(accountRequest.getLastName());
		entity.setBirthDate(accountRequest.getBirthDate());
		entity.setMobile(accountRequest.getMobile());
		entity.setGender(accountRequest.getGender());
		entity.setRg(accountRequest.getRg());
		entity.setCpf(accountRequest.getCpf());
		entity.setEntityName(accountRequest.getEntityName());
		entity.setEntityNumber(accountRequest.getEntityNumber());
		entity.setTradingName(accountRequest.getTradingName());
		entity.setCnpj(accountRequest.getCnpj());
		entity.setBusiness(accountRequest.isBusiness());
		entity.setReaders(accountRequest.getReaders());
		entity.setStatus(AccountStatus.ACTIVATED);
		this.accountRepo.save(entity);

		val address = this.addressRepo.findBy(accountId, AddressType.BILLING).orElseThrow(AddressNotFoundException::new);
		address.setAdditionalInformation(accountRequest.getAddress().getAdditionalInformation());
		address.setCity(accountRequest.getAddress().getCity());
		address.setDistrict(accountRequest.getAddress().getDistrict());
		address.setNumber(accountRequest.getAddress().getNumber());
		address.setPhone(accountRequest.getAddress().getPhone());
		address.setState(accountRequest.getAddress().getState());
		address.setStreet(accountRequest.getAddress().getStreet());
		address.setZip(accountRequest.getAddress().getZip());
		address.setCityCode(accountRequest.getAddress().getCityCode());
		this.addressRepo.save(address);

		if (accountRequest.getCredentials().getPassword() != null) {
			UserEntity user = userRepository.findById(accountRequest.getCredentials().getEmail()).orElseThrow(UserNotFoundException::new);
			user.setPassword(accountRequest.getCredentials().getPassword());
			userRepository.save(user);
		}
		
		lSubAccToCreate.stream()
			.map(request -> request.toBuilder().accountId(accountId).build())
				.map(childrenAccount -> new ParentUserCreationRequestEvent(this, childrenAccount))
				.forEach(this.publisher::publishEvent);

		log.info("Updated parent account " + accountId);

	}

	@EventListener
	void create(final ParentAdminCreationRequestEvent event) throws JsonProcessingException {
		log.info("New parent admin request: " + this.mapper.writeValueAsString(event));
		val request = event.getRequest();
		this.create(request);
	}

	public void deactivate(String accountId) {

		UUID accountIdUUID = UUID.fromString(accountId);

		Optional<AccountEntity> accountEntity = accountRepo.findById(accountIdUUID);
		accountEntity.get().setStatus(AccountStatus.CANCELED);
		accountRepo.save(accountEntity.get());

		for (UserEntity userEntity : userRepository.findBy(accountIdUUID)) {
			userEntity.setEnabled(false);
			userRepository.save(userEntity);
		}

		PaginatedScanList<SubAccountEntity> saList = subAccountRepository.findBy(accountIdUUID);

		for (SubAccountEntity subAccountEntity : saList) {
			for (UserEntity userEntity : userRepository.findBy(subAccountEntity.getId())) {
				userEntity.setEnabled(false);
				userRepository.save(userEntity);
			}
		}
	}
}
