package wanted.onboarding.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {
    @Getter @Setter
    public static class JoinDTO{
        @NotEmpty
        @Pattern(regexp = ".*@.*", message = "이메일 형식에 @를 포함해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8 , message = "비밀번호는 8자 이상이여야 합니다.")
        private String password;

        private String username;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .build();
        }
    }

    @Getter @Setter
    public static class LoginDTO{
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }
}
