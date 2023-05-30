package biz.digissance.homiedemo.http.user;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.service.user.UserService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(final UserService service,
                          final SpaceEntityRepository spaceEntityRepository,
                          final ElementMapper mapper) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto request, UriComponentsBuilder uri) {
        final var user = service.create(request.getUsername(), request.getPassword());
        return ResponseEntity.created(uri
                .path("/users/{id}")
                .buildAndExpand(Map.of("id", user.getIdentifier()))
                .toUri()).build();
    }
}
