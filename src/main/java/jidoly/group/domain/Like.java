package jidoly.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = "id")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    private Like(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
    private Like(Member member, Club club) {
        this.member = member;
        this.club = club;
    }


    /* 비지니스 로직 */

    public static Like addLikeBoard(Member member, Board board) {
        return new Like(member, board);
    }

    public static Like addLikeClub(Member member, Club club) {
        return new Like(member, club);
    }



}