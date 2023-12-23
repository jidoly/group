package jidoly.group.domain;

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
    private List<UploadFile> uploadFiles;
    private List<Comment> comments;


    public BoardDto(Board board) {
        id = board.getId();
        writer = board.getMember().getNickname();
        title = board.getTitle();
        content = board.getContent();
        likeCount = board.getLikes().size();
        uploadFiles = board.getUploadFiles();
        comments = board.getComments();
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
}
