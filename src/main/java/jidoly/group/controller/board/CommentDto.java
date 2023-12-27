package jidoly.group.controller.board;

import jidoly.group.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long boardId;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public CommentDto(Comment comment) {
        boardId = comment.getBoard().getId();
        writer = comment.getMember().getNickname();
        content = comment.getContent();
        createdDate = comment.getCreateDate();
        modifiedDate = comment.getCreateDate();
    }

    public CommentDto() {
    }

}
