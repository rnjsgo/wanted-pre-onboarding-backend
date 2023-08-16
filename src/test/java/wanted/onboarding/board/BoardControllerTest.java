package wanted.onboarding.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wanted.onboarding.security.SecurityConfig;

@Import({
        SecurityConfig.class
})
@WebMvcTest(controllers = {BoardController.class})
public class BoardControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @WithMockUser(username = "rnjsgo@naver.com")
    @DisplayName("게시물 작성 테스트")
    @Test // URL: "/board/save"
    public void saveBoard_test() throws Exception {
        //given
        BoardRequest.SaveBoardDTO board = new BoardRequest.SaveBoardDTO();
        board.setTitle("게시물 1");
        board.setContent("1번 게시물 입니다");

        String requestBody = om.writeValueAsString(board);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/board/save")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();

        System.out.println("테스트 : "+requestBody);
        System.out.println("테스트 : "+responseBody);

        //then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response").isEmpty());
    }
}
