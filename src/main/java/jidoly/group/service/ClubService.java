package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.*;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    public Club findById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));
        return club;
    }

    @Transactional
    public Long createClub(Long userId,Club club) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        ;
        Join join = Join.createJoin(member, club);
        join.setManager();
        club.addJoins(join);
        clubRepository.save(club);
        return club.getId();
    }
    @Transactional
    public Long changeClubNameOrInfo(Long clubId, String newClubName, String newInfo) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        // 예를 들어, 중복 체크, 유효성 검사
        if (clubRepository.findByClubName(newClubName).isPresent()) {
            throw new RuntimeException("Club name already exists: " + newClubName);
        }

        club.updateClubNameAndInfo(newClubName,newInfo);
        return club.getId();
    }

    /* 처음 눌렀을때 -> 좋아요 저장, true 반환 / 두번째 -> 좋아요 취소, false반환 */
    @Transactional
    public boolean likeClub(Long memberId, Long clubId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        //좋아요 중복 금지 - Exist하면, 다시 눌렀을떄 취소 하는걸로
        Optional<Like> likeExist = likeRepository.findByMemberIdAndClubId(memberId, clubId);
        if (likeExist.isPresent()) {
            Long likeId = likeExist.get().getId();
            club.removeLike(member);
            likeRepository.deleteById(likeId);
            return false;
        } else {
            club.addLike(member);
            clubRepository.save(club);
            return true;
        }
    }

}
