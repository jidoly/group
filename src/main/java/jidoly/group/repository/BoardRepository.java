package jidoly.group.repository;

import jidoly.group.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @Override
    @EntityGraph(attributePaths = {"likes"}) // 파일 만들어지면 파일이랑 같이
    List<Board> findAll();


    @EntityGraph(attributePaths = {"likes"}) // 파일 만들어지면 파일이랑 같이
    Optional<Board> findWithLikeBoardById(long id);

}
