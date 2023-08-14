package wanted.onboarding.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wanted.onboarding.user.User;
import wanted.onboarding.user.UserJPARepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardJPARepositoryTest {
    @Autowired
    private BoardJPARepository boardJPARepository;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private EntityManager em;

    public String title1 = "게시물1";
    public String title2 = "게시물2";

    @BeforeEach
    public void setUp(){
        User user = User.builder()
                .email("rnjsgo@naver.com")
                .password("1234")
                .build();

        userJPARepository.save(user);

        Board board1 = Board.builder()
                .user(user)
                .title(title1)
                .build();

        Board board2 = Board.builder()
                .user(user)
                .title(title2)
                .build();

        boardJPARepository.save(board1);
        boardJPARepository.save(board2);

        em.clear();
    }

    @Test
    @DisplayName("모든 게시물 불러오기 테스트")
    public void findAll_test(){
        //given -> @BeforeEach setUp()

        //when
        System.out.println("쿼리1"); //lazy loading 확인용
        List<Board> boards = boardJPARepository.findAll();

        //eye
        System.out.println("첫번째 게시물 제목 : " + boards.get(0).getTitle());
        System.out.println("두번째 게시물 제목 : " + boards.get(1).getTitle());

        //then
        System.out.println("쿼리2"); //lazy loading 확인용
        Assertions.assertThat(boards.get(0).getUser().getEmail()).isEqualTo("rnjsgo@naver.com");
        Assertions.assertThat(boards.get(0).getTitle()).isEqualTo(title1);
        Assertions.assertThat(boards.get(1).getTitle()).isEqualTo(title2);
    }

    @Test
    @DisplayName("게시물 id로 게시물 불러오기 테스트")
    public void findById_test(){
        //given -> @BeforeEach setUp()
        Long boardId = (long)1;

        //when
        Board findBoard= boardJPARepository.findById(boardId).orElseThrow(
                ()-> new RuntimeException("해당 게시물을 찾을 수 없습니다.")
        );

        //eye
        System.out.println("찾은 게시물 제목 : " + findBoard.getTitle());

        //then
        Assertions.assertThat(findBoard.getTitle()).isEqualTo(title1);
        Assertions.assertThat(findBoard.getId()).isEqualTo(boardId);
    }
}
