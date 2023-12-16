package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Board;
import jidoly.group.domain.Club;
import jidoly.group.domain.Like;
import jidoly.group.domain.Member;
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
    public Long createClub(Club club) {
        if (clubRepository.findByClubName(club.getClubName()).isPresent()) {
            throw new RuntimeException("name already exists"); // 여기 MVC에서 message로 대체
        }
        clubRepository.save(club);
        return club.getId();
    }
    @Transactional
    public Long changeClubNameOrInfo(Long clubId, String newClubName, String newInfo) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        // 클럽 이름 변경 로직 또는 규칙 적용 필요 MVC시 @Valided랑 같이 고민해보자
        // 예를 들어, 중복 체크, 유효성 검사
        if (clubRepository.findByClubName(newClubName).isPresent()) {
            throw new RuntimeException("Club name already exists: " + newClubName);
        }

        club.updateClubNameAndInfo(newClubName,newInfo);
        return club.getId();
    }

    @Transactional
    public void likeClub(Long memberId, Long clubId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        //좋아요 중복 금지 - Exist하면, 다시 눌렀을떄 취소 하는걸로
        Optional<Like> likeExist = likeRepository.findByMemberIdAndClubId(memberId, clubId);
        if (likeExist.isPresent()) {
            Long likeId = likeExist.get().getId();
            likeRepository.deleteById(likeId);
        } else {
            likeRepository.save(Like.addLikeClub(member, club));
        }
    }

}
