package jidoly.group.service;

import jakarta.persistence.EntityManager;
import jidoly.group.domain.*;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
    void writePostAndLikePostTest() {
        // given
        Member member = memberRepository.findByUsername("member1").get();
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용");
        Long boardId = boardService.writePost(board);
        //when
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못 찾았음"));

        //then
        assertThat(findBoard.getTitle()).isEqualTo("제목");
    }


    @Test
    public void updatePostTest() {
        Member member = memberRepository.findByUsername("member1").get();
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용");
        Long boardId = boardService.writePost(board);
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
        Member member = memberRepository.findByUsername("member1").get();
        Member member2 = memberRepository.findByUsername("member2").get();
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용");
        Long boardId = boardService.writePost(board);
        Long boardId2 = boardService.writePost(board);
        //when
        boardService.likeBoard(member.getId(), boardId);
        //위에 끝나고나서,
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

        Member member = memberRepository.findByUsername("member1").get();
        Member member2 = memberRepository.findByUsername("member2").get();
        Club club = clubRepository.findByClubName("헬스").get();
        Board board = Board.createBoard(club, member, "제목", "내용", uploadFiles);


        Long boardId = boardService.writePost(board);
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
        Board board = Board.createBoard(club, member, "제목", "내용", uploadFiles);


        Long boardId = boardService.writePost(board);

        Comment comment = Comment.createComment("hihi", member, board);
        Comment comment2 = Comment.createComment("hihi", member, board);
        //when
        boardService.addCommentToBoard(boardId, comment);
        boardService.addCommentToBoard(boardId, comment2);

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