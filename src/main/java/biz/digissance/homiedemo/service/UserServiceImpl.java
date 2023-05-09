package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserEntityRepository repository;
    private final ElementMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserEntityRepository repository,
                           final ElementMapper mapper,
                           final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserDto> findByName(final String name) {
        return repository.findOne(Example.of(UserEntity.builder().username(name).build())).map(mapper::toUserDto);
    }

    @Override
    public UserDto create(final String name, final String password) {
        return mapper.toUserDto(repository.save(UserEntity.builder()
                .username(name)
                .password(passwordEncoder.encode(password))
                .build()));
    }
}
