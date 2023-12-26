package jidoly.group.controller.group;

import com.querydsl.core.annotations.QueryProjection;
import jidoly.group.domain.JoinStatus;
import lombok.Data;

@Data
public class LikeGroupsDto {

    private Long groupId;
    private String groupName;
    private String info;
    private JoinStatus joinStatus;
    private String likeStatus;
    private String storeFileName;

    @QueryProjection
    public LikeGroupsDto(Long groupId, String groupName, String info, JoinStatus joinStatus, String likeStatus, String storeFileName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.info = info;
        this.joinStatus = joinStatus;
        this.likeStatus = likeStatus;
        this.storeFileName = storeFileName;
    }
}
