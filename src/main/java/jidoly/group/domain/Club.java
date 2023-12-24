package jidoly.group.domain;

import jakarta.persistence.*;
import jidoly.group.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Club extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    private String clubName;
    private String info;

    public Club(String clubName, String info) {
        this.clubName = clubName;
        this.info = info;
    }

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    /* 연관관계 편의 메소드*/
    public void addFiles(UploadFile uploadFile) {
        uploadFiles.add(uploadFile);
        uploadFile.addClub(this);
    }
    
    /*생성 메서드*/
    public static Club createClub(String clubName, String info, UploadFile... uploadFiles) {
        Club club = new Club(clubName, info);
        for (UploadFile uploadFile : uploadFiles) {
            club.addFiles(uploadFile);
        }
        return club;
    }

    public void updateClubNameAndInfo(String clubName, String clubInfo) {
        this.clubName = clubName;
        this.info = clubInfo;
    }

}
