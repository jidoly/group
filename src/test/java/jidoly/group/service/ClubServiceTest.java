package jidoly.group.service;

import jidoly.group.domain.Club;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClubServiceTest {

    @Autowired ClubService clubService;
    @Autowired ClubRepository clubRepository;
    @Autowired MemberService memberService;

    @BeforeEach
    void before() {
        Member member1 = Member.createMember("member1", "10", "kim");
        Member member2 = Member.createMember("member2", "20", "lee");
        memberService.registerMember(member1);
        memberService.registerMember(member2);
    }

    @Test
    void createClub() throws Exception {
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        //when
        Long clubId = clubService.createClub(club1);
        clubService.createClub(club2);

        Club findClub = clubRepository.findById(clubId).get();
        assertThat(findClub.getClubName()).isEqualTo("헬스");
    }

    @Test
    void updateClub() throws Exception {
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Long clubId = clubService.createClub(club1);
        //when
        clubService.changeClubNameOrInfo(clubId, "음악", "hihi");
        Club findClub = clubRepository.findById(clubId).get();
        //then
        assertThat(findClub.getInfo()).isEqualTo("hihi");
    }

    @Test
    void find() throws Exception {
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        Long clubId = clubService.createClub(club1);
        clubService.createClub(club2);
        //when
        List<Club> all = clubService.findAll();
        Club findClub = clubService.findById(clubId);
        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(findClub.getClubName()).isEqualTo("헬스");
    }


}