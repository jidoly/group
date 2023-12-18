package jidoly.group.domain;

import jakarta.persistence.*;
import jidoly.group.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "password")
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String nickname;
    @Enumerated
    private Rank rank;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    /* 연관관계 편의 메소드*/
    public void addFiles(File file) {
        files.add(file);
        file.addMember(this);
    }

    private Member(String username, String password, String nick) {
        this.username = username;
        this.password = password;
        this.nickname = nick;
        this.rank = Rank.NORMAL; // 디폴트로 유저등급 넣어줌.
    }
    public static Member createMember(String username, String password, String nick, File... files) {
        Member member = new Member(username, password, nick);
        for (File file : files) {
            member.addFiles(file);
        }
        return member;
    }
    public void setEncodePassword(String password) {
        this.password = password;
    }
}
