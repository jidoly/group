package jidoly.group.repository;

import jidoly.group.domain.Board;
import jidoly.group.domain.Club;
import jidoly.group.domain.Like;
import jidoly.group.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

//    List<Club> findAllByOrderByLastModifiedDateDesc();
    Optional<Club> findByClubName(String clubName);
    boolean existsByClubName(String groupName);

    @Override
    @Query("SELECT c FROM Club c ORDER BY c.LastModifiedDate DESC")
    List<Club> findAll();

}
