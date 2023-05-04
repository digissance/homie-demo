package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserEntityRepository repository;
    private final ElementMapper mapper;

    public UserServiceImpl(final UserEntityRepository repository,
                           final ElementMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<UserDto> findByName(final String name) {
        return repository.findOne(Example.of(UserEntity.builder().name(name).build())).map(mapper::toUserDto);
    }

    @Override
    public UserDto create(final String name) {
        return mapper.toUserDto(repository.save(UserEntity.builder().name(name).build()));
    }
}
