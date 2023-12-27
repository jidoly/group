package jidoly.group;

import jakarta.annotation.PostConstruct;
import jidoly.group.domain.Club;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import jidoly.group.repository.JoinRepository;
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
public class Init {

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

            /**
             * 로그인 유저 test@test.com / 1234
             */
            UploadFile uploadFile = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
            Member member = Member.createMember("test@test.com", "1234", "nick", uploadFile);
            memberService.registerMember(member);
            UploadFile uploadFile22 = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
            Member member2 = Member.createMember("test@test.com0", "1234", "nick0", uploadFile22);
            memberService.registerMember(member2);

            /**
             * 파일들
             */
            UploadFile uploadFile2 = new UploadFile("유저파일이름", "jang.jpg");
            UploadFile uploadFile3 = new UploadFile("유저파일이름", "test.jpg");
            UploadFile uploadFile4 = new UploadFile("유저파일이름", "test2.gif");

            /**
             * 멤버 초기화 데이터 10개
             */
            for (int i = 1; i < 10; i++) {
                UploadFile uploadFile1 = new UploadFile("유저파일이름", "1f1c32ff-8ffd-43ab-8e44-8ad90afbf5af.png");
                Member member1 = Member.createMember("test@test.com" + i, "1234", "nick"+i,uploadFile1);
                memberService.registerMember(member1);
            }

            /**
             * 그룹 초기화 데이터 10개 + 커스텀 3개
             */
            Club club1 = Club.createClub("헬스", "냠냠", uploadFile2);
            Club club2 = Club.createClub("수영", "룰루", uploadFile3);
            Club club3 = Club.createClub("음악", "룰루", uploadFile4);
            clubService.createClub(member.getId(),club1);
            clubService.createClub(member.getId(),club2);
            clubService.createClub(member.getId(),club3);
            for (int i = 0; i < 10; i++) {
                UploadFile uploadFile1 = new UploadFile("유저파일이름", "test.jpg");
                Club club = Club.createClub("club" + i, "info" + i, uploadFile1);
                clubService.createClub(member.getId(), club);
            }


            Long joinId = joinService.applyJoin(member2.getId(), club2.getId());
            joinService.acceptJoin(joinId);

            /**
             * 좋아요
             */
            for (Long i = 1L; i < 11L; i++) {
                clubService.likeClub(i, club1.getId());
                clubService.likeClub(i, club2.getId());
                clubService.likeClub(i, club3.getId());
            }
            clubService.likeClub(1L, club1.getId());
            clubService.likeClub(2L, club1.getId());
            clubService.likeClub(3L, club2.getId());
            clubService.likeClub(4L, club2.getId());
            clubService.likeClub(5L, club2.getId());

            /**
             * 클럽 가입
             */
            for (Long i = 5L; i < 10L; i++) {
                Long joinId1 = joinService.applyJoin(i, club3.getId());
                Long joinId2 = joinService.applyJoin(i, club2.getId());
                if (i / 2 == 0) {
                    joinService.acceptJoin(joinId1);
                } else {
                    joinService.acceptJoin(joinId2);
                }
            }

        }
    }

}