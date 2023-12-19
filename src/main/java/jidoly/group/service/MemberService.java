package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.controller.member.SignupDto;
import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registerMember(Member member) {
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            throw new RuntimeException("username already exists"); // 여기 MVC에서 message로 대체
        }
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setEncodePassword(encodedPassword);

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found")); // 여기도 MVC에서 message로 대체
    }

    public Member login(SignupDto signupDto) {
        String encodePassword = passwordEncoder.encode(signupDto.getPassword());
        return memberRepository.findByUsername(signupDto.getUsername())
                .filter(m -> m.getPassword().equals(encodePassword))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 및 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
