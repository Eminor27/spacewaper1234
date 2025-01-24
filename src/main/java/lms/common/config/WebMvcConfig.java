package lms.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lms.common.interceptor.AccessPermissionCheckInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessPermissionCheckInterceptor())
                .order(1)
                .excludePathPatterns("/", "/login","/main", "/loginHome", "/signUp", "/renew", "/loginSuccess",
                        "/login/oauth2/code/**", "/oauth2/signUp", "/error", "/js/**");
    }
}
