package wanted.onboarding.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserJPARepositoryTest {
    @Autowired
    private UserJPARepository userJPARepository;

    @Test
    @DisplayName("이메일로 아이디 찾기 테스트")
    public void findByEmail_test(){
        //given
        String email = "rnjsgo@naver.com";
        User savedUser = User.builder()
                .email(email)
                .password("1234")
                .build();
        userJPARepository.save(savedUser);

        //when
        User findUser = userJPARepository.findByEmail(email).orElseThrow(
                ()-> new RuntimeException("해당 이메일을 찾을 수 없습니다.")
        );

        //eye
        System.out.println("찾은 유저의 이메일 : " + findUser.getEmail());

        //then
        Assertions.assertThat(findUser).isEqualTo(savedUser);
    }
}
