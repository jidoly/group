package jidoly.group.repository;

import jidoly.group.domain.Club;
import jidoly.group.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByClubName(String clubName);
}
