package jidoly.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "joins")
@ToString(of = "id")
public class Join {

    @Id
    @GeneratedValue
    @Column(name = "join_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private JoinStatus status;

    private Join(Member member, Club club) {
        this.member = member;
        this.club = club;
        this.status = JoinStatus.WAIT;
    }

    /* 생성 메소드 */
    public static Join createJoin(Member member, Club club) {
        return new Join(member, club);
    }

    /* 연관관계 편의 메소드*/
    public void addClub(Club club) {
        this.club = club;
    }

    /*비지니스 로직*/
    /**
     * 가입 승인, 거절, 승격
     */
    public void agree(){
        this.status = JoinStatus.JOINED;
    }
    public void setManager(){
        this.status = JoinStatus.MANAGED;
    }

}
