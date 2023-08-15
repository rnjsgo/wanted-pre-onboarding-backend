package wanted.onboarding.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wanted.onboarding.errors.GlobalExceptionHandler;
import wanted.onboarding.security.JWTProvider;
import wanted.onboarding.security.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;

@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})
@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("회원가입 테스트")
    public void join_test() throws Exception {
        // given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setEmail("rnjsgo@naver.com");
        requestDTO.setPassword("12345678");
        requestDTO.setUsername("권해");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void login_test() throws Exception {
        // given
        UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
        loginDTO.setEmail("rnjsgo@naver.com");
        loginDTO.setPassword("12345678");
        User user = User.builder().id((long)1).build();
        String requestBody = om.writeValueAsString(loginDTO);

        // stub
        String jwt = JWTProvider.create(user);
        Mockito.when(userService.login(any())).thenReturn(jwt);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        String responseHeader = result.andReturn().getResponse().getHeader(JWTProvider.HEADER);
        System.out.println("테스트 : "+responseBody);
        System.out.println("테스트 : "+responseHeader);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        Assertions.assertTrue(jwt.startsWith(JWTProvider.TOKEN_PREFIX));
    }
}