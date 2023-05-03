package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.RoomEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.service.SpaceDto;
import biz.digissance.homiedemo.service.SpaceService;
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
@RequestMapping("/spaces")
public class SpaceController {

    private final ElementEntityRepository repository;
    private final SpaceEntityRepository spaceEntityRepository;
    private final RoomEntityRepository roomEntityRepository;
    private final ElementMapper mapper;

    private final SpaceService service;

    public SpaceController(final ElementEntityRepository repository,
                           final SpaceEntityRepository spaceEntityRepository,
                           final RoomEntityRepository roomEntityRepository,
                           final ElementMapper mapper,
                           final SpaceService service) {
        this.repository = repository;
        this.spaceEntityRepository = spaceEntityRepository;
        this.roomEntityRepository = roomEntityRepository;
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping
    public final ResponseEntity<SpaceEntity> createSpace(final @RequestBody CreateSpaceRequest request,
                                                         final UriComponentsBuilder uri) {
        final var space = service.createSpace(request);
        return ResponseEntity.created(uri.build().toUri())
                .body(space);
    }

    @PostMapping("/{id}/rooms")
    public final ResponseEntity<RoomEntity> createRoom(final @PathVariable long id,
                                                       final @RequestBody CreateElementRequest request,
                                                       final UriComponentsBuilder uri) {
        final var element = service.createRoom(id, request);
        return ResponseEntity.created(uri.build().toUri())
                .body(element);
    }

    @GetMapping("/{id}/rooms")
    public final ResponseEntity<List<RoomEntity>> getRooms(final @PathVariable long id,
                                                           final UriComponentsBuilder uri) {
        return ResponseEntity.ok(service.getRooms(id));
    }

    @GetMapping("/whole-tree/{id}")
    public final ResponseEntity<SpaceDto> getWholeSpace(final @PathVariable long id,
                                                        final UriComponentsBuilder uri) {
        return ResponseEntity.ok(service.getSpaceTree(id));
    }
}
