package jidoly.group.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;


    //가입 신청시
    @Transactional
    public Long applyJoin(Join join) {
        Optional<Join> exist = joinRepository.findByMemberIdAndClubId(join.getMember().getId(), join.getClub().getId());
        if (!exist.isPresent()) {
            joinRepository.save(join);
            return join.getId();
        } else {
            throw new EntityExistsException("이미 존재하는 요청입니다 id: " + exist.get().getId());
        }
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
