package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;

    //가입 신청시
    public Join applyJoin(Member member, Club club) {
        Join join = Join.createJoin(member, club);
        return joinRepository.save(join);
    }

    /**
     *파라미터로 받아서 중복 제거하는게 나을까?
     */
    //가입 승인
    public void acceptJoin(Member member, Club club) {
        Join join = joinRepository.findByMemberIdAndClubId(member.getId(), club.getId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));

        join.agree();
    }

    //가입 거절
    public void denyJoin(Member member, Club club) {
        Join join = joinRepository.findByMemberIdAndClubId(member.getId(), club.getId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));
        join.denied();
    }
    //매니저 승격
    public void setManagerJoin(Member member, Club club) {
        Join join = joinRepository.findByMemberIdAndClubId(member.getId(), club.getId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));
        join.setManager();
    }


}
