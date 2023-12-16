package jidoly.group.service;
import jidoly.group.domain.*;
import jidoly.group.repository.BoardRepository;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ClubRepository clubRepository;


    @Transactional
    public Board writePost(String username, String clubName, String title, String content) {
        Club club = clubRepository.findByClubName(clubName)
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Board board = Board.createBoard(club, member, title, content);

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
    public void likePost(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));

        //좋아요 중복 금지 - Exist하면, 다시 눌렀을떄 취소 하는걸로
        Optional<Like> likeExist = likeRepository.findByMemberIdAndBoardId(memberId, boardId);
        if (likeExist.isPresent()) {
            Long likeId = likeExist.get().getId();
            likeRepository.deleteById(likeId);
        } else {
            likeRepository.save(Like.addLikeBoard(member, board));
        }
    }


    public BoardDto findBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow();
        BoardDto boardDto = new BoardDto(board);

        boardDto.setLikeCount(likeRepository.countByBoardId(id));

        return boardDto;
    }

}
