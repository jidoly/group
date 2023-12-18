package jidoly.group;

import jakarta.annotation.PostConstruct;
import jidoly.group.domain.Member;
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

        InitMemberService(MemberService memberService) {
            this.memberService = memberService;
        }

        @Transactional
        public void init() {

            Member member = Member.createMember("test@test.com", "1234", "kim");
            memberService.registerMember(member);
        }
    }

}