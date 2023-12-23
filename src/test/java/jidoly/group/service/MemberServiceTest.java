package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.UploadFile;
import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
        UploadFile uploadFile1 = new UploadFile("유저파일이름", "저장파일이름");
        UploadFile uploadFile2 = new UploadFile("유저파일이름1", "저장파일이름2");
        UploadFile[] uploadFiles = {uploadFile1, uploadFile2};
        Member member2 = Member.createMember("member2", "20", "lee", uploadFiles);
        System.err.println(member2);
        // when
        Long boardId = memberService.registerMember(member1);
        Long boardId2 = memberService.registerMember(member2);
        // then
        Member findMember = memberRepository.findById(boardId).get();
        assertThat(findMember.getNickname()).isEqualTo("kim");
        Member findMember2 = memberRepository.findById(boardId2).get();
        assertThat(findMember2.getUploadFiles().size()).isEqualTo(2);
    }

    @Test
    public void testFindMemberByUsername() {
        // given
        Member member1 = Member.createMember("member1", "10", "kim");
        memberService.registerMember(member1);

        //then
        Member findMember = memberService.findMemberByUsername("member1");
        assertThat(findMember.getNickname()).isEqualTo("kim");

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