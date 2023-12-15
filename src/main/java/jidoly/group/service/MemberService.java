package jidoly.group.service;

import jakarta.persistence.EntityNotFoundException;
import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member registerMember(String username, String password, String nick) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("username already exists"); // 여기 MVC에서 message로 대체
        }

        String encodedPassword = passwordEncoder.encode(password);
        Member member = Member.createMember(username, encodedPassword, nick);
        return memberRepository.save(member);
    }

    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found")); // 여기도 MVC에서 message로 대체
    }
}
