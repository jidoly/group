package jidoly.group.domain;

import lombok.Data;

@Data
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private int likeCount;
    private String createdDate, modifiedDate;

    public BoardDto(Board board) {
        id = board.getId();
        writer = board.getMember().getNick();
        title = board.getTitle();
        content = board.getContent();
//        createdDate = bo
    }
}
