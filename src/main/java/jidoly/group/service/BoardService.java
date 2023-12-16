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


    @Transactional
    public Board writePost(String username, String clubName, String title, String content,File... files) {
        Club club = clubRepository.findByClubName(clubName)
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Board board = Board.createBoard(club, member, title, content, files);

        return boardRepository.save(board);
    }
    @Transactional
    public Board updatePost(Long postId, String title, String content) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
        board.updateBoard(title, content);
        return board;
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
    public BoardDto findBoardById(Long id) {
        Board board = boardRepository.findWithLikeBoardById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        BoardDto boardDto = new BoardDto(board);

        return boardDto;
    }

    public List<BoardDto> findAll() {
        List<Board> all = boardRepository.findAll();
        List<BoardDto> collect = all.stream()
                .map(a -> new BoardDto(a))
                .collect(Collectors.toList());

        return collect;
    }


}
