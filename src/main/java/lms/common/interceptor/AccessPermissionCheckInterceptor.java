package lms.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lms.member.domain.MemberRole;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * 권한에 따른 처리를 하기 위한 Interceptor
 * @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 를 선언하고
 * Controller 에서 @PreAuthorize 를 사용해도 되지만, Controller 로 가기 전, Interceptor 에서 처리를 하는 것이 낫다고 생각함
 */

public class AccessPermissionCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    String uri = request.getRequestURI();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // 로그인 성공 페이지와 공개 경로 허용
	    if (uri.equals("/loginSuccess") || uri.equals("/")) {
	        return true;
	    }

	    // 관리자 권한 확인
	    if (uri.startsWith("/admin")) {
	        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()))) {
	            return true;
	        }
	    } else if (uri.startsWith("/professor")){
	        // 교수 권한 확인
	        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.PROFESSOR.getValue()))) {
	            return true;
	        }
	    }else if (uri.startsWith("/student")){
	        // 학생 권한 확인
	        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.STUDENT.getValue()))) {
	            return true;
	        }
	    }else if (uri.startsWith("user")){
	        // 일반 사용자 권한 확인
	        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.USER.getValue()))) {
	            return true;
	        }
	    }

	    response.setStatus(SC_FORBIDDEN);
	    response.setCharacterEncoding("utf-8");
	    new ObjectMapper().writeValue(response.getWriter(), "접근 권한이 없습니다.");
	    return false;
	}
}