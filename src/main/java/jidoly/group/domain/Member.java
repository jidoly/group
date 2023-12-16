package jidoly.group.domain;

import jakarta.persistence.*;
import jidoly.group.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "password")
public class Member /*extends BaseEntity*/ {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String nick;
    @Enumerated
    private Rank rank;

    private Member(String username, String password, String nick) {
        this.username = username;
        this.password = password;
        this.nick = nick;
        this.rank = Rank.NORMAL; // 디폴트로 유저등급 넣어줌.
    }
    public static Member createMember(String username, String password, String nick) {
        return new Member(username, password, nick);
    }
    public void setEncodePassword(String password) {
        this.password = password;
    }
}
