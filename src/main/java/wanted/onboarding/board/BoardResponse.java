package wanted.onboarding.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResponse {
    @Getter @Setter
    public static class GetBoardsDTO {
        private Long id;
        private String userName;
        private String title;
        private String content;

        public GetBoardsDTO(Board board) {
            this.id = board.getId();
            this.userName = board.getUser().getUsername();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    @Getter @Setter
    public static class GetBoardByIdDTO {
        private Long id;
        private String userName;
        private String title;
        private String content;

        public GetBoardByIdDTO(Board board) {
            this.id = board.getId();
            this.userName = board.getUser().getUsername();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
