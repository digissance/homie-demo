package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.security.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/token")
    public MyJwtToken token(Authentication authentication, final HttpServletRequest request) {
        log.debug("Authenticated {}", authentication);
        final var token = (String) request.getAttribute(SecurityConfig.J_SEC_COOKIE_NAME);
        return new MyJwtToken(token);
    }
}
