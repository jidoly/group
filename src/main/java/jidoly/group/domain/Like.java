package jidoly.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Like addLikeBoard(Member member, Board board) {
        return new Like(member, board);
    }

    private Like(Member member, Club club) {
        this.member = member;
        this.club = club;
    }
    public static Like likeClub(Member member, Club club) {
        return new Like(member, club);
    }


}