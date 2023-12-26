package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import jidoly.group.repository.JoinRepository;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JoinRepository joinRepository;

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


    public Boolean loginCheck(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElse(null);
        if (member != null) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 및 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Transactional
    public void updateNick(String username, String nickname) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.changeNick(nickname);
    }


    @Transactional
    public void updatePw(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        String encodedPassword = passwordEncoder.encode(password);
        member.setEncodePassword(encodedPassword);
    }

    public List<Join> findMyGroups(Long userId) {
        return joinRepository.findMyGroups(userId);
    }

    @Transactional
    public void changeImage(String username, UploadFile uploadFile) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + username));
        member.getUploadFiles().clear();
        member.addFiles(uploadFile);
    }
}
