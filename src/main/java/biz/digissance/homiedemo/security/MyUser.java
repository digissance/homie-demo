package biz.digissance.homiedemo.security;

import biz.digissance.homiedemo.domain.UserEntity;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MyUser extends User {

    @Getter
    private final String identifier;

    public MyUser(final UserEntity user) {
        super(user.getUsername(), user.getPassword(), Stream.of("READ", "ROLE_USER")
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        this.identifier = user.getIdentifier();
    }
}
