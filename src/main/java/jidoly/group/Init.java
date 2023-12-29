package jidoly.group;

import jakarta.annotation.PostConstruct;
import jidoly.group.controller.board.BoardWriteDto;
import jidoly.group.domain.*;
import jidoly.group.repository.JoinRepository;
import jidoly.group.service.BoardService;
import jidoly.group.service.ClubService;
import jidoly.group.service.JoinService;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Profile("local")
@Component
@RequiredArgsConstructor
public class Init {

    private final InitMemberService initMemberService;
//    @PostConstruct
    public void init() throws IOException {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        private MemberService memberService;
        private ClubService clubService;
        private JoinService joinService;

        private BoardService boardService;

        public InitMemberService(MemberService memberService, ClubService clubService, JoinService joinService, BoardService boardService) {
            this.memberService = memberService;
            this.clubService = clubService;
            this.joinService = joinService;
            this.boardService = boardService;
        }

        @Transactional
        public void init() throws IOException {

            /**
             * 테스트 로그인 유저 test@test.com / 1234
             */
            UploadFile uploadFile = new UploadFile("유저파일이름", "pokemon.png");
            Member member = Member.createMember("test@test.com", "1234", "nick", uploadFile);
            memberService.registerMember(member);

            Member member2 = Member.createMember("test@test.com0", "1234", "nick0");
            memberService.registerMember(member2);

            /**
             * 멤버 초기화 데이터 10개
             */
            for (int i = 1; i < 11; i++) {
                UploadFile uploadFile1 = new UploadFile("유저파일이름", "user" + i +".png");
                Member member1 = Member.createMember("test@test.com" + i, "1234", "닉네임"+i,uploadFile1);
                memberService.registerMember(member1);
            }

            /**
             * 그룹 초기화 데이터 10개 + 커스텀 3개
             */
            for (int i = 1; i < 11; i++) {
                UploadFile uploadFile1 = new UploadFile("유저파일이름", "group" + i +".jpg");
                Club club = Club.createClub("그룹이름" + i, "소개" + i, uploadFile1);
                clubService.createClub(member.getId(), club);
            }

            Club club1 = clubService.findById(1L);
            Club club2 = clubService.findById(2L);
            Club club3 = clubService.findById(3L);
            Long joinId = joinService.applyJoin(member2.getId(), club1.getId());
            joinService.acceptJoin(joinId);

            /**
             * 좋아요
             */
            for (Long i = 1L; i < 11L; i++) {
                if (i % 2 == 0) {
                    clubService.likeClub(i, club1.getId());
                } else {
                    clubService.likeClub(i, club2.getId());
                }
                clubService.likeClub(i, club3.getId());
            }

            /**
             * 클럽 가입
             */
            for (Long i = 5L; i < 11L; i++) {
                Long joinId1 = joinService.applyJoin(i, club3.getId());
                Long joinId2 = joinService.applyJoin(i, club2.getId());
                Long joinId3 = joinService.applyJoin(i, club1.getId());
                if (i % 2 == 0) {
                    joinService.acceptJoin(joinId1);
                } else {
                    joinService.acceptJoin(joinId2);
                    joinService.acceptJoin(joinId3);
                }
            }

            /**
             * 글작성
             */


        }
    }

}