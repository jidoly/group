package jidoly.group.service;
import jakarta.persistence.EntityNotFoundException;
import jidoly.group.controller.board.BoardDto;
import jidoly.group.controller.board.BoardWriteDto;
import jidoly.group.controller.board.CommentDto;
import jidoly.group.domain.*;
import jidoly.group.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ClubRepository clubRepository;
    private final CommentRepository commentRepository;
    private final FileStore fileStore;

    public BoardDto findBoardById(Long id) {
        Board board = boardRepository.findFetchById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        BoardDto boardDto = new BoardDto(board);

        return boardDto;
    }

    public List<BoardDto> findAll() {

        List<Board> all = boardRepository.findAllByOrderByLastModifiedDateDesc();

        List<BoardDto> collect = all.stream()
                .map(a -> new BoardDto(a))
                .collect(Collectors.toList());

        return collect;
    }
    public List<BoardDto> findAllByGroupId(Long groupId) {

        List<Board> all = boardRepository.findAllByGroupId(groupId);

        List<BoardDto> collect = all.stream()
                .map(a -> new BoardDto(a))
                .collect(Collectors.toList());

        return collect;
    }

    public List<BoardDto> findAllDsl() {
        List<BoardDto> boardQueryDSL = boardRepository.findBoardQueryDSL();
        return boardQueryDSL;
    }


    @Transactional
    public Long writePost(BoardWriteDto writeDto) throws IOException {
        Club club = clubRepository.findById(writeDto.getGroupId())
                .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findById(writeDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        UploadFile uploadFile = (writeDto.getAttachFile() != null && !writeDto.getAttachFile().isEmpty())
                ? fileStore.storeFile(writeDto.getAttachFile())
                : null;

        Board board = Board.createBoard(club, member, writeDto.getTitle(), writeDto.getContent(),
                writeDto.getCategory(), uploadFile);

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

    /* 처음 눌렀을때 -> 좋아요 저장, true 반환 / 두번째 -> 좋아요 취소, false반환 */
    @Transactional
    public boolean likeBoard(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        //좋아요 중복 금지 - Exist하면, 다시 눌렀을떄 취소 하는걸로
        Optional<Like> likeExist = likeRepository.findByMemberIdAndBoardId(memberId, boardId);
        if (likeExist.isPresent()) {
            Long likeId = likeExist.get().getId();
            board.removeLike(member);
            likeRepository.deleteById(likeId);
            return false;
        } else {
            board.addLike(member);
            boardRepository.save(board);
            return true;
        }
    }
    
    @Transactional
    public void addCommentToBoard(CommentDto commentDto) {
        Board board = boardRepository.findById(commentDto.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board with ID " + commentDto.getBoardId() + " not found"));
        Member member = memberRepository.findByNickname(commentDto.getWriter())
                .orElseThrow(() -> new EntityNotFoundException("Member with nickname " + commentDto.getWriter() + " not found"));

        Comment comment = Comment.createComment(commentDto.getContent(), member, board);
        board.addComment(comment);

        commentRepository.save(comment);
    }


}
