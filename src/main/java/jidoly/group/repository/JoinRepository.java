package jidoly.group.repository;

import jidoly.group.domain.Join;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JoinRepository extends JpaRepository<Join, Long> {

    Optional<Join> findByMemberIdAndClubId(Long memberId, Long clubId);

//    @Query("SELECT j FROM Join j LEFT JOIN FETCH j.club c WHERE j.member.id = :userId AND (j.status = 'JOINED' OR j.status = 'MANAGED')")
    @Query("SELECT j FROM Join j WHERE j.member.id = :userId AND (j.status = 'JOINED' OR j.status = 'MANAGED')")
    List<Join> findMyGroups(@Param("userId") Long userId);

}
