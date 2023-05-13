package biz.digissance.homiedemo.service.jwt;

import biz.digissance.homiedemo.security.MyUser;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final String cookieName;
    private final int tokenAge;
    private final boolean isCookieSecure;

    public TokenService(JwtEncoder encoder,
                        @Value("${app.security.jwt.token_age:60}") final int tokenAge,
                        @Value("${app.security.cookie.secured:true}") final boolean isCookieSecure) {
        this.encoder = encoder;
        this.tokenAge = tokenAge;
        this.isCookieSecure = isCookieSecure;
        cookieName = "J_SEC";
    }

    public String generateToken(Authentication authentication) {
        final MyUser user = (MyUser) authentication.getPrincipal();
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE"))
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(tokenAge, ChronoUnit.SECONDS))
                .subject(user.getIdentifier())
                .claim("scope", scope)
                .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public Cookie generateCookie(final Authentication authentication) {
        Cookie cookie = new Cookie(cookieName, this.generateToken(authentication));
        cookie.setPath("/");
        cookie.setMaxAge(tokenAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(isCookieSecure);
        return cookie;
    }
}