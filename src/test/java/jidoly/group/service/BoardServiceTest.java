package jidoly.group.service;

import jidoly.group.domain.*;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired BoardService boardService;

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired ClubService clubService;

    @Autowired BoardRepository boardRepository;

    @Autowired LikeRepository likeRepository;

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
    void writePostAndLikePostTest() {
        // given
        Long boardId = boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");
        //when
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못 찾았음"));

        //then
        assertThat(findBoard.getTitle()).isEqualTo("게시물제목");
    }


    @Test
    public void updatePostTest() {
        Long boardId = boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");
        //when
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못 찾았음"));
        boardService.updatePost(boardId, "바뀐 제목", "바뀐 내용");

        Board updateBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못 찾았음"));

        assertThat(updateBoard.getTitle()).isEqualTo("바뀐 제목");
    }
    
    @Test
    void 좋아요_갯수까지받아오는board정보() throws Exception {
        //given
        List<Member> all = memberRepository.findAll();
        List<Long> collect = all.stream()
                .map(Member::getId)
                .collect(Collectors.toList());


        Member member = memberRepository.findById(collect.get(0)).get();
        Member member2 = memberRepository.findById(collect.get(1)).get();
        Long boardId = boardService.writePost("member1", "헬스", "게시물제목", "게시물내용");
        //when
        boardService.addLikeToBoard(boardId, member);
        boardService.addLikeToBoard(boardId, member2);

        System.err.println(boardService.findBoardById(boardId));
        int likeCount = boardService.findBoardById(boardId).getLikeCount();
        assertThat(likeCount).isEqualTo(2);

        //다시 좋아요 누르면 -> 취소
        boardService.removeLikeFromBoard( boardId,member);
        BoardDto boardDto = boardService.findBoardById(boardId);
        assertThat(boardDto.getLikeCount()).isEqualTo(1);


        //then

    }

    @Test
    void 파일포함한전부all() throws Exception {
        //given
        List<Member> all = memberRepository.findAll();
        List<Long> collect = all.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        Member memberId = memberRepository.findById(collect.get(0)).get();
        Member memberId2 = memberRepository.findById(collect.get(1)).get();

        File file1 = new File("유저파일이름", "저장파일이름");
        File file2 = new File("유저파일이름1", "저장파일이름2");
        File[] files = {file1, file2};

        Long boardId = boardService.writePost("member1", "헬스", "게시물제목", "게시물내용", files);
        boardService.writePost("member2", "헬스", "게시물제목", "게시물내용");
        boardService.writePost("member2", "헬스", "게시물제목", "게시물내용");
        //when
        boardService.addLikeToBoard(boardId, memberId);
        boardService.addLikeToBoard(boardId, memberId2);

        //when
        List<BoardDto> boardDtos = boardService.findAll();
        for (BoardDto boardDto : boardDtos) {
            System.err.println("boardDto = " + boardDto);
        }
        //then

    }

    @Test
    void comment() throws Exception {
        //given
        File file1 = new File("유저파일이름", "저장파일이름");

        Long boardId = boardService.writePost("member1", "헬스", "게시물제목", "게시물내용", file1);
        boardService.writePost("member2", "헬스", "게시물제목", "게시물내용");
        boardService.writePost("member2", "헬스", "게시물제목", "게시물내용");


        Member member = memberRepository.findByUsername("member1").get();

        Board board = boardRepository.findById(boardId).get();

        Comment comment = Comment.createComment("hihi", member, board);
        Comment comment2 = Comment.createComment("hihi", member, board);
        //when
        boardService.addCommentToBoard(boardId, comment);
        boardService.addCommentToBoard(boardId, comment2);

        //when
        List<BoardDto> all = boardService.findAll();
        for (BoardDto boardDto : all) {
            System.err.println("boardDto = " + boardDto);
        }

    }

}