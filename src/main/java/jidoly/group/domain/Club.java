package jidoly.group.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jidoly.group.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    
    /*생성 메서드*/
    public static Club createClub(String clubName, String info) {
        return new Club(clubName, info);
    }
    public void updateClubName(String clubName) {
        this.clubName = clubName;
    }
    public void updateInfo(String clubInfo) {
        this.info = clubInfo;
    }
}
