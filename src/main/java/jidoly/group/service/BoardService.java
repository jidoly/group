package jidoly.group.service;
import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.*;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ClubRepository clubRepository;

    public BoardDto findBoardById(Long id) {
        Board board = boardRepository.findFetchById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        BoardDto boardDto = new BoardDto(board);

        return boardDto;
    }

    public List<BoardDto> findAllold() {

        List<Board> all = boardRepository.findFetchBoard();

        System.err.println("all = " + all);
        List<BoardDto> collect = all.stream()
                .map(a -> new BoardDto(a))
                .collect(Collectors.toList());

        return collect;
    }

    public List<BoardDto> findAll() {
        List<BoardDto> boardQueryDSL = boardRepository.findBoardQueryDSL();
        return boardQueryDSL;
    }


    @Transactional
    public Long writePost(Board board) {
        Club club = clubRepository.findByClubName(board.getClub().getClubName())
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(board.getMember().getUsername())
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        boardRepository.save(board);

        return board.getId();
    }

    /**
     * - 파일업데이트는 이후 버전에서 추가
     */
    @Transactional
    public Long updatePost(Long boardId, String title, String content) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
        board.updateBoard(title, content);
        return board.getId();
    }
    @Transactional
    public void addLikeToBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.addLike(member);
        boardRepository.save(board);
    }
    @Transactional
    public void removeLikeFromBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Like like = likeRepository.findByMemberIdAndBoardId(member.getId(), boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.removeLike(member);
        likeRepository.deleteById(like.getId());

    }
    @Transactional
    public void addCommentToBoard(Long boardId, Comment comment) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.addComment(comment);
        boardRepository.save(board);
    }


}
