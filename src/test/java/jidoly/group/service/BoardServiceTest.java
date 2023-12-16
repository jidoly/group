package jidoly.group.service;

import jidoly.group.domain.Board;
import jidoly.group.domain.Club;
import jidoly.group.domain.Like;
import jidoly.group.domain.Member;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import org.assertj.core.api.Assertions;
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
@Commit
class BoardServiceTest {

    @Autowired BoardService boardService;

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired ClubService clubService;

    @Autowired BoardRepository boardRepository;

    @Autowired LikeRepository likeRepository;

    @Test
    void writePostAndLikePostTest() {
        // given
        Member member1 = memberService.registerMember("member1", "10", "kim");
        Member member2 = memberService.registerMember("member2", "20", "lee");
        Club club1 = clubService.createClub("헬스", "안녕");
        Club club2 = clubService.createClub("음악", "룰루");

        //when
        boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");
        Optional<Board> board = boardRepository.findById(1L);
        Board findboard = board.orElseThrow(() -> new RuntimeException("못 찾았음"));

        //then
        assertThat(findboard.getTitle()).isEqualTo("게시물제목");

        // 좋아요 추가
//        Like like = boardService.likePost(member1.getId(), board.getId());
    }


    @Test
    public void updatePostTest() {
        // given
        Member member1 = memberService.registerMember("member1", "10", "kim");
        Member member2 = memberService.registerMember("member2", "20", "lee");
        Club club1 = clubService.createClub("헬스", "안녕");
        Club club2 = clubService.createClub("음악", "룰루");
        boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");

        //when
        Optional<Board> board = boardRepository.findById(1L);
        Board findboard = board.orElseThrow(() -> new RuntimeException("못 찾았음"));
        boardService.updatePost(1L, "바뀐 제목", "바뀐 내용");

        Optional<Board> board2 = boardRepository.findById(1L);
        Board findboard2 = board2.orElseThrow(() -> new RuntimeException("못 찾았음"));

        assertThat(findboard2.getTitle()).isEqualTo("바뀐 제목");

    }
    
    @Test
    void 좋아요_갯수까지받아오는board정보() throws Exception {
        //given
        Member member1 = memberService.registerMember("member1", "10", "kim");
        Member member2 = memberService.registerMember("member2", "20", "lee");
        Club club1 = clubService.createClub("헬스", "안녕");
        Club club2 = clubService.createClub("음악", "룰루");
        boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");
        //when
        boardService.likePost(member1.getId(), 1L);
        boardService.likePost(member2.getId(), 1L);

        System.err.println(boardService.findBoardById(1L));
        int likeCount = boardService.findBoardById(1L).getLikeCount();
        assertThat(likeCount).isEqualTo(2);

        //다시 좋아요 누르면 -> 취소
        boardService.likePost(member1.getId(), 1L);
        int likeCount2 = boardService.findBoardById(1L).getLikeCount();
        assertThat(likeCount2).isEqualTo(1);

        //then
        
    }

    // 추가적인 테스트 케이스에 대한 메서드 추가 가능
}