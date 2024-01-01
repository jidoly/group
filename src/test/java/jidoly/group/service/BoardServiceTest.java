package jidoly.group.service;

import jakarta.persistence.EntityManager;
import jidoly.group.controller.board.BoardDto;
import jidoly.group.controller.board.BoardWriteDto;
import jidoly.group.controller.board.CommentDto;
import jidoly.group.domain.*;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired BoardService boardService;

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired ClubService clubService;

    @Autowired BoardRepository boardRepository;
    @Autowired ClubRepository clubRepository;

    @Autowired LikeRepository likeRepository;
    @Autowired EntityManager em;

    @BeforeEach
    void before() {
        Member member1 = Member.createMember("member1", "10", "kim");
        Member member2 = Member.createMember("member2", "20", "lee");
        Club club1 = Club.createClub("헬스", "냠냠");
        Club club2 = Club.createClub("음악", "룰루");
        memberService.registerMember(member1);
        memberService.registerMember(member2);
        clubService.createClub(member1.getId(), club1);
        clubService.createClub(member1.getId(), club2);
    }

    @Test
    void writePostAndLikePostTest() throws IOException {
        // given
        Member member = memberRepository.findByUsername("member1")
                .orElseThrow(() -> new RuntimeException("not found member"));;
        Club club = clubRepository.findByClubName("헬스")
                .orElseThrow(() -> new RuntimeException("not found club"));
        Board board = Board.createBoard(club, member, "제목", "내용", BoardCategory.BOARD);
        BoardWriteDto boardWriteDto = new BoardWriteDto(member.getId(), club.getId(), board.getTitle(), board.getCategory(), board.getContent());
        Long boardId = boardService.writePost(boardWriteDto);
        //when
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("not found board"));

        //then
        assertThat(findBoard.getTitle()).isEqualTo("제목");
    }


    @Test
    public void updatePostTest() throws IOException {
        Member member = memberRepository.findByUsername("member1")
                .orElseThrow(() -> new RuntimeException("not found member"));;
        Club club = clubRepository.findByClubName("헬스")
                .orElseThrow(() -> new RuntimeException("not found club"));
        Board board = Board.createBoard(club, member, "제목", "내용", BoardCategory.BOARD);
        BoardWriteDto boardWriteDto = new BoardWriteDto(member.getId(), club.getId(), board.getTitle(), board.getCategory(), board.getContent());
        Long boardId = boardService.writePost(boardWriteDto);
        //when
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("not found board"));
        boardService.updatePost(boardId, "바뀐 제목", "바뀐 내용");

        Board updateBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못 찾았음"));

        assertThat(updateBoard.getTitle()).isEqualTo("바뀐 제목");
    }
    
    @Test
    void like_board_test() throws Exception {
        //given
        Member member = memberRepository.findByUsername("member1")
                .orElseThrow(() -> new RuntimeException("not found member"));;
        Member member2 = memberRepository.findByUsername("member2")
                .orElseThrow(() -> new RuntimeException("not found member"));;
        Club club = clubRepository.findByClubName("헬스")
                .orElseThrow(() -> new RuntimeException("not found club"));
        Board board = Board.createBoard(club, member, "제목", "내용", BoardCategory.BOARD);
        BoardWriteDto boardWriteDto = new BoardWriteDto(member.getId(), club.getId(), board.getTitle(), board.getCategory(), board.getContent());
        Long boardId = boardService.writePost(boardWriteDto);
        Long boardId2 = boardService.writePost(boardWriteDto);
        //when
        boardService.likeBoard(member.getId(), boardId);
        boardService.likeBoard(member2.getId(), boardId);

        int likeCount = boardService.findBoardById(boardId).getLikeCount();
        assertThat(likeCount).isEqualTo(2);

        //다시 좋아요 누르면 -> 취소
        boardService.likeBoard(member.getId(), boardId);
        BoardDto boardDto = boardService.findBoardById(boardId);
        assertThat(boardDto.getLikeCount()).isEqualTo(1);


        //then

    }

    @Test
    void 파일포함한전부all() throws Exception {
        //given
        UploadFile uploadFile1 = new UploadFile("유저파일이름", "저장파일이름");
        UploadFile uploadFile2 = new UploadFile("유저파일이름1", "저장파일이름2");
        UploadFile[] uploadFiles = {uploadFile1, uploadFile2};

        Member member = memberRepository.findByUsername("member1")
                .orElseThrow(() -> new RuntimeException("not found member1"));
        Member member2 = memberRepository.findByUsername("member2")
                .orElseThrow(() -> new RuntimeException("not found member2"));
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용", BoardCategory.BOARD, uploadFiles);


        BoardWriteDto boardWriteDto = new BoardWriteDto(member.getId(), club.getId(), board.getTitle(), board.getCategory(), board.getContent());
        Long boardId = boardService.writePost(boardWriteDto);
        //when
        boardService.likeBoard(member.getId(), boardId);
        boardService.likeBoard(member2.getId(), boardId);

        //when
        List<BoardDto> boardDtos = boardService.findAll();
        //then

    }

    @Test
    void comment() throws Exception {
        //given
        UploadFile uploadFile1 = new UploadFile("유저파일이름", "저장파일이름");
        UploadFile uploadFile2 = new UploadFile("유저파일이름1", "저장파일이름2");
        UploadFile[] uploadFiles = {uploadFile1, uploadFile2};

        Member member = memberRepository.findByUsername("member1").get();
        Member member2 = memberRepository.findByUsername("member2").get();
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용",BoardCategory.BOARD, uploadFiles);


        BoardWriteDto boardWriteDto = new BoardWriteDto(member.getId(), club.getId(), board.getTitle(), board.getCategory(), board.getContent());
        Long boardId = boardService.writePost(boardWriteDto);



        CommentDto commentDto1 = new CommentDto();
        commentDto1.setBoardId(boardId);
        commentDto1.setContent("hihi");
        commentDto1.setWriter(member.getNickname());

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setBoardId(boardId);
        commentDto2.setContent("nono");
        commentDto2.setWriter(member.getNickname());

        //when
        boardService.addCommentToBoard(commentDto1);
        boardService.addCommentToBoard(commentDto2);

        em.flush();
        em.clear();

        //when
        System.err.println("======= 1 =====");
        List<BoardDto> all = boardService.findAll();
        System.err.println("======= 2 =====");
        for (BoardDto boardDto : all) {
            System.err.println("boardDto = " + boardDto);
        }
        System.err.println("======= 3 =====");

    }


}