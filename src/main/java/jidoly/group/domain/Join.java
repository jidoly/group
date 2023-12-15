package jidoly.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "joins")
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

    public Join(Member member, Club club) {
        this.member = member;
        this.club = club;
        this.status = JoinStatus.WAIT;
    }

    /**비지니스 로직*/

    /**
     * 가입 승인, 거절
     */
    public void agree(){
        this.status = JoinStatus.JOINED;
    }
    public void denied(){
        this.status = JoinStatus.DENIED;
    }
}
