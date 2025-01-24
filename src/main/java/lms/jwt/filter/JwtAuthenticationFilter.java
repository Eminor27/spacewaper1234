package lms.jwt.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lms.jwt.constants.JwtConstants;
import lms.jwt.constants.JwtUtils;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static lms.jwt.constants.JwtUtils.verifyToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/", "/login", "/loginHome", "/signUp", "/renew", "/loginSuccess",
                                            "/login/oauth2/code/**", "/oauth2/signUp", "/error", "/js/**"};

    // 필터를 거치지 않을 URL 을 설정하고, true 를 return 하면 바로 다음 필터를 진행하게 됨
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String header = request.getHeader(JwtConstants.JWT_HEADER);

        // 로그 추가
        log.debug("Request URI: {}", request.getRequestURI());
        log.debug("Authorization Header: {}", header);

        // 세션에서 인증 정보 확인
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            log.debug("User is already authenticated via session.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 없거나 잘못된 경우 처리
        if (header == null || !header.startsWith(JwtConstants.JWT_TYPE)) {
            log.warn("Token 이 존재하지 않습니다");
            response.setStatus(SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getWriter(), "Token 이 존재하지 않습니다");
            return;
        }

        try {
            // 토큰 검증
            String token = JwtUtils.getTokenFromHeader(header);
            log.debug("Extracted Token: {}", token);

            DecodedJWT decodedJWT = verifyToken(token);
            UsernamePasswordAuthenticationToken authenticationToken = JwtUtils.getAuthenticationToken(decodedJWT);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (TokenExpiredException e) {
            log.error("Access Token 이 만료되었습니다: {}", e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getWriter(), "Access Token 이 만료되었습니다.");
        } catch (Exception e) {
            log.error("올바르지 않은 Token 입니다: {}", e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getWriter(), "올바르지 않은 Token 입니다.");
        }
    }
}
