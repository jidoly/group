package jidoly.group.controller.board;

import jidoly.group.controller.group.GroupDto;
import jidoly.group.domain.Board;
import jidoly.group.domain.BoardCategory;
import jidoly.group.domain.Comment;
import jidoly.group.domain.UploadFile;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardDto {

    private Long boardId;
    private String title;
    private String writer;
    private String content;
    private BoardCategory boardCategory;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<UploadFile> uploadFiles;
    private List<CommentDto> comments;
    private int likeCount;
    private String lastStoreFileName;
    private String userImage;


    public BoardDto(Board board) {
        boardId = board.getId();
        title = board.getTitle();
        writer = board.getMember().getNickname();
        content = board.getContent();
        createdDate = board.getCreateDate();
        modifiedDate = board.getLastModifiedDate();
        boardCategory = board.getCategory();
        uploadFiles = board.getUploadFiles();
        comments = board.getComments().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());

        likeCount = board.getLikes().size();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            this.lastStoreFileName = storeFileName;
        }
        List<UploadFile> uploadFiles = board.getMember().getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            this.userImage = storeFileName;
        }
    }

    public BoardDto(Long boardId, String title, String writer, String content, int likeCount, List<UploadFile> uploadFiles, List<Comment> comments) {
        this.boardId = boardId;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.likeCount = likeCount;
        this.uploadFiles = uploadFiles;
        //comment
    }

}
