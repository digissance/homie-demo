package biz.digissance.homiedemo.http;

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
    public void token(Authentication authentication) {
        log.debug("Authenticated {}", authentication);
    }
}
