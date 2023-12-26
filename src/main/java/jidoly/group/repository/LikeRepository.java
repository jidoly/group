package jidoly.group.repository;

import jidoly.group.controller.group.LikeCountDto;
import jidoly.group.domain.Board;
import jidoly.group.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByBoardId(Long boardId);
    Optional<Like> findByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<Like> findByMemberIdAndClubId(Long memberId, Long clubId);

    @Query("SELECT l.club.id FROM Like l GROUP BY l.club.id ORDER BY COUNT(l) DESC")
    List<Long> findTop3ClubsByLikes();

    @Query("select l.club.id from Like l where l.member.id = :id")
    List<Long> findLongById(Long id);
}
