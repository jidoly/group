package jidoly.group.repository;

import jidoly.group.domain.Board;
import jidoly.group.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByBoardId(Long boardId);
    Optional<Like> findByMemberIdAndBoardId(Long memberId, Long boardId);

}
