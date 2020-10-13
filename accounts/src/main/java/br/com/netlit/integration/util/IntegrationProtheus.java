package br.com.netlit.integration.util;

import java.time.format.DateTimeFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.editoradobrasil.totvs.netlit.stub.ACLIENTES;
import br.com.editoradobrasil.totvs.netlit.stub.APEDIDOS;
import br.com.editoradobrasil.totvs.netlit.stub.ARRAYOFAPEDIDOS;
import br.com.editoradobrasil.totvs.netlit.stub.LISTAPEDIDOS;
import br.com.editoradobrasil.totvs.netlit.stub.RECEBEPV;
import br.com.netlit.accounts.domain.account.city.resource.CityAdminCreationHttpRequest;
import br.com.netlit.accounts.domain.account.general.address.AddressCreationRequest;
import br.com.netlit.accounts.domain.account.mock.school.SchoolAccountCreationRequest;
import br.com.netlit.accounts.domain.account.parent.service.ParentAdminCreationRequest;
import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.model.OrderProduct;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class IntegrationProtheus {
	
	private static final String COD_PAIS_VALUE = "01058";
	private static final String ISENTO_VALUE = "ISENTO";
	private static final String PESSOA_JURIDICA = "J";
	private static final String PESSOA_FISICA = "F";
	private static final String PROTHEUS_VALUE = "PROTHEUS";
	private static final String LOJA_CLI_VALUE = "01";
	private static final String LOJA_ENT_VALUE = "01";
	private static final String TIPO_PED_VALUE = "N";
	private static final String COD_PGTO_VALUE = "001";
	private static final String TES_VALUE = "501";
	private static final String TIP_CLI_VALUE = "F";
	private static final Long PROFESSOR_BONIFICADO_PRODUCT_ID = 7L;
	private static final Float PROFESSOR_BONIFICADO_PRICE = new Float(0.01);
	
	private String wsdlLocation;
	
	public IntegrationProtheus(
			@Value("${netlit.protheus.url}") final String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}
	
	public String getWsdlLocation() {
		return this.wsdlLocation;
	}

	public RECEBEPV buildRequest(ParentAdminCreationRequest req, Order order) 
			throws IllegalAccessException {
		AddressCreationRequest address = req.getAddress();
		StringUtils.normalizeObject(AddressCreationRequest.class, address);

		StringUtils.normalizeObject(req.getClass(), req);
		
		ACLIENTES clientInfo = new ACLIENTES();
		clientInfo.setBAIRRO(req.getAddress().getDistrict());
		clientInfo.setCEP(req.getAddress().getZip().replaceAll("[^\\d]", ""));
		clientInfo.setCGC(req.getCpf().replaceAll("[^\\d]", ""));
		clientInfo.setRG(req.getRg().replaceAll("[^\\d]", ""));
		String phone = address.getPhone().replaceAll("[^\\d]", "");
		clientInfo.setDDD("0" + phone.substring(0, 2));
		clientInfo.setDTNASC(req.getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE));
		clientInfo.setEMAIL(req.getEmail());
		clientInfo.setENDERECO(String.format("%s%s%s", address.getStreet(), (address.getNumber() != null ? ", " + address.getNumber() : ""), (address.getAdditionalInformation() != null ? ", " + address.getAdditionalInformation() : "")));
		clientInfo.setESTADO(address.getState());
		clientInfo.setINSCR(null == req.getEntityNumber() ? ISENTO_VALUE : req.getEntityNumber());
		clientInfo.setMUNICIPIO(address.getCity());
		clientInfo.setCODMUN(address.getCityCode());
		clientInfo.setNOME(req.getName() + " " + req.getLastName());
		clientInfo.setNREDUZ(req.getName());
		clientInfo.setPESSOA(PESSOA_FISICA);
		clientInfo.setTELEFONE(phone.substring(2));
		clientInfo.setNATUREZA(PROTHEUS_VALUE);

		clientInfo.setTIPO(TIP_CLI_VALUE);
		clientInfo.setCODPAIS(COD_PAIS_VALUE);
		clientInfo.setCODIGO(PROTHEUS_VALUE);
		clientInfo.setLOJA(LOJA_CLI_VALUE);
		
		LISTAPEDIDOS orders = new LISTAPEDIDOS();
		ARRAYOFAPEDIDOS arrayOrders = new ARRAYOFAPEDIDOS();
		
		int count = 1;
		for (OrderProduct orderProduct : order.getOrderProducts()) {
			APEDIDOS orderReq = new APEDIDOS();
			orderReq.setITEM(String.format("%02d", count++));
			orderReq.setNUMNLIT(order.getId().toString());
			orderReq.setPRCVEN(Float.valueOf(orderProduct.getProduct().getPrice().toString()));
			orderReq.setPRODUTO(orderProduct.getProduct().getProtheusId());
			orderReq.setPRUNIT(Float.valueOf(orderProduct.getProduct().getPrice().toString()));
			orderReq.setQTDVEN(orderProduct.getQuantity());
			orderReq.setTES(TES_VALUE);
			
			orderReq.setVALOR(Float.valueOf(orderProduct.getTotalPrice().toString()));
			arrayOrders.getAPEDIDOS().add(orderReq);
			
		}
		
		orders.setAITENS(arrayOrders);
		orders.setLOJACLI(LOJA_CLI_VALUE);
		orders.setLOJAENT(LOJA_ENT_VALUE);
		orders.setTIPO(TIPO_PED_VALUE);
		orders.setCONDPAG(COD_PGTO_VALUE);
		orders.setCLIENTE(PROTHEUS_VALUE);
		orders.setNUMNLIT(order.getId().toString());
		

		RECEBEPV request = new RECEBEPV();
		request.setDADOSCLI(clientInfo);
		request.setDADOSREC(orders);
		return request;
	}
	
	public RECEBEPV buildRequest(SchoolAccountCreationRequest req, Order order)
			throws IllegalAccessException {
		AddressCreationRequest address = req.getAddress();
		StringUtils.normalizeObject(AddressCreationRequest.class, address);

		StringUtils.normalizeObject(req.getClass(), req);
		
		ACLIENTES clientInfo = new ACLIENTES();
		clientInfo.setBAIRRO(req.getAddress().getDistrict());
		clientInfo.setCEP(req.getAddress().getZip().replaceAll("[^\\d]", ""));
		clientInfo.setCGC(req.getCnpj().replaceAll("[^\\d]", ""));
		clientInfo.setRG(PROTHEUS_VALUE);
		String phone = address.getPhone().replaceAll("[^\\d]", "");
		clientInfo.setDDD("0" + phone.substring(0, 2));
		clientInfo.setDTNASC(req.getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE));
		clientInfo.setEMAIL(req.getEmail());
		clientInfo.setENDERECO(String.format("%s%s%s", address.getStreet(), (address.getNumber() != null ? ", " + address.getNumber() : ""), (address.getAdditionalInformation() != null ? ", " + address.getAdditionalInformation() : "")));
		clientInfo.setESTADO(address.getState());
		clientInfo.setINSCR(null == req.getEntityNumber() ? ISENTO_VALUE : req.getEntityNumber());
		clientInfo.setMUNICIPIO(address.getCity());
		clientInfo.setCODMUN(address.getCityCode());
		clientInfo.setNOME(req.getName() + " " + req.getLastName());
		clientInfo.setNREDUZ(req.getName());
		clientInfo.setPESSOA(PESSOA_JURIDICA);
		clientInfo.setTELEFONE(phone.substring(2));
		clientInfo.setNATUREZA(PROTHEUS_VALUE);

		clientInfo.setTIPO(TIP_CLI_VALUE);
		clientInfo.setCODPAIS(COD_PAIS_VALUE);
		clientInfo.setCODIGO(PROTHEUS_VALUE);
		clientInfo.setLOJA(LOJA_CLI_VALUE);
		
		LISTAPEDIDOS orders = new LISTAPEDIDOS();
		ARRAYOFAPEDIDOS arrayOrders = new ARRAYOFAPEDIDOS();
		
		int count = 1;
		for (OrderProduct op : order.getOrderProducts()) {
			APEDIDOS orderReq = new APEDIDOS();
			orderReq.setITEM(String.format("%02d", count++));
			orderReq.setNUMNLIT(order.getId().toString());
			orderReq.setPRODUTO(op.getProduct().getProtheusId());
			orderReq.setQTDVEN(op.getQuantity());
			orderReq.setTES(TES_VALUE);
			
			if (op.getProduct().getId().equals(PROFESSOR_BONIFICADO_PRODUCT_ID)) {
				orderReq.setPRUNIT(PROFESSOR_BONIFICADO_PRICE);
				orderReq.setPRCVEN(PROFESSOR_BONIFICADO_PRICE);
				orderReq.setVALOR(PROFESSOR_BONIFICADO_PRICE * op.getQuantity());
			}else {
				orderReq.setPRUNIT(Float.valueOf(op.getProduct().getPrice().toString()));
				orderReq.setPRCVEN(Float.valueOf(op.getProduct().getPrice().toString()));
				orderReq.setVALOR(Float.valueOf(op.getTotalPrice().toString()));
			}
			
			arrayOrders.getAPEDIDOS().add(orderReq);
			
		}
		
		orders.setAITENS(arrayOrders);
		orders.setLOJACLI(LOJA_CLI_VALUE);
		orders.setLOJAENT(LOJA_ENT_VALUE);
		orders.setTIPO(TIPO_PED_VALUE);
		orders.setCONDPAG(COD_PGTO_VALUE);
		orders.setCLIENTE(PROTHEUS_VALUE);
		orders.setNUMNLIT(order.getId().toString());
		

		RECEBEPV request = new RECEBEPV();
		request.setDADOSCLI(clientInfo);
		request.setDADOSREC(orders);
		return request;
	}

	public RECEBEPV buildRequest(@Valid @NotNull CityAdminCreationHttpRequest req, Order order) throws IllegalAccessException {
		AddressCreationRequest address = req.getAddress();
		StringUtils.normalizeObject(AddressCreationRequest.class, address);

		StringUtils.normalizeObject(req.getClass(), req);
		
		ACLIENTES clientInfo = new ACLIENTES();
		clientInfo.setBAIRRO(req.getAddress().getDistrict());
		clientInfo.setCEP(req.getAddress().getZip().replaceAll("[^\\d]", ""));
		clientInfo.setCGC(req.getCnpj().replaceAll("[^\\d]", ""));
		clientInfo.setRG(PROTHEUS_VALUE);
		String phone = address.getPhone().replaceAll("[^\\d]", "");
		clientInfo.setDDD("0" + phone.substring(0, 2));
		clientInfo.setDTNASC(req.getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE));
		clientInfo.setEMAIL(req.getEmail());
		clientInfo.setENDERECO(String.format("%s%s%s", address.getStreet(), (address.getNumber() != null ? ", " + address.getNumber() : ""), (address.getAdditionalInformation() != null ? ", " + address.getAdditionalInformation() : "")));
		clientInfo.setESTADO(address.getState());
		clientInfo.setINSCR(null == req.getEntityNumber() ? ISENTO_VALUE : req.getEntityNumber());
		clientInfo.setMUNICIPIO(address.getCity());
		clientInfo.setCODMUN(address.getCityCode());
		clientInfo.setNOME(req.getName() + " " + req.getLastName());
		clientInfo.setNREDUZ(req.getName());
		clientInfo.setPESSOA(PESSOA_JURIDICA);
		clientInfo.setTELEFONE(phone.substring(2));
		clientInfo.setNATUREZA(PROTHEUS_VALUE);

		clientInfo.setTIPO(TIP_CLI_VALUE);
		clientInfo.setCODPAIS(COD_PAIS_VALUE);
		clientInfo.setCODIGO(PROTHEUS_VALUE);
		clientInfo.setLOJA(LOJA_CLI_VALUE);
		
		LISTAPEDIDOS orders = new LISTAPEDIDOS();
		ARRAYOFAPEDIDOS arrayOrders = new ARRAYOFAPEDIDOS();
		
		int count = 1;
		for (OrderProduct op : order.getOrderProducts()) {
			APEDIDOS orderReq = new APEDIDOS();
			orderReq.setITEM(String.format("%02d", count++));
			orderReq.setNUMNLIT(order.getId().toString());
			orderReq.setPRODUTO(op.getProduct().getProtheusId());
			orderReq.setQTDVEN(op.getQuantity());
			orderReq.setTES(TES_VALUE);
			
			if (op.getProduct().getId().equals(PROFESSOR_BONIFICADO_PRODUCT_ID)) {
				orderReq.setPRUNIT(PROFESSOR_BONIFICADO_PRICE);
				orderReq.setPRCVEN(PROFESSOR_BONIFICADO_PRICE);
				orderReq.setVALOR(PROFESSOR_BONIFICADO_PRICE * op.getQuantity());
			}else {
				orderReq.setPRUNIT(Float.valueOf(op.getProduct().getPrice().toString()));
				orderReq.setPRCVEN(Float.valueOf(op.getProduct().getPrice().toString()));
				orderReq.setVALOR(Float.valueOf(op.getTotalPrice().toString()));
			}
			
			arrayOrders.getAPEDIDOS().add(orderReq);
			
		}
		
		orders.setAITENS(arrayOrders);
		orders.setLOJACLI(LOJA_CLI_VALUE);
		orders.setLOJAENT(LOJA_ENT_VALUE);
		orders.setTIPO(TIPO_PED_VALUE);
		orders.setCONDPAG(COD_PGTO_VALUE);
		orders.setCLIENTE(PROTHEUS_VALUE);
		orders.setNUMNLIT(order.getId().toString());
		

		RECEBEPV request = new RECEBEPV();
		request.setDADOSCLI(clientInfo);
		request.setDADOSREC(orders);
		return request;
	}

}
