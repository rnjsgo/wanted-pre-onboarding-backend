package wanted.onboarding.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wanted.onboarding.user.User;
import wanted.onboarding.user.UserJPARepository;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=userJPARepository.findByEmail(username);
        if(user.isEmpty())
            return null;
        return new CustomUserDetails(user.get());
    }
}
