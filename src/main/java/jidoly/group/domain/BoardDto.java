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
    private List<File> files;

    public BoardDto(Board board) {
        id = board.getId();
        writer = board.getMember().getNick();
        title = board.getTitle();
        content = board.getContent();
        likeCount = board.getLikes().size();
        files = board.getFiles();

    }
}
