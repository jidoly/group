package jidoly.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "likes")
public class Board /*extends BaseEntity*/ {


    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
//
//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Like> likes = new ArrayList<>();

    /* 생성 메소드 */

    private Board(Club club, Member member, String title, String content) {
        this.club = club;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Board createBoard(Club club, Member member, String title, String content) {
        return new Board(club, member, title, content);
    }




    /* 비지니스 메소드 */

    /**
     * 게시물 업데이트
     */
    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 좋아요 / 취소 / 갯수조회
     */
//    public void addLikeBoard(Member member) {
//        Like like = Like.addLikeBoard(member, this);
//        likes.add(like);
//    }
//
//    public void removeLikeBoard(Member member) {
//        likes.removeIf(like -> like.getBoard().equals(this) && like.getMember().equals(member));
//    }
//


}
