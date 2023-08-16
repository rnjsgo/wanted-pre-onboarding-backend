package wanted.onboarding.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import wanted.onboarding.Utils.ApiUtils;
import wanted.onboarding.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    // 게시물 작성
    @PostMapping("/board/save")
    public ResponseEntity<?> saveBoard(@RequestBody @Valid BoardRequest.SaveBoardDTO requestDTO, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.saveBoard(requestDTO, userDetails.getUser());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

    // 게시물 목록 조회 (page)
    @GetMapping("/boards")
    public ResponseEntity<?> getBoards(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<BoardResponse.GetBoardsDTO> responseDTOs = boardService.getBoards(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    // 게시물 조회 (id)
    @GetMapping("/boards/{id}")
    public ResponseEntity<?> getBoard(@PathVariable int id) {
        BoardResponse.GetBoardByIdDTO responseDTO = boardService.getBoardById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

    // 게시물 수정
    @PostMapping("/board/update")
    public ResponseEntity<?> update(@RequestBody @Valid BoardRequest.BoardUpdateDTO boardUpdateDTO, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.updateBoard(boardUpdateDTO, userDetails.getUser());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

    // 게시물 삭제
    @DeleteMapping("/board/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable int id , @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }
}