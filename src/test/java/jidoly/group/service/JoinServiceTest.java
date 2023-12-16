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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        memberService.registerMember(member1);
        memberService.registerMember(member2);
        clubService.createClub(club1);
        clubService.createClub(club2);
    }

    @Test
    void joinTest() throws Exception {
        Member member = memberService.findMemberByUsername("member1");
        Club club = clubRepository.findByClubName("헬스").get();
        //when
        Join join = Join.createJoin(member, club);
        Long joinId = joinService.applyJoin(join);
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
        Join join = Join.createJoin(member1, club1);
        Join join2 = Join.createJoin(member1, club2);
        joinService.applyJoin(join);
        joinService.applyJoin(join2);

        //when
        joinService.acceptJoin(member1, club1);
        joinService.denyJoin(member1, club2);

        //then
        Join findJoin = joinRepository.findByMemberIdAndClubId(member1.getId(), club1.getId()).get();
        Join findJoin2 = joinRepository.findByMemberIdAndClubId(member1.getId(), club2.getId()).get();

        assertThat(findJoin.getStatus()).isEqualTo(JoinStatus.JOINED);
        assertThat(findJoin2.getStatus()).isEqualTo(JoinStatus.DENIED);
    }

    @Test
    void joinExceptionTest() throws Exception {
        //given
        Member member1 = memberService.findMemberByUsername("member1");
        Member member2 = memberService.findMemberByUsername("member2");
        Club club1 = clubRepository.findByClubName("헬스").get();
        Club club2 = clubRepository.findByClubName("음악").get();
        Join join = Join.createJoin(member1, club1);
        Join join2 = Join.createJoin(member1, club2);
        joinService.applyJoin(join);
        joinService.applyJoin(join2);
        //when
        Join join3 = Join.createJoin(member1, club1);

        //then
        assertThrows(EntityExistsException.class,
                () -> joinService.applyJoin(join3));


    }


}