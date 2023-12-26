package jidoly.group.service;

import jidoly.group.domain.Club;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
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
        Member member3 = Member.createMember("member3", "20", "qee");
        Long memberId = memberService.registerMember(member3);
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        //when
        Long clubId = clubService.createClub(memberId,club1);
        clubService.createClub(member3.getId(), club2);

        Club findClub = clubRepository.findById(clubId).get();
        assertThat(findClub.getClubName()).isEqualTo("헬스");
    }

    @Test
    void updateClub() throws Exception {
        Member member3 = Member.createMember("member3", "20", "qee");
        Long memberId = memberService.registerMember(member3);
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Long clubId = clubService.createClub(memberId, club1);
        //when
        clubService.changeClubNameOrInfo(clubId, "음악", "hihi");
        Club findClub = clubRepository.findById(clubId).get();
        //then
        assertThat(findClub.getInfo()).isEqualTo("hihi");
    }

    @Test
    void find() throws Exception {
        Member member3 = Member.createMember("member3", "20", "qee");
        Long memberId = memberService.registerMember(member3);
        //given
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        Long clubId = clubService.createClub(memberId, club1);
        clubService.createClub(member3.getId(), club2);
        clubService.likeClub(memberId, clubId);
        //when
        List<Club> all = clubService.findAll();
        Club findClub = clubService.findById(clubId);
        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(findClub.getClubName()).isEqualTo("헬스");
        assertThat(findClub.getLikes().size()).isEqualTo(1);
    }

    @Test
    void groups() throws Exception {


        for (int i = 0; i < 10; i++) {
            UploadFile uploadFile1 = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
            Member member = Member.createMember("test@test.com" + i, "1234", "nick"+i,uploadFile1);
            memberService.registerMember(member);
        }
        UploadFile uploadFile1 = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
        Member member = Member.createMember("test@test.com", "1234", "nick",uploadFile1);
        memberService.registerMember(member);

        UploadFile uploadFile2 = new UploadFile("유저파일이름", "jang.jpg");
        Club club1 = Club.createClub("헬스", "냠냠", uploadFile2);
        Club club2 = Club.createClub("수영", "룰루");
        Club club3 = Club.createClub("음악", "룰루");
        clubService.createClub(member.getId(),club1);
        clubService.createClub(member.getId(),club2);
        clubService.createClub(member.getId(),club3);
        clubService.likeClub(1L, club1.getId());
        clubService.likeClub(2L, club1.getId());
        clubService.likeClub(3L, club2.getId());
        clubService.likeClub(4L, club2.getId());
        clubService.likeClub(5L, club2.getId());
        //given

        //when

        //then

    }


}