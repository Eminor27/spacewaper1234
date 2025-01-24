package lms.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/assign-role")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @PostMapping("/student")
    public ResponseEntity<String> assignStudentRole(@RequestParam String username) {
        boolean success = memberService.assignRole(username, "ROLE_STUDENT");
        return success
            ? ResponseEntity.ok("학생 역할이 부여되었습니다.")
            : ResponseEntity.badRequest().body("역할 부여 실패: 사용자 이름을 확인하세요.");
    }



    @PostMapping("/professor")
    public ResponseEntity<String> assignProfessorRole(@RequestParam String username) {
        boolean success = memberService.assignRole(username, "ROLE_PROFESSOR");
        return success
            ? ResponseEntity.ok("교수 역할이 부여되었습니다.")
            : ResponseEntity.badRequest().body("역할 부여 실패: 사용자 아이디를 확인하세요.");
    }
    
    @PostMapping("/user")
    public ResponseEntity<String> assignUserRole(@RequestParam String username) {
        boolean success = memberService.assignRole(username, "ROLE_USER");
        return success
            ? ResponseEntity.ok("유저 역할이 부여되었습니다.")
            : ResponseEntity.badRequest().body("역할 부여 실패: 사용자 이름을 확인하세요.");
    }

}

