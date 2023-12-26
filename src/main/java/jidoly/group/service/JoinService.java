package jidoly.group.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.JoinRepository;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    
    /* create 할때도 넣어야됨 status = Manage */


    //가입 신청시
    @Transactional
    public Long applyJoin(Long memberId, Long clubId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + memberId));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));
        Join join = Join.createJoin(member, club);
        joinRepository.save(join);
        return join.getId();
    }

    //가입 승인
    public void acceptJoin(Member member, Club club) {
        /*권한 체크해야됨*/
        Join join = joinRepository.findByMemberIdAndClubId(member.getId(), club.getId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));

        join.agree();
    }

    //가입 거절
    @Transactional
    public void denyJoin(Long memberId, Long clubId) {
        /*권한 체크해야됨*/
        Join join = joinRepository.findByMemberIdAndClubId(memberId, clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));
        joinRepository.deleteById(join.getId());
    }

    //매니저 승격
    public void setManagerJoin(Member member, Club club) {
        /*권한 체크해야됨*/
        Join join = joinRepository.findByMemberIdAndClubId(member.getId(), club.getId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: "));
        join.setManager();
    }


}
