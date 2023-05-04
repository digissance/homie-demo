package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import java.util.List;
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

    private final UserEntityRepository repository;
    private final SpaceEntityRepository spaceEntityRepository;

    public UserController(final UserEntityRepository repository,
                          final SpaceEntityRepository spaceEntityRepository) {
        this.repository = repository;
        this.spaceEntityRepository = spaceEntityRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserEntity user, UriComponentsBuilder uri) {
        repository.save(user);
        return ResponseEntity.created(uri.build().toUri()).build();
    }

    @GetMapping("/{ownerId}/spaces")
    public final ResponseEntity<List<SpaceEntity>> getOwnerSpaces(final @PathVariable long ownerId,
                                                                  final UriComponentsBuilder uri) {

        return ResponseEntity.ok(spaceEntityRepository.findByOwnerId(ownerId));
    }

    @GetMapping
    public final ResponseEntity<List<UserEntity>> getAllUsers() {

        return ResponseEntity.ok(repository.findAll());
    }
}
