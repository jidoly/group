package jidoly.group.repository;

import jidoly.group.domain.Join;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinRepository extends JpaRepository<Join, Long> {

    Optional<Join> findByMemberIdAndClubId(Long memberId, Long clubId);
}
