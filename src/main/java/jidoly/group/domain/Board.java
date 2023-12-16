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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /* 연관관계 편의메소드*/

    public void addFiles(File file) {
        files.add(file);
        file.addBoard(this);
    }

    /* 생성 메소드 */
    private Board(Club club, Member member, String title, String content) {
        this.club = club;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Board createBoard(Club club, Member member, String title, String content, File... files) {
        Board board = new Board(club, member, title, content);
        for (File file : files) {
            board.addFiles(file);
        }
        return board;
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
     *  좋아요 추가 / 취소
     */
    public void addLike(Member member) {
        Like like = Like.addLikeBoard(member, this);
        likes.add(like);
    }

    public void removeLike(Member member) {
        likes.removeIf(like -> like.getBoard().equals(this) && like.getMember().equals(member));
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.addBoard(this);
    }


}
