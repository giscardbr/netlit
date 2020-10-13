package br.com.netlit.accounts.domain.account.parent.service;

import br.com.netlit.accounts.domain.account.general.OnAdminAccountCreation;
import br.com.netlit.accounts.domain.account.general.OnUserAccountCreation;
import br.com.netlit.accounts.domain.account.general.credentials.CredentialsCreationRequest;
import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.domain.account.parent.resource.ParentUserHttpRequest;
import br.com.netlit.accounts.domain.account.parent.resource.School;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ParentUserCreationRequest {

	private final String id;
	private final @Null(groups = OnAdminAccountCreation.class) @NotNull(groups = OnUserAccountCreation.class) UUID accountId;
	private @Past @NotNull LocalDate birthDate;
	private final @Valid CredentialsCreationRequest credentialsCreationRequest;
	private @NotNull Gender gender;
	private @NotEmpty String lastName;
	private @NotEmpty String name;
	private @NotNull @Valid School school;
	private LocalDateTime created;
	private AccountType type;

	public static ParentUserCreationRequest valueOf(final @Valid @NotNull ParentUserHttpRequest account) {
		final CredentialsCreationRequest credentialsCreationRequest = CredentialsCreationRequest
				.valueOf(account.getCredentials());
	    return builder()
	            .name(account.getName())
	            .lastName(account.getLastName())
	            .birthDate(account.getBirthDate())
	            .gender(account.getGender())
	            .school(account.getSchool())
	            .credentialsCreationRequest(credentialsCreationRequest)
	            .id(account.getId())
	            .type(account.getType())
	            .created(account.getCreated())
	            .build();
	}
	
	public static ParentUserCreationRequest valueOf(final @Valid @NotNull SubAccountEntity entity, final @Valid @NotNull CredentialsEntity credentials) {
	    return valueOf(entity, credentials, null); 
	}

	public static ParentUserCreationRequest valueOf(SubAccountEntity entity, CredentialsEntity credentials,
			UserEntity user) {
		return builder()
	            .name(entity.getName())
	            .lastName(entity.getLastName())
	            .birthDate(entity.getBirthDate())
	            .gender(entity.getGender())
	            .school(School.builder().name(entity.getSchool().getName()).year(entity.getSchool().getYear()).build())
	            .credentialsCreationRequest(CredentialsCreationRequest.builder().accountId(credentials.getAccountId()).email(credentials.getEmail()).password(user != null ? user.getPassword() : "").build())
	            .id(entity.getId().toString())
	            .type(entity.getType())
	            .created(entity.getCreated())
	            .build();
	}
	
}
