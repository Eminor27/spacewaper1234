package lms.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lms.member.repository.MemberRepository;
import lms.security.domain.UserDetailsImpl;
import lms.security.exception.UserNotFoundException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws AuthenticationException {
        return memberRepository.findById(id)
            .map(member -> new UserDetailsImpl(
                member,
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue()))
            ))
            .orElseThrow(() -> new UserNotFoundException("등록되지 않은 사용자입니다"));
    }
}
