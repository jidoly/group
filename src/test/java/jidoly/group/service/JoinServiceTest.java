package jidoly.group.service;

import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.JoinStatus;
import jidoly.group.domain.Member;
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

@SpringBootTest
@Transactional
@Commit
class JoinServiceTest {


    @Autowired
    private MemberService memberService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private JoinService joinService;
    @Autowired
    private JoinRepository joinRepository;

    @Test
    void joinTest() throws Exception {
        //given
        Member member1 = memberService.registerMember("member1", "10", "kim");
        Member member2 = memberService.registerMember("member2", "20", "lee");
        Club club1 = clubService.createClub("king", "안녕하세요");
        Club club2 = clubService.createClub("king2", "안녕하세요2");

        //when
        Join findJoin = joinService.applyJoin(member1, club1);

        //then
        assertThat(findJoin.getMember().getUsername()).isEqualTo("member1");
    }

    @Test
    void joinAcceptAndDeny() throws Exception {
        //given
        Member member1 = memberService.registerMember("member1", "10", "kim");
        Member member2 = memberService.registerMember("member2", "20", "lee");
        Club club1 = clubService.createClub("헬스", "안녕");
        Club club2 = clubService.createClub("음악", "룰루");
        joinService.applyJoin(member1, club1);
        joinService.applyJoin(member1, club2);

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

        //when

        //then

    }


}