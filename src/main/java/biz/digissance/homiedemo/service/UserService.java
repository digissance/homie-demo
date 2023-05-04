package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.dto.UserDto;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findByName(String name);

    UserDto create(String name);
}
