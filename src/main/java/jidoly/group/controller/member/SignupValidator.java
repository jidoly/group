package jidoly.group.controller.member;

import jidoly.group.controller.member.dto.SignupDto;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class SignupValidator extends AbstractValidator<SignupDto>{

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(SignupDto signupDto, Errors errors) {

        if (!signupDto.getPassword().equals(signupDto.getValidPw())) {
            errors.rejectValue("password", "passwordFail", "비밀번호가 동일하지 않습니다.");
        }

        if (memberRepository.existsByUsername(signupDto.getUsername())) {
            errors.rejectValue("username","duplicate", "이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByNickname(signupDto.getNickname())) {
            errors.rejectValue("nickname","duplicateNick","이미 존재하는 닉네임입니다.");
        }

        if(errors.hasErrors()){
            errors.reject("global", "오류가 발생했습니다 다시 시도하세요.");
            System.err.println("바인딩 result 에러");
        }
    }
}
