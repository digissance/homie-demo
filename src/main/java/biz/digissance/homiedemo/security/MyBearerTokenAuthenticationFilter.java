package biz.digissance.homiedemo.security;

import biz.digissance.homiedemo.service.jwt.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

class MyBearerTokenAuthenticationFilter extends BearerTokenAuthenticationFilter {
    private final TokenService tokenService;

    public MyBearerTokenAuthenticationFilter(final HttpSecurity http, final TokenService tokenService) {
        super(new HttpServletRequestAuthenticationManagerResolver(http));
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(SecurityContextHolder.getContextHolderStrategy())
                .map(SecurityContextHolderStrategy::getContext)
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth.getPrincipal() instanceof MyUser)
                .map(tokenService::generateCookie)
                .ifPresentOrElse(p -> {
                    response.addCookie(p);
                    request.setAttribute(SecurityConfig.J_SEC_COOKIE_NAME, p.getValue());
                    try {
                        if (request.getServletPath().equals("/api/auth/token")) {
                            filterChain.doFilter(request, response);
                        } else {
                            super.doFilterInternal(request, response, filterChain);
                        }
                        request.removeAttribute(SecurityConfig.J_SEC_COOKIE_NAME);
                    } catch (ServletException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (IOException | ServletException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
