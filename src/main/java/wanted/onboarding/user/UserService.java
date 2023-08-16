package wanted.onboarding.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.errors.Exception.Exception400;
import wanted.onboarding.errors.Exception.Exception401;
import wanted.onboarding.errors.Exception.Exception500;
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
        User user = userJPARepository.findByEmail(loginDTO.getEmail())
                .filter(it -> passwordEncoder.matches(loginDTO.getPassword(), it.getPassword()))
                .orElseThrow(() -> new Exception401("회원 정보가 일치하지 않습니다."));
        return jwtProvider.create(user);
    }



    //이메일 중복체크
    public void sameCheckEmail(String email) {
        Optional<User> userOP = userJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new Exception400("동일한 이메일이 존재합니다 : " + email);
        }
    }

}
