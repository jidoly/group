package jidoly.group.controller.member;

import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
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

    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()")
    public String mypage(Model model, Principal principal) {

        String username = principal.getName();
        Member member = memberService.findMemberByUsername(username);

        /* 엔티티를 그대로 넘기는건 좋은 프렉티스가 아니므로 Dto에 담아서 전달*/
        SignupDto signupDto = new SignupDto();
        signupDto.setUsername(member.getUsername());
        signupDto.setNickname(member.getNickname());
        signupDto.setAttachFile((MultipartFile) member.getUploadFiles().get(0)); /*프로필 사진*/
        return "member/mypage";
    }

    @GetMapping("/mypage/changeNick")
    public String ShowChangeNick(@ModelAttribute("signupDto") SignupDto signupDto) {
        return "member/change-nick";
    }

    @PostMapping("/mypage/changeNick")
    @PreAuthorize("isAuthenticated()")
    public String changeNick(@ModelAttribute("signupDto") SignupDto signupDto, BindingResult bindingResult,
                             Principal principal) {
        String changeNick = signupDto.getNickname();
        if (memberRepository.existsByNickname(changeNick)) {
            bindingResult.rejectValue("nickname","nickNameDuple", "중복된 닉네임이 존재합니다.");
            return "member/change-nick";
        }

        /* 도메인 객체 가져오기 */
        String sessionName = principal.getName();
        memberService.updateNick(sessionName, changeNick);

        return "redirect:/member/mypage";
    }

    @GetMapping("/mypage/changePassword")
    public String showChangePassword(@ModelAttribute("signupDto") SignupDto signupDto) {
        return "member/change-password";
    }

    @PostMapping("/mypage/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(@ModelAttribute("signupDto") SignupDto signupDto, BindingResult bindingResult,
                             Principal principal) {
        String password = signupDto.getPassword();
        String validPw = signupDto.getValidPw();
        String sessionName = principal.getName();
        String pastPw = signupDto.getPastPw();

        System.err.println("signupDto = " + signupDto);
        System.err.println("sessionName = " + sessionName);

        /* 과거 비밀번호 맞는지 검사*/
        if (!memberService.loginCheck(sessionName, pastPw)) {
            bindingResult.rejectValue("pastPw", "passwordFail", "암호가 틀렸습니다.");
            return "member/change-password";
        }

        /* 비밀번호 일치하는지 검사 */
        if (!password.equals(validPw)) {
            bindingResult.rejectValue("validPw", "passwordFail", "비밀번호가 동일하지 않습니다.");
            return "member/change-password";
        }

        /* 다 통과되면 비밀번호 변경*/
        memberService.updatePw(sessionName, password);

        return "redirect:/member/mypage";
    }
}
