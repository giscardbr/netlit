package br.com.netlit.oauth2.domain.service;

import br.com.netlit.oauth2.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public DefaultUserDetailsService(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return this.userRepository.findById(username).map(User::new)
        .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
  }
}
