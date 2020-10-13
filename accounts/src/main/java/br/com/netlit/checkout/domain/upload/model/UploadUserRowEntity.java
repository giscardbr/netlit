package br.com.netlit.checkout.domain.upload.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "upload_user_record")
public class UploadUserRowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "upload_id")
	private Long uploadId;

    @Column(name = "account_id")
	private String accountId;

    @Column(name = "user_type")
	private UserType userType;

    @Column(name = "productId")
	private String productId;

	@Column(name = "name")
	private String name;

    @Column(name = "last_name")
	private String lastname;

    @Column(name = "login")
	private String login;

    @Column(name = "password")
	private String password;

    @Column(name = "year_class")
	private String yearClass;

    @Column(name = "status")
    private UploadStatus status;
    
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "imported_date")
    private LocalDateTime importedDate;

    @Column(name = "log")
    private String log;
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
		result = prime * result + ((yearClass == null) ? 0 : yearClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadUserRowEntity other = (UploadUserRowEntity) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (userType != other.userType)
			return false;
		if (yearClass == null) {
			if (other.yearClass != null)
				return false;
		} else if (!yearClass.equals(other.yearClass))
			return false;
		return true;
	}

}
