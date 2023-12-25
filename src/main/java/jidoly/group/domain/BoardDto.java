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
    private String createdDate;
    private String modifiedDate;
    private List<UploadFile> uploadFiles;
    private List<Comment> comments;
    private int likeCount;



    public BoardDto(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        uploadFiles = board.getUploadFiles();
        comments = board.getComments();
        writer = board.getMember().getNickname();
        likeCount = board.getLikes().size();
    }

    public BoardDto(Long id, String title, String writer, String content, int likeCount, List<UploadFile> uploadFiles, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.likeCount = likeCount;
        this.uploadFiles = uploadFiles;
        this.comments = comments;
    }
    public BoardDto(Long id, String title, String writer, String content, List<UploadFile> uploadFiles, List<Comment> comments, int likeCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.uploadFiles = uploadFiles;
        this.comments = comments;
        this.likeCount = likeCount;
    }
}
