package jidoly.group.controller.member;

import jidoly.group.domain.Member;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final SignupValidator signupValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(signupValidator);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String join(@ModelAttribute("signupDto") SignupDto signupDto) {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String register(@Validated @ModelAttribute("signupDto") SignupDto signupDto, Errors errors, Model model) {

        /* 검증 */
        if (errors.hasErrors()) {
            /* 유효성 검사를 통과하지 못한 필드와 메세지 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            /* 회원가입 페이지로 리턴 */
            return "/member/signup";
        }

        /**
         * 여기서 파일 저장하는 서비스 부르고, 저장된 userfilename, srvfilename두개 넘겨줘야됨.
         */
        Member member = Member.createMember(signupDto.getUsername(), signupDto.getPassword(), signupDto.getNickname());
        Long member1 = memberService.registerMember(member);
        System.err.println("회원가입 성공 : " + member1);
        return "redirect:/member/login";
    }

}
