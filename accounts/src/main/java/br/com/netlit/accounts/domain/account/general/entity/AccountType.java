package br.com.netlit.accounts.domain.account.general.entity;

import br.com.netlit.checkout.domain.upload.model.UserType;

public enum AccountType {

	SCHOOL, LIBRARY, CITY, PARENT, STUDENT, TEACHER, TEACHER_FREE_ACCOUNT;

	public static AccountType valueOf(UserType userType) {
		switch (userType) {
		case ALUNO:
			
			return STUDENT;

		case BIBLIOTECA:
			
			return LIBRARY;

		case PROFESSOR:
			
			return TEACHER;

		case PROFESSOR_BONIFICADO:
			
			return TEACHER_FREE_ACCOUNT;

		case SALA_DE_AULA:
			
			return SCHOOL;
		}
		
		return null;
	}
}
