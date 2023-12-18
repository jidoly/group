package jidoly.group.controller.member;

import jidoly.group.domain.Member;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/member/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/member/join")
    public String join() {
        return "member/join";
    }

    @PostMapping("/member/join")
    public String register(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
//        memberService.registerMember()
        return null;
    }
}
