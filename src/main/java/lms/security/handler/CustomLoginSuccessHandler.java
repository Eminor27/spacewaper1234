package lms.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lms.jwt.constants.JwtConstants;
import lms.jwt.constants.JwtUtils;
import lms.jwt.domain.RefreshToken;
import lms.jwt.service.JwtService;
import lms.member.domain.Member;
import lms.security.domain.UserDetailsImpl;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 인증 성공 후 사용자 정보 가져오기
        Member member = ((UserDetailsImpl) authentication.getPrincipal()).getMember();

        // JWT 토큰 생성 및 저장
        String accessToken = JwtUtils.generateAccessToken(member);
        String refreshToken = JwtUtils.generateRefreshToken(member);
        jwtService.save(new RefreshToken(refreshToken, member.getId()));

        // 헤더로 JWT 토큰 전달
        response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + accessToken);
        response.addHeader(JwtConstants.REFRESH, JwtConstants.JWT_TYPE + refreshToken);

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // 사용자 권한에 따른 리다이렉트 설정
        String redirectUrl = determineRedirectUrl(authentication.getAuthorities());
        System.out.println("Selected Redirect URL: " + redirectUrl); // 디버깅용 로그
        response.sendRedirect(redirectUrl);
    }


    private String determineRedirectUrl(Collection<? extends GrantedAuthority> authorities) {
        System.out.println("User Authorities: " + authorities); // 디버깅용 로그

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            switch (role) {
                case "ADMIN":
                    return "/adminPage";
                case "PROFESSOR":
                    return "/professorPage";
                case "STUDENT":
                    return "/studentPage";
                case "USER":
                	return "/main";
                default:
                    return "/loginSuccess"; // 기본 리다이렉트
            }
        }
        // 모든 권한에 대해 리다이렉트가 설정되지 않은 경우
        return "/loginHome";
    }

}
