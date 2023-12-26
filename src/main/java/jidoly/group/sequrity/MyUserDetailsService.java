package jidoly.group.sequrity;

import jidoly.group.domain.Member;
import jidoly.group.repository.MemberRepository;
import jidoly.group.service.MemberService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MyUserDetailsService(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
        Optional<Member> findOne = memberRepository.findByUsername(insertedUserId);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 사용자입니다."));

        // 사용자에게 직접 권한을 부여
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROOT_USER"), new SimpleGrantedAuthority("NORMAL_USER"));

        return new CustomUser(
                member.getUsername(),
                member.getPassword(),
                authorities,
                member.getId()
        );
    }
}