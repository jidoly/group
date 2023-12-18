package jidoly.group.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private int likeCount;
    private String createdDate, modifiedDate;
    private List<File> files;
    private List<Comment> comments;


    public BoardDto(Board board) {
        id = board.getId();
        writer = board.getMember().getNickname();
        title = board.getTitle();
        content = board.getContent();
        likeCount = board.getLikes().size();
        files = board.getFiles();
        comments = board.getComments();
    }

    public BoardDto(Long id, String title, String writer, String content, int likeCount, List<File> files, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.likeCount = likeCount;
        this.files = files;
        this.comments = comments;
    }
}
