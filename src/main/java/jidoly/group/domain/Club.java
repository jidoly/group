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
@ToString(exclude = "boards")
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

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Join> joins = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    /* 연관관계 편의 메소드*/
    public void addFiles(UploadFile uploadFile) {
        uploadFiles.add(uploadFile);
        uploadFile.addClub(this);
    }

    public void addJoins(Join join) {
        joins.add(join);
        join.addClub(this);
    }
    public void addBoards(Board board) {
        boards.add(board);
        board.addClub(this);
    }
    
    /*생성 메서드*/
    public static Club createClub(String clubName, String info, UploadFile... uploadFiles) {
        Club club = new Club(clubName, info);
        if (uploadFiles != null) {
            for (UploadFile uploadFile : uploadFiles) {
                if (uploadFile != null) {
                    club.addFiles(uploadFile);
                }
            }
        }
        return club;
    }

    public void updateClubNameAndInfo(String clubName, String clubInfo) {
        this.clubName = clubName;
        this.info = clubInfo;
    }

    /**
     *  좋아요 추가 / 취소
     */
    public void addLike(Member member) {
        Like like = Like.addLikeClub(member, this);
        likes.add(like);
    }

    public void removeLike(Member member) {
        likes.removeIf(like -> like.getClub().equals(this) && like.getMember().equals(member));
    }

}
