package jidoly.group.repository;

import jidoly.group.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"member", "club"})
    List<Board> findAll();

    @EntityGraph(attributePaths = {"member", "club"})
    @Query("SELECT b FROM Board b ORDER BY b.LastModifiedDate DESC")
    List<Board> findAllByOrderByLastModifiedDateDesc();

    @EntityGraph(attributePaths = {"member", "club"})
    @Query("SELECT b FROM Board b WHERE b.club.id = :groupId ORDER BY b.LastModifiedDate DESC")
    List<Board> findAllByGroupId(@Param("groupId") Long groupId);


    @EntityGraph(attributePaths = {"member", "club"})
    Optional<Board> findFetchById(long id);

    @EntityGraph(attributePaths = {"member", "club"})
    @Query("select b from Board b")
    List<Board> findFetchBoard();
}
