package lms.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lms.member.domain.Member;
import lms.member.domain.MemberDTO;
import lms.security.exception.InputNotFoundException;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberDTO memberDTO = objectMapper.readValue(request.getInputStream(), MemberDTO.class);

            if (memberDTO.getId() == null || memberDTO.getPassword() == null) {
                throw new AuthenticationServiceException("아이디와 비밀번호를 입력해주세요.");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDTO.getId(), memberDTO.getPassword());

            return this.getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 요청 처리 중 오류가 발생했습니다.", e);
        }
    }



}