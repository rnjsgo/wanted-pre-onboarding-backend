package wanted.onboarding.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.errors.Exception.Exception403;
import wanted.onboarding.errors.Exception.Exception404;
import wanted.onboarding.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardJPARepository boardJPARepository;

    // 게시물 작성
    public void saveBoard(BoardRequest.SaveBoardDTO saveBoardDTO, User sessionUser){
        boardJPARepository.save(saveBoardDTO.toEntity(sessionUser));
    }

    // 게시물 조회 (페이지)
    public List<BoardResponse.GetBoardsDTO> getBoards(int page){
        Pageable pageable = PageRequest.of(page,9);

        Page<Board> pageContent = boardJPARepository.findAll(pageable);
        List<BoardResponse.GetBoardsDTO> responseDTOs = pageContent.getContent().stream()
                .map(board -> new BoardResponse.GetBoardsDTO(board))
                .collect(Collectors.toList());

        return responseDTOs;
    }

    // 게시물 조회 (id)
    public BoardResponse.GetBoardByIdDTO getBoardById(int id){
        Board findBoard = boardJPARepository.findById(id).orElseThrow(
                () -> new Exception404("해당 게시물을 찾을 수 없습니다 : "+id)
        );
        return new BoardResponse.GetBoardByIdDTO(findBoard);
    }

    // 게시물 수정
    public void updateBoard(BoardRequest.BoardUpdateDTO boardUpdateDTO , User sessionUser){
        Board findBoard = boardJPARepository.findById(boardUpdateDTO.getId()).orElseThrow(
                () -> new Exception404("해당 게시물을 찾을 수 없습니다 : "+ boardUpdateDTO.getId())
        );

        if(!findBoard.getUser().getId().equals(sessionUser.getId()))
            throw new Exception403("게시물 수정 권한이 없습니다");

        findBoard.update(boardUpdateDTO.getTitle(),boardUpdateDTO.getContent()); // dirty checking
    }

    // 게시물 삭제
    public void deleteBoard(int id , User sessionUser){
        Board findBoard = boardJPARepository.findById(id).orElseThrow(
                () -> new Exception404("해당 게시물을 찾을 수 없습니다 : "+ id)
        );

        if(!findBoard.getUser().getId().equals(sessionUser.getId()))
            throw new Exception403("게시물 삭제 권한이 없습니다");

        boardJPARepository.delete(findBoard);
    }

}


