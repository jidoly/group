package jidoly.group.controller.member.dto;

import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import lombok.Data;

import java.util.List;

@Data
public class MemberDto {


    private String username;
    private String nickname;
    private String storeFileName;

    public MemberDto(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        List<UploadFile> uploadFiles = member.getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            this.storeFileName = storeFileName;
        }
    }
}
