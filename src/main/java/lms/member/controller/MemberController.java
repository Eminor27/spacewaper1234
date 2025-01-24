package lms.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lms.member.domain.Member;
import lms.member.domain.MemberDTO;
import lms.member.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    @ResponseBody
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberDTO memberDTO, BindingResult bindingResult) {
        // 유효성 검사 에러 처리
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        // 중복 확인
        if (memberService.findById(memberDTO.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
        if (memberService.findByEmail(memberDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        // 회원 저장
        Member savedMember = memberService.save(memberDTO);
        if (savedMember == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패했습니다.");
        }

        // 기본 역할 (예: ROLE_USER) 할당
        boolean roleAssigned = memberService.assignRole(savedMember.getId(), "ROLE_USER");
        if (!roleAssigned) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기본 역할 할당에 실패했습니다.");
        }

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
