package jidoly.group.controller.group;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SearchGroupDto {

    private Long groupId;
    private String groupName;
    private String info;
    private String storeFileName;
    private Long likeCount;
    private Long joinCount;

    @QueryProjection
    public SearchGroupDto(Long groupId, String groupName, String info, String storeFileName, Long likeCount, Long joinCount) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.info = info;
        this.storeFileName = storeFileName;
        this.likeCount = likeCount;
        this.joinCount = joinCount;
    }
}
