package jidoly.group.service;

import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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


    @BeforeEach
    void before(){
        Member member1 = new Member("member1", "10", "kim");
        Member member2 = new Member("member2", "10", "lee");
        Member member3 = new Member("member3", "10", "park");
        Member member4 = new Member("member4", "10", "jin");
        memberService.registerMember("member1", "10", "kim");
        memberService.registerMember("member2", "20", "lee");
    }

    @Test
    public void testRegisterMember() {
        // given
        String username = "testMember";
        String password = "testPassword";
        String nick = "testNick";

        // when
        Member registeredMember = memberService.registerMember(username, password, nick);

        // then
        assertNotNull(registeredMember);
        assertEquals(username, registeredMember.getUsername());
        assertTrue(passwordEncoder.matches(password, registeredMember.getPassword()));
    }

    @Test
    public void testFindMemberByUsername() {
        // given
        String username = "testMember";
        String password = "testPassword";
        String nick = "testNick";
        Member registeredMember = memberService.registerMember(username, password, nick);

        // when
        Member foundMember = memberService.findMemberByUsername(username);

        // then
        assertNotNull(foundMember);
        assertEquals(username, foundMember.getUsername());
    }

    @Test
    public void testFindMemberByUsername_NotFound() {
        // given
        String username = "nonExistingMember";

        // when
        memberService.findMemberByUsername(username);
    }

    // 다른 MemberService의 메서드에 대한 테스트들
}