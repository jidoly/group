package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Club;
import jidoly.group.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public Club createClub(String name, String info) {
        if (clubRepository.findByClubName(name).isPresent()) {
            throw new RuntimeException("name already exists"); // 여기 MVC에서 message로 대체
        }

        Club club = Club.createClub(name, info);
        return clubRepository.save(club);
    }
    @Transactional
    public void changeClubNameOrInfo(Long clubId, String newClubName, String newInfo) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        // 클럽 이름 변경 로직 또는 규칙 적용 필요 MVC시 @Valided랑 같이 고민해보자
        // 예를 들어, 중복 체크, 유효성 검사
        if (clubRepository.findByClubName(newClubName).isPresent()) {
            throw new RuntimeException("Club name already exists: " + newClubName);
        }
        club.updateClubNameAndInfo(newClubName,newInfo);
    }
}
