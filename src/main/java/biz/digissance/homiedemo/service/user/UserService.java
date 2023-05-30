package biz.digissance.homiedemo.service.user;

import biz.digissance.homiedemo.http.dto.UserDto;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findByName(String name);

    UserDto create(final String name, final String password);
}
