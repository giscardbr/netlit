package br.com.netlit.accounts.domain.account.parent.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.netlit.accounts.infra.utils.validation.CpfCnpj;

public class CPFTest {

	private static Validator validator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    validator = Validation.buildDefaultValidatorFactory().getValidator();
	    
	}
	
	@Test
	public void test() {
		
		CPFMock cpf = new CPFMock("09.309.266000195");
		Set<ConstraintViolation<CPFMock>> violations = validator.validate(cpf);
		assertThat(violations.size()).isEqualTo(0);
		
	}

	class CPFMock {
		
		@CpfCnpj
		private String cpfCnpj;

		CPFMock(String cpfCnpj){
			this.cpfCnpj = cpfCnpj;
		}
	}
}
