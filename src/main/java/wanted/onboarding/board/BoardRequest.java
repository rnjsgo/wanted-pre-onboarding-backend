package wanted.onboarding.board;

import lombok.Getter;
import lombok.Setter;
import wanted.onboarding.user.User;

public class BoardRequest {
    @Getter @Setter
    public static class SaveBoardDTO{
        private String title;
        private String content;

        public Board toEntity(User user){
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Getter @Setter
    public static class BoardUpdateDTO{
        private int id;
        private String title;
        private String content;

        public Board toEntity(User user){
            return Board.builder()
                    .id((long)id)
                    .user(user)
                    .title(title)
                    .content(content)
                    .build();
        }
    }
}
