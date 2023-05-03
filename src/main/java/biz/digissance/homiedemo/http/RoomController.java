package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.ItemEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import biz.digissance.homiedemo.domain.StuffEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.StuffEntityRepository;
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
@RequestMapping("/rooms")
public class RoomController {

    private final ElementEntityRepository repository;
    private final StuffEntityRepository stuffEntityRepository;
    private final ElementMapper mapper;

    public RoomController(final ElementEntityRepository repository,
                          final StuffEntityRepository stuffEntityRepository,
                          final ElementMapper mapper) {
        this.repository = repository;
        this.stuffEntityRepository = stuffEntityRepository;
        this.mapper = mapper;
    }

    @PostMapping("/{id}/items")
    public final ResponseEntity<ItemEntity> createItem(final @PathVariable long id,
                                                       final @RequestBody CreateElementRequest request,
                                                       final UriComponentsBuilder uri) {
        final var element = repository.save(mapper.toItemEntity(id, request));
        return ResponseEntity.created(uri.build().toUri())
                .body(element);
    }

    @PostMapping("/{id}/storage")
    public final ResponseEntity<StorageEntity> createStorage(final @PathVariable long id,
                                                             final @RequestBody CreateElementRequest request,
                                                             final UriComponentsBuilder uri) {
        final var element = repository.save(mapper.toStorageEntity(id, request));
        return ResponseEntity.created(uri.build().toUri())
                .body(element);
    }

    @GetMapping("/{id}/elements")
    public final ResponseEntity<List<StuffEntity>> getElements(final @PathVariable long id,
                                                               final UriComponentsBuilder uri) {
        return ResponseEntity.ok(stuffEntityRepository.findByParentId(id));
    }
}
