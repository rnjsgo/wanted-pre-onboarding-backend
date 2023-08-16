package wanted.onboarding.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wanted.onboarding.errors.Exception.Exception400;
import wanted.onboarding.user.User;
import wanted.onboarding.user.UserJPARepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user=userJPARepository.findByEmail(username)
                 .orElseThrow( ()-> new Exception400("회원 정보가 일치하지 않습니다."));
        return new CustomUserDetails(user);
    }
}
