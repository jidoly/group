package jidoly.group.controller.board;

import jakarta.validation.constraints.NotBlank;
import jidoly.group.domain.BoardCategory;
import jidoly.group.domain.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardWriteDto {

    private Long memberId;

    private Long groupId;

    @NotBlank
    private String title;

    private BoardCategory category;

    @NotBlank
    private String content;

    private MultipartFile attachFile;

    private String storeFileName;

    public BoardWriteDto(Long memberId, Long groupId, String title, BoardCategory category, String content) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
