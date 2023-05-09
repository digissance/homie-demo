package biz.digissance.homiedemo.security;

import static org.springframework.security.config.Customizer.withDefaults;

import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import biz.digissance.homiedemo.service.TokenService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String J_SEC_COOKIE_NAME = "J_SEC";
    @Value("${app.security.jwt.key}")
    private String jwtKey;

    @Value("${app.security.debug.enabled:false}")
    private boolean securityDebugEnable;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .and().debug(securityDebugEnable);
    }

    @Bean
    public UserDetailsService userDetailsService(final UserEntityRepository repository) {
        return username -> repository.findOne(Example.of(UserEntity.builder()
                        .username(username)
                        .build()))
                .map(MyUser::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(final UserDetailsService userDetailsService,
                                                            final PasswordEncoder passwordEncoder) {
        final var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            TokenService tokenService,
            DaoAuthenticationProvider daoAuthenticationProvider,
            final UserDetailsService userDetailsService,
            JwtDecoder jwtDecoder)
            throws Exception {

        final BearerTokenResolver bearerTokenResolver = request ->
                Optional.ofNullable(request.getCookies()).stream()
                        .flatMap(Arrays::stream)
                        .filter(cookie -> J_SEC_COOKIE_NAME.equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElseGet(() -> (String) request.getAttribute(J_SEC_COOKIE_NAME));
        final var bearerPostFilter = new MyBearerTokenAuthenticationFilter(http, tokenService);
        bearerPostFilter.setBearerTokenResolver(bearerTokenResolver);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterAfter(bearerPostFilter, RememberMeAuthenticationFilter.class)
                .logout(l -> l.deleteCookies(J_SEC_COOKIE_NAME))
                .httpBasic(withDefaults())
                .rememberMe(r -> {
                    r.key(jwtKey);
                    r.alwaysRemember(true);
                    r.userDetailsService(userDetailsService);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt();
                    oauth2.bearerTokenResolver(bearerTokenResolver);
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health/liveness", "/actuator/health/readiness").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/api/auth/token").hasRole("USER")
                        .anyRequest().hasAuthority("SCOPE_READ")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = jwtKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
