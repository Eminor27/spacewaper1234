package lms.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lms.member.domain.Member;
import lms.member.domain.MemberDTO;
import lms.member.domain.MemberRole;
import lms.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(MemberDTO dto) {
        Member user = Member.createUser(dto, passwordEncoder);
        return memberRepository.save(user);
    }

    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    

    /**
     * 사용자에게 역할(Role)을 할당하는 메서드
     *
     * @param username 사용자 이름
     * @param role     부여할 역할 (예: ROLE_STUDENT, ROLE_PROFESSOR)
     * @return 성공 여부 (true: 성공, false: 실패)
     */
    public boolean assignRole(String name, String role) {
        System.out.println("Assigning role to name: " + name + ", role: " + role);

        Optional<Member> memberOptional = memberRepository.findByName(name);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            System.out.println("User found: " + member);

            // MemberRole의 value 속성과 role 비교
            MemberRole assignedRole = Arrays.stream(MemberRole.values())
                .filter(r -> r.getValue().equals(role))
                .findFirst()
                .orElse(null);

            if (assignedRole != null) {
                member.setRole(assignedRole);
                memberRepository.save(member);
                System.out.println("Role assigned successfully: " + assignedRole);
                return true;
            } else {
                System.out.println("Invalid role: " + role);
            }
        } else {
            System.out.println("User not found");
        }
        return false;
    }

}
