package jidoly.group.repository;

import jidoly.group.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {


    @Override
    @EntityGraph(attributePaths = {"likes"})
    List<Board> findAll();


    @EntityGraph(attributePaths = {"likes"})
    Optional<Board> findWithLikeBoardById(long id);

    @EntityGraph(attributePaths = {"member", "club"})
    @Query("select b from Board b")
    List<Board> findBoardFetch();
}
