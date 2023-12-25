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
    public static MyGroupDto fromJoin(Join join) {
        MyGroupDto myGroupDto = new MyGroupDto();
        myGroupDto.setGroupId(join.getClub().getId());
        myGroupDto.setGroupName(join.getClub().getClubName());

        // 여기서는 업로드 파일이 하나만 있다고 가정하고, 필요에 따라 수정할 수 있다.
        List<UploadFile> uploadFiles = join.getClub().getUploadFiles();
        if (!uploadFiles.isEmpty()) {
            myGroupDto.setStoreFileName(uploadFiles.get(0).getStoreFileName());
        }

        return myGroupDto;
    }

    // 여러 Join 엔티티 리스트를 MyGroupDto 리스트로 변환하는 메서드
    public static List<MyGroupDto> fromJoinList(List<Join> joinList) {
        return joinList.stream()
                .map(MyGroupDto::fromJoin)
                .collect(Collectors.toList());
    }

}
