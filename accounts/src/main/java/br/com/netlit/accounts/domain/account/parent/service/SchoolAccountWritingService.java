package br.com.netlit.accounts.domain.account.parent.service;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.editoradobrasil.totvs.netlit.stub.PEDIDOSNETLIT;
import br.com.editoradobrasil.totvs.netlit.stub.PEDIDOSNETLITSOAP;
import br.com.editoradobrasil.totvs.netlit.stub.RECEBEPV;
import br.com.netlit.LogMessagetHandler;
import br.com.netlit.accounts.domain.account.error.CNPJAlreadyUsedException;
import br.com.netlit.accounts.domain.account.error.NullAccountCNPJException;
import br.com.netlit.accounts.domain.account.error.UserNotFoundException;
import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsService;
import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.AccountStatus;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressType;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.general.repository.AccountRepository;
import br.com.netlit.accounts.domain.account.general.repository.AddressRepository;
import br.com.netlit.accounts.domain.account.general.repository.UserRepository;
import br.com.netlit.accounts.domain.account.mock.school.SchoolAccountCreationRequest;
import br.com.netlit.accounts.domain.event.CreditCardRegistrationEvent;
import br.com.netlit.accounts.domain.event.SchoolAccountCreationRequestEvent;
import br.com.netlit.accounts.infra.utils.validation.CpfCnpjValidator;
import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.repository.OrderRepository;
import br.com.netlit.integration.util.IntegrationProtheus;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Validated
public class SchoolAccountWritingService {

	private final AccountRepository accountRepo;
	private final AddressRepository addressRepo;
	private final CredentialsService credentialsService;
	
	private final ApplicationEventPublisher publisher;
	private final ObjectMapper mapper;

	private final UserRepository userRepository;
	
	private final OrderRepository orderRepository;

	@Autowired
	private IntegrationProtheus integrationProtheus;
	
	public SchoolAccountWritingService(
			final ApplicationEventPublisher publisher, 
			final AccountRepository accountRepo,
			final AddressRepository addressRepo, 
			final CredentialsService credentialsService,
			final ObjectMapper mapper,
			final UserRepository userRepository,
			final OrderRepository orderRepository
			) {

		this.publisher = publisher;
		this.accountRepo = accountRepo;
		this.addressRepo = addressRepo;
		this.credentialsService = credentialsService;
		this.mapper = mapper;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@Validated(OnAdminAccountCreation.class)
	public void create(final @Valid @NotNull SchoolAccountCreationRequest request) {

		log.info("Creating school account");
		UserEntity user = userRepository.findById(request.getEmail()).orElseThrow(UserNotFoundException::new);

		val entity = new AccountEntity();
		entity.setType(AccountType.SCHOOL);
		entity.setName(request.getName());
		entity.setLastName(request.getLastName());
		entity.setBirthDate(request.getBirthDate());
		entity.setGender(request.getGender());
		entity.setCreated(LocalDate.now());
		entity.setEntityName(request.getEntityName());
		entity.setEntityNumber(request.getEntityNumber());
		entity.setTradingName(request.getTradingName());
		entity.setCnpj(CpfCnpjValidator.format(request.getCnpj()));
		entity.setBusiness(true);
		entity.setStatus(AccountStatus.ACTIVATED);

		val document = Optional.of(entity).map(AccountEntity::getCnpj).orElseThrow(NullAccountCNPJException::new);
		if (this.accountRepo.exists(entity.isBusiness(), document))
			throw new CNPJAlreadyUsedException(document);

		this.accountRepo.save(entity);

		val address = new AddressEntity();
		address.setAdditionalInformation(request.getAddress().getAdditionalInformation());
		address.setCity(request.getAddress().getCity());
		address.setDistrict(request.getAddress().getDistrict());
		address.setNumber(request.getAddress().getNumber());
		address.setPhone(request.getAddress().getPhone());
		address.setState(request.getAddress().getState());
		address.setStreet(request.getAddress().getStreet());
		address.setZip(request.getAddress().getZip());
		address.setType(AddressType.BILLING);
		address.setAccountId(entity.getId());
		address.setCityCode(request.getAddress().getCityCode());
		this.addressRepo.save(address);

		this.credentialsService.create(
				CredentialsCreationRequest.builder()
					.email(request.getEmail())
					.password(user.getPassword())
					.accountId(entity.getId())
				.build());

		val accountId = entity.getId();

		this.publisher.publishEvent(new CreditCardRegistrationEvent(
				this, 
				accountId, 
				request.getEmail(), 
				request.getBilling()));
		
		Order order = orderRepository.findByEmailAndOrderId(request.getEmail(), request.getBilling().getOrderId());
		
		String recebepv = null;
		
		try {
			
			RECEBEPV resquest = integrationProtheus.buildRequest(request, order);
			
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
	
	@EventListener
	void create(final SchoolAccountCreationRequestEvent event) throws JsonProcessingException {
		log.info("New schoool account request: " + this.mapper.writeValueAsString(event));
		val request = event.getRequest();
		this.create(request);
	}

}
