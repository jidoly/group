package jidoly.group.controller.member;

import jidoly.group.sequrity.CustomUser;
import jidoly.group.controller.member.dto.MemberDto;
import jidoly.group.controller.member.dto.MyGroupDto;
import jidoly.group.controller.member.dto.SignupDto;
import jidoly.group.domain.FileStore;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import jidoly.group.repository.MemberRepository;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final SignupValidator signupValidator;
    private final FileStore fileStore;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(signupValidator);
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model) {

        /* 에러와 예외를 모델에 담아 view resolve */
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/member/login";
    }

    @GetMapping("/signup")
    public String join(@ModelAttribute("signupDto") SignupDto signupDto) {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String register(@Validated @ModelAttribute("signupDto") SignupDto signupDto, Errors errors, Model model) throws IOException {

        /* 검증 */
        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            /* 유효성 검사를 통과하지 못한 필드와 메세지 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            /* 회원가입 페이지로 리턴 */
            return "/member/signup";
        }

        /**
         * 여기서 파일 저장하는 서비스 부르고, 저장된 파일이름 넘겨주기
         */
        UploadFile uploadFile = (signupDto.getAttachFile() != null && !signupDto.getAttachFile().isEmpty())
                ? fileStore.storeFile(signupDto.getAttachFile())
                : null;
        Member member = Member.createMember(signupDto.getUsername(), signupDto.getPassword(), signupDto.getNickname(), uploadFile);
        Long member1 = memberService.registerMember(member);
        log.debug("회원가입 성공 = {}", member1);
        return "redirect:/member/login";
    }

    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()")
    public String mypage(@ModelAttribute("signupDto") SignupDto signupDto, Model model, @AuthenticationPrincipal CustomUser customUser) {

        String username = customUser.getUsername();
        Long userId = customUser.getId();

        Member member = memberService.findMemberByUsername(username);
        List<Join> myGroups = memberService.findMyGroups(userId);
        List<MyGroupDto> myGroupDto = MyGroupDto.makeMyGroupDtoList(myGroups); // 이거 즐겨찾기로 변경

        MemberDto memberDto = new MemberDto(member);
        model.addAttribute("member", memberDto);
        model.addAttribute("myGroupDto", myGroupDto);
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
            bindingResult.rejectValue("nickname","duplicate");
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

        /* 과거 비밀번호 맞는지 검사*/
        if (!memberService.loginCheck(sessionName, pastPw)) {
            bindingResult.rejectValue("pastPw", "passwordFail");
            return "member/change-password";
        }

        /* 비밀번호 일치하는지 검사 */
        if (!password.equals(validPw)) {
            bindingResult.rejectValue("validPw", "passwordFail");
            return "member/change-password";
        }

        /* 다 통과되면 비밀번호 변경*/
        memberService.updatePw(sessionName, password);

        return "redirect:/member/mypage";
    }
    @PostMapping("/mypage/changeImage")
    @PreAuthorize("isAuthenticated()")
    public String changeImage(@ModelAttribute("signupDto") SignupDto signupDto, BindingResult bindingResult,
                             Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            log.error("errors={}", bindingResult);
            return "/member/mypage";
        }


        UploadFile uploadFile = UploadFile.createEmptyFile();
        MultipartFile attachFile = signupDto.getAttachFile();
        if (attachFile != null && !attachFile.isEmpty()) {
            uploadFile = fileStore.storeFile(attachFile);
        }
        String username = principal.getName();
        memberService.changeImage(username, uploadFile);

        return "redirect:/member/mypage";
    }
}
