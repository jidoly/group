package jidoly.group.controller.group;

import jakarta.validation.constraints.NotBlank;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.JoinStatus;
import jidoly.group.domain.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class GroupDto {

    private Long groupId;
    @NotBlank
    private String groupName;
    @NotBlank
    private String info;

    private MultipartFile attachFile;

    private String storeFileName;
    private int likeCount;
    private int joinCount;
    private int boardCount;

    public GroupDto(Club club) {
        this.groupId = club.getId();
        this.groupName = club.getClubName();
        this.info = club.getInfo();
        List<UploadFile> uploadFiles = club.getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            this.storeFileName = storeFileName;
        }
        this.likeCount = club.getLikes().size();
        // joinStatus != wait 인 멤버수만 계산
        this.joinCount = (int) club.getJoins().stream()
                .filter(join -> join.getStatus() != JoinStatus.WAIT)
                .count();
        this.boardCount = club.getBoards().size();
    }


}
