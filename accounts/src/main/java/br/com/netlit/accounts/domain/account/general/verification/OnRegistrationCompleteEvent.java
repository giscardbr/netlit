package br.com.netlit.accounts.domain.account.general.verification;

import org.springframework.context.ApplicationEvent;

import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import lombok.Value;

@Value
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private UserEntity user;
    
    private AccountType accountType;
 
    public OnRegistrationCompleteEvent(UserEntity user, AccountType accountType) {
        super(user);
         
        this.user = user;
        this.accountType = accountType;
    }
     
}