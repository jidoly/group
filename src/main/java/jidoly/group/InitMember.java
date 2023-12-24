package jidoly.group;

import jakarta.annotation.PostConstruct;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import jidoly.group.service.ClubService;
import jidoly.group.service.JoinService;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;
    @PostConstruct
    public void init(){
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        private MemberService memberService;
        private ClubService clubService;
        private JoinService joinService;

        public InitMemberService(MemberService memberService, ClubService clubService, JoinService joinService) {
            this.memberService = memberService;
            this.clubService = clubService;
            this.joinService = joinService;
        }

        @Transactional
        public void init() {
            UploadFile uploadFile1 = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
            Member member = Member.createMember("test@test.com", "1234", "kim",uploadFile1);
            memberService.registerMember(member);

            UploadFile uploadFile2 = new UploadFile("유저파일이름", "jang.jpg");
            Club club1 = Club.createClub("헬스", "냠냠", uploadFile2);
            Club club2 = Club.createClub("보이면안됨", "룰루");
            Club club3 = Club.createClub("음악", "룰루");
            clubService.createClub(club1);
            clubService.createClub(club2);
            clubService.createClub(club3);
            Join join = Join.createJoin(member, club1);
            Join join2 = Join.createJoin(member, club2);
            Join join3 = Join.createJoin(member, club3);
            joinService.applyJoin(join);
            joinService.applyJoin(join2);
            joinService.applyJoin(join3);

            joinService.acceptJoin(member, club1);
            joinService.acceptJoin(member, club3);
        }
    }

}