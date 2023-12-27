package jidoly.group.controller.group;

import jidoly.group.domain.Join;
import jidoly.group.domain.JoinStatus;
import jidoly.group.domain.UploadFile;
import lombok.Data;

import java.util.List;

@Data
public class JoinMemberDto {

    private Long joinId;
    private String nickName;
    private String username;
    private String storeFileName;
    private JoinStatus status;

    public JoinMemberDto(Join join) {
        this.joinId = join.getId();
        this.nickName = join.getMember().getNickname();
        this.username = join.getMember().getUsername();
        List<UploadFile> uploadFiles = join.getMember().getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            this.storeFileName = storeFileName;
        }
        this.status = join.getStatus();
    }
}
