package wanted.onboarding.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import wanted.MyRestDoc;
import wanted.onboarding.user.User;
import wanted.onboarding.user.UserJPARepository;

import javax.persistence.EntityManager;
import java.util.Optional;
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@SpringBootTest
public class BoardControllerTest extends MyRestDoc {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private BoardJPARepository boardJPARepository;
    @Autowired
    private EntityManager em;
    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE board_tb AUTO_INCREMENT = 1").executeUpdate();
        User user = userJPARepository.save(User.builder()
                .email("rnjsgo@naver.com")
                .password("12345678")
                .build()
        );
        User anonymousUser = userJPARepository.save(User.builder()
                .email("anonymous@naver.com")
                .password("12345678")
                .build()
        );
        boardJPARepository.save(Board.builder()
                        .title("1번 게시물")
                        .content("1번 게시물 입니다")
                        .user(user)
                        .build());

        boardJPARepository.save(Board.builder()
                .title("2번 게시물")
                .content("2번 게시물 입니다")
                .user(user)
                .build());
        em.clear();
    }
    @WithUserDetails(value = "rnjsgo@naver.com",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물 작성 테스트")
    @Test // URL: "/board/save"
    @Transactional
    public void saveBoard_test() throws Exception {
        //given
        BoardRequest.SaveBoardDTO board = new BoardRequest.SaveBoardDTO();
        board.setTitle("게시물 제목");
        board.setContent("게시물 내용 입니다");

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
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @DisplayName("게시물 목록 조회 테스트 (page)")
    @Test // URL: "/boards"
    @Transactional
    public void getBoards_test() throws Exception {
        //given
        String page = "0";

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .get("/boards")
                        .param("page", page)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();


        System.out.println("테스트 : "+responseBody);

        //then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].title").value("1번 게시물"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].title").value("2번 게시물"));
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @DisplayName("게시물 조회 테스트 (id)")
    @Test // URL: "/boards/{id}"
    @Transactional
    public void getBoardById_test() throws Exception {
        //given
        int id = 1;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .get("/boards/"+id)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();


        System.out.println("테스트 : "+responseBody);

        //then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("1번 게시물"));
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "rnjsgo@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물 수정 테스트")
    @Test // URL: "/board/update"
    @Transactional
    public void updateBoard_test() throws Exception {
        //given
        BoardRequest.BoardUpdateDTO board = new BoardRequest.BoardUpdateDTO();
        board.setId(1);
        board.setTitle("1번 게시물 제목 수정");
        board.setContent("1번 게시물 내용을 수정합니다");

        String requestBody = om.writeValueAsString(board);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/board/update")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();

        System.out.println("테스트 : "+requestBody);
        System.out.println("테스트 : "+responseBody);

        //then
        Board updatedBoard = boardJPARepository.findById((long)1).get();
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        Assertions.assertThat(updatedBoard.getTitle()).isEqualTo("1번 게시물 제목 수정");
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "anonymous@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물 수정 실패 테스트 (작성자가 아닌 경우)")
    @Test // URL: "/board/update"
    @Transactional
    public void updateBoardFailure_test() throws Exception {
        //given
        BoardRequest.BoardUpdateDTO board = new BoardRequest.BoardUpdateDTO();
        board.setId(1);
        board.setTitle("1번 게시물 제목 수정");
        board.setContent("1번 게시물 내용을 수정합니다");

        String requestBody = om.writeValueAsString(board);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/board/update")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();

        System.out.println("테스트 : "+requestBody);
        System.out.println("테스트 : "+responseBody);

        //then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(403));
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "rnjsgo@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물 삭제 테스트")
    @Test // URL: "/board/{id} (DELETE)"
    @Transactional
    public void deleteBoard_test() throws Exception {
        //given
        long id = 1;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/board/"+id)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        //then
        Optional<Board> deletedBoard = boardJPARepository.findById(id);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        Assertions.assertThat(deletedBoard).isEmpty();
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "anonymous@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물 삭제 실패 테스트 (작성자가 아닌 경우)")
    @Test // URL: "/board/{id} (DELETE)"
    @Transactional
    public void deleteBoardFailure_test() throws Exception {
        //given
        long id = 1;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/board/"+id)
        );

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        //then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(403));
        result.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
