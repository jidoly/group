package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {


    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Test
    public void testRegisterMember() {
        // given
        Member member1 = Member.createMember("member1", "10", "kim");
        // when
        Long boardId = memberService.registerMember(member1);
        // then
        Member findMember = memberRepository.findById(boardId).get();
        assertThat(findMember.getNick()).isEqualTo("kim");
    }

    @Test
    public void testFindMemberByUsername() {
        // given
        Member member1 = Member.createMember("member1", "10", "kim");
        memberService.registerMember(member1);

        //then
        Member findMember = memberService.findMemberByUsername("member1");
        assertThat(findMember.getNick()).isEqualTo("kim");

    }

    @Test
    public void testFindMemberByUsername_NotFound() {
        // given
        String username = "nonExistingMember";

        // when
        assertThrows(EntityNotFoundException.class, ()->
                memberService.findMemberByUsername(username));
    }

}