package wanted.onboarding.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardJPARepository extends JpaRepository<Board, Long> {
    List<Board> findAll();
    Optional<Board> findById(int id);

}
