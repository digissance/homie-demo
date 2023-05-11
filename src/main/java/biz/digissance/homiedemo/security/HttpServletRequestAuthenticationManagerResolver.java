package biz.digissance.homiedemo.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class HttpServletRequestAuthenticationManagerResolver
        implements AuthenticationManagerResolver<HttpServletRequest> {
    @Getter(lazy = true)
    private final AuthenticationManager cached = expensive();
    private final HttpSecurity http;

    public HttpServletRequestAuthenticationManagerResolver(final HttpSecurity http) {
        this.http = http;
    }

    private AuthenticationManager expensive() {
        return http.getSharedObject(AuthenticationManager.class);
    }

    @Override
    public AuthenticationManager resolve(final HttpServletRequest context) {
        return getCached();
    }
}
