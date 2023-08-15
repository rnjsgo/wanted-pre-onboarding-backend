package wanted.onboarding.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.errors.Exception.Exception400;
import wanted.onboarding.errors.Exception.Exception500;
import wanted.onboarding.security.CustomUserDetails;
import wanted.onboarding.security.JWTProvider;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final JWTProvider jwtProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        try {
            userJPARepository.save(requestDTO.toEntity());
        } catch (Exception e) {
            throw new Exception500("unknown server error");
        }
    }

    @Transactional
    public String login(UserRequest.LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        CustomUserDetails customUserDetails = (CustomUserDetails) authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtProvider.create(customUserDetails.getUser());
    }



    //이메일 중복체크
    public void sameCheckEmail(String email) {
        Optional<User> userOP = userJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new Exception400("동일한 이메일이 존재합니다 : " + email);
        }
    }

}
