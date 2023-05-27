package biz.digissance.homiedemo.security;

import biz.digissance.homiedemo.service.jwt.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

class MyBearerTokenAuthenticationFilter extends BearerTokenAuthenticationFilter {
    private final TokenService tokenService;

    public MyBearerTokenAuthenticationFilter(final HttpSecurity http, final TokenService tokenService) {
        super(new HttpServletRequestAuthenticationManagerResolver(http));
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final var cookie = Optional.ofNullable(SecurityContextHolder.getContextHolderStrategy())
                .map(SecurityContextHolderStrategy::getContext)
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth.getPrincipal() instanceof MyUser)
                .map(tokenService::generateCookie);
        if (cookie.isPresent()) {
            response.addCookie(cookie.get());
            request.setAttribute(SecurityConfig.J_SEC_COOKIE_NAME, cookie.get().getValue());
            if (request.getServletPath().equals("/api/auth/token")) {
                filterChain.doFilter(request, response);
            } else {
                super.doFilterInternal(request, response, filterChain);
            }
            request.removeAttribute(SecurityConfig.J_SEC_COOKIE_NAME);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
