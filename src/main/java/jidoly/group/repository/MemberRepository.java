package jidoly.group.repository;

import jidoly.group.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

}
