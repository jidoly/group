package jidoly.group.controller.member;

import jidoly.group.controller.member.dto.SignupDto;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignupValidator extends AbstractValidator<SignupDto>{

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(SignupDto signupDto, Errors errors) {

        if (!signupDto.getPassword().equals(signupDto.getValidPw())) {
            errors.rejectValue("validPw", "passwordFail");
        }

        if (memberRepository.existsByUsername(signupDto.getUsername())) {
            errors.rejectValue("username","duplicate");
        }
        if (memberRepository.existsByNickname(signupDto.getNickname())) {
            errors.rejectValue("nickname","duplicate");
        }

        if(errors.hasErrors()){
            log.error("errors={}", errors);
            errors.reject("global");
        }
    }
}
