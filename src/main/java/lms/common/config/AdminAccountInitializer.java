package lms.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lms.member.domain.Member;
import lms.member.domain.MemberRole;
import lms.member.repository.MemberRepository;

@Component
public class AdminAccountInitializer {

    @Bean
    public CommandLineRunner createAdminAccount(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "1234";
            String adminEmail = "admin@example.com";

            if (memberRepository.findById(adminUsername).isEmpty()) {
            	Member admin = Member.builder()
            	        .id(adminUsername)
            	        .name("관리자")
            	        .password(passwordEncoder.encode(adminPassword)) // 암호화된 비밀번호 저장
            	        .email(adminEmail)
            	        .role(MemberRole.ADMIN)
            	        .build();
                memberRepository.save(admin);
                System.out.println("Admin account created: " + adminUsername);
            } else {
                System.out.println("Admin account already exists.");
            }
        };
    }
}
