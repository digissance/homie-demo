package biz.digissance.homiedemo.http.user;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final SpaceEntityRepository spaceEntityRepository;
    private final ElementMapper mapper;

    public UserController(final UserService service,
                          final SpaceEntityRepository spaceEntityRepository,
                          final ElementMapper mapper) {
        this.service = service;
        this.spaceEntityRepository = spaceEntityRepository;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto request, UriComponentsBuilder uri) {
        final var user = service.create(request.getUsername(), request.getPassword());
        return ResponseEntity.created(uri
                .path("/users/{id}")
                .buildAndExpand(Map.of("id", user.getIdentifier()))
                .toUri()).body(user);
    }

    @GetMapping("/{ownerId}/spaces")
    public final ResponseEntity<List<SpaceDto>> getOwnerSpaces(final @PathVariable String ownerId,
                                                               final UriComponentsBuilder uri) {

        return ResponseEntity.ok(spaceEntityRepository.findByOwnerIdentifier(ownerId).stream()
                .map(mapper::toSpaceDto).collect(Collectors.toList()));
    }
}
