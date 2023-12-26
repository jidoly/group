package jidoly.group.service;

import jakarta.persistence.EntityExistsException;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.JoinStatus;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.JoinRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class JoinServiceTest {


    @Autowired
    private MemberService memberService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private JoinService joinService;
    @Autowired
    private JoinRepository joinRepository;
    @Autowired
    ClubRepository clubRepository;

    @BeforeEach
    void before() {
        Member member1 = Member.createMember("member1", "10", "kim");
        Member member2 = Member.createMember("member2", "20", "lee");
        Member member3= Member.createMember("member3", "20", "qee");
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        memberService.registerMember(member1);
        memberService.registerMember(member2);
        memberService.registerMember(member3);
        clubService.createClub(member3.getId(), club1);
        clubService.createClub(member3.getId(), club2);
    }

    @Test
    void joinTest() throws Exception {
        Member member = memberService.findMemberByUsername("member1");
        Club club = clubRepository.findByClubName("헬스").get();
        //when
        Join join = Join.createJoin(member, club);
        Long joinId = joinService.applyJoin(member.getId(), club.getId());
        Join findJoin = joinRepository.findById(joinId).get();

        //then
        assertThat(findJoin.getMember().getUsername()).isEqualTo("member1");
    }

    @Test
    void joinAcceptAndDeny() throws Exception {
        Member member1 = memberService.findMemberByUsername("member1");
        Member member2 = memberService.findMemberByUsername("member2");
        Club club1 = clubRepository.findByClubName("헬스").get();
        Club club2 = clubRepository.findByClubName("음악").get();
        //given
        joinService.applyJoin(member1.getId(), club1.getId());
        joinService.applyJoin(member1.getId(), club2.getId());

        //when
        joinService.acceptJoin(member1, club1);
        joinService.denyJoin(member1.getId(), club2.getId());

        //then
        Join findJoin = joinRepository.findByMemberIdAndClubId(member1.getId(), club1.getId()).get();

        assertThat(findJoin.getStatus()).isEqualTo(JoinStatus.JOINED);
        System.err.println(joinRepository.findAll());
    }

    @Test
    void joinExceptionTest() throws Exception {
        //given
        Member member1 = memberService.findMemberByUsername("member1");
        Member member2 = memberService.findMemberByUsername("member2");
        Club club1 = clubRepository.findByClubName("헬스").get();
        Club club2 = clubRepository.findByClubName("음악").get();
        joinService.applyJoin(member1.getId(), club1.getId());
        joinService.applyJoin(member2.getId(), club2.getId());
        //when

        //then


    }


}