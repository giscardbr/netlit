package br.com.netlit.accounts.domain.password;

import br.com.netlit.accounts.domain.account.error.NullAccountEmailException;
import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.domain.account.general.repository.CredentialsRepository;
import br.com.netlit.accounts.domain.account.general.repository.SubAccountRepository;
import br.com.netlit.accounts.domain.event.CredentialsUpdatedEvent;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static br.com.netlit.accounts.domain.account.general.entity.AccountRole.ADMIN;
import static java.lang.String.format;

@Log4j2
@Service
public class PasswordService {

  private final CredentialsRepository credentialsRepository;
  private final JavaMailSender mailSender;
  private final ApplicationEventPublisher publisher;
  private final SubAccountRepository subAccountRepository;

  public PasswordService(final JavaMailSender mailSender, final CredentialsRepository credentialsRepository, final SubAccountRepository subAccountRepository, final ApplicationEventPublisher publisher) {
    this.mailSender = mailSender;
    this.credentialsRepository = credentialsRepository;
    this.subAccountRepository = subAccountRepository;
    this.publisher = publisher;
  }

  @Async
  public void reset(@NonNull final String username) {
    log.info("It was requested a new password for user: {}", username);
    final Optional<CredentialsEntity> credentials = this.credentialsRepository.findById(username);
    if (!credentials.isPresent()) {
      log.info("There's no user: {}" + username);
    }
    credentials.ifPresent(this::reset);
  }

  public void reset(@NonNull final CredentialsEntity credentials) {
    final CharacterRule alphabetical = new CharacterRule(EnglishCharacterData.Alphabetical);
    final CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
    final PasswordGenerator passwordGenerator = new PasswordGenerator();
    final String password = passwordGenerator.generatePassword(8, alphabetical, digits);

    if (ADMIN.equals(credentials.getAccountRole())) {
      this.sendMail(credentials.getEmail(), credentials.getEmail(), password);
    } else {
      final UUID subAccountId = credentials.getAccountId();
      final String parentEmail = this.subAccountRepository
          .findById(subAccountId)
          .map(SubAccountEntity::getAccountId)
          .flatMap(this.credentialsRepository::findByAccountId)
          .map(CredentialsEntity::getEmail)
          .orElseThrow(NullAccountEmailException::new);
      this.sendMail(parentEmail, credentials.getEmail(), password);
    }

    this.publisher.publishEvent(
        new CredentialsUpdatedEvent(this, credentials.getAccountId(), credentials.getEmail(), password));
  }

  private void sendMail(final String to, final String username, final String password) {
    log.info("Sending new credentials of {} to {}", username, to);
    final SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Sua senha Netlit");
    message.setText(format("A senha Netlit do seu usuário %s é %s.", username, password));
    this.mailSender.send(message);
    log.info("Mail sent successfully");
  }
}


