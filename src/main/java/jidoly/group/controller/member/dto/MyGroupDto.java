package jidoly.group.controller.member.dto;

import jidoly.group.domain.Join;
import jidoly.group.domain.UploadFile;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MyGroupDto {

    private Long groupId;
    private String groupName;
    private String storeFileName;

    // 생성자나 정적 팩토리 메서드를 이용하여 MyGroupDto 객체 생성
    public static MyGroupDto makeMygroupDto(Join join) {
        MyGroupDto myGroupDto = new MyGroupDto();
        myGroupDto.setGroupId(join.getClub().getId());
        myGroupDto.setGroupName(join.getClub().getClubName());

        List<UploadFile> uploadFiles = join.getClub().getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            String storeFileName = uploadFiles.get(uploadFiles.size() - 1).getStoreFileName();
            myGroupDto.setStoreFileName(storeFileName);
        }

        return myGroupDto;
    }

    public static List<MyGroupDto> makeMyGroupDtoList(List<Join> joinList) {
        return joinList.stream()
                .map(MyGroupDto::makeMygroupDto)
                .collect(Collectors.toList());
    }

}
