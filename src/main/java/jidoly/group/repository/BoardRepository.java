package jidoly.group.repository;

import jidoly.group.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.likes")
//    List<Board> findAllWithLikes();
//
//    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.likes")
//    List<Board> findBoardWithLikes();

}
