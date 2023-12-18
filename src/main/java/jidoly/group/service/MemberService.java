package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.controller.member.LoginDto;
import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Member login(LoginDto loginDto) {
        String encodePassword = passwordEncoder.encode(loginDto.getPassword());
        return memberRepository.findByUsername(loginDto.getUsername())
                .filter(m -> m.getPassword().equals(encodePassword))
                .orElse(null);
    }
}
